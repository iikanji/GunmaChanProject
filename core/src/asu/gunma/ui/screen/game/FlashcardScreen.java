package asu.gunma.ui.screen.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.SimpleDirectionGestureDetector;
import asu.gunma.ui.util.GradeSystem;


public class FlashcardScreen implements Screen {

    private Game game;
    public ActionResolver speechGDX;
    private GradeSystem gradeSystem;
    public DbInterface dbCallback;
    public Screen previousScreen;
    public Music gameMusic;
    public static float masterVolume = 5;
    public String wordAudioFile;
    private int listCounter = 0;
    private String displayWord;
    private List<VocabWord> dbListWords;
    public ArrayList<VocabWord> vocabWordArrayList;
  
    private final int CORRECT_GREENCIRCLE_DURATION = 80;
    private final int INCORRECT_REDX_DURATION = 80;
    private int correctDisplayTimer;
    private int incorrectDisplayTimer;

    private Stage stage;
    private Stage stage2;
    private BitmapFont font;
    private BitmapFont font2;
    private SpriteBatch batch;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter2;

    private GlyphLayout displayWordLayout;
    private int targetWidth = 400;

    private Texture cabbage;
    private Texture happyneg;
    private Texture konjackun;
    private Texture greenCircle;
    private Texture redX;

    private Label backInstruction;

    String[] correctWordList;
    String cWords;
    String incomingWord;
    Boolean correct = false;

    //private Skin leftArrow;
    //private Skin rightArrow;

    private TextButton backButton;
    private TextButton buttonRecord;
    private TextButton speakButton;

    private TextButton nextButton;
    private TextButton prevButton;
    private TextButton flipButton;
    public Preferences prefs;


    public FlashcardScreen (Game game, ActionResolver speechGDX, Music music,
                            DbInterface dbCallback, Screen previousScreen, ArrayList<VocabWord> arrayList, Preferences prefs) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbCallback = dbCallback;
        this.previousScreen = previousScreen;
        this.vocabWordArrayList = arrayList;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, .8f, 1, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        //leftArrow = new Skin(Gdx.files.internal("leftArrow.json"));
        //rightArrow = new Skin(Gdx.files.internal("rightArrow.json"));

        /*cabbage = new Texture("cabbage1.png");
        happyneg = new Texture("happyneg.png");
        konjackun = new Texture("konjackun.jpg");
        negisan = new Texture("negisan.png");
        index_card = new Texture("index_card.png");*/
        greenCircle = new Texture("greenCircle.png");
        redX = new Texture("redX.png");

        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //font for other words
        parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        dbListWords = dbCallback.getDbVocab();
        displayWord = dbListWords.get(listCounter).getEngSpelling();

        //setting font values
        parameter.characters = displayWord;
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        parameter2.size = 30;
        parameter2.color = Color.BLACK;
        font2 = generator.generateFont(parameter2);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font2, Color.BLACK);
        backInstruction = new Label("Swipe Right On First Word to Go Back", headingStyle);
        backInstruction.setPosition(450, 450);
        backInstruction.setFontScale(2);

        //Alignment and Text Wrapping for Vocab Word
        displayWordLayout = new GlyphLayout();
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        cWords = dbListWords.get(listCounter).getCorrectWords();
        correctWordList = cWords.split("\\s*,\\s*");
        System.out.println(listCounter);

        wordAudioFile = dbListWords.get(listCounter).getAudio();
        System.out.print(wordAudioFile);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;


        //maybe swap onUp and onDown functionality to invisible button

        //Enables Gesture swipes
       /* Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector
                (new SimpleDirectionGestureDetector.DirectionListener() {
            @Override
            public void onUp() {
                if(displayWord == dbListWords.get(listCounter).getEngSpelling()) {
                    displayWord = dbListWords.get(listCounter).getKanjiSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(.2f, 1, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
                else if(displayWord == dbListWords.get(listCounter).getKanjiSpelling()) {
                    displayWord = dbListWords.get(listCounter).getEngSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(1, .8f, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
            }

            @Override
            public void onRight() {
                if (listCounter > 0) {

                    if(displayWord == dbListWords.get(listCounter).getEngSpelling()){
                        listCounter = listCounter - 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord == dbListWords.get(listCounter).getKanjiSpelling()){
                        listCounter = listCounter - 1;
                        displayWord = dbListWords.get(listCounter).getKanjiSpelling();
                        Gdx.gl.glClearColor(.2f, 1, 1, 1);

                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
                // THIS EXITS THE FLASHCARD SCREEN BECAUSE A BUTTON DOES NOT WORK WHEN GESTURES
                // ARE ACTIVE
                else if(listCounter == 0){
                    dispose(); // dispose of current FlashScreen
                    previousScreen.dispose();
                    game.setScreen(new MainMenuScreen(game, speechGDX, dbCallback));
                }
            }

            @Override
            public void onLeft() {
                if(listCounter < dbListWords.size()) {
                    if(displayWord == dbListWords.get(listCounter).getEngSpelling()){
                        listCounter = listCounter + 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord == dbListWords.get(listCounter).getKanjiSpelling()){
                        listCounter = listCounter + 1;
                        displayWord = dbListWords.get(listCounter).getKanjiSpelling();
                        Gdx.gl.glClearColor(.2f, 1, 1, 1);
                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
            }

            @Override
            public void onDown() {
                if(displayWord == dbListWords.get(listCounter).getEngSpelling()) {
                    displayWord = dbListWords.get(listCounter).getKanjiSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(.2f, .8f, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
                else if(displayWord == dbListWords.get(listCounter).getKanjiSpelling()) {
                    displayWord = dbListWords.get(listCounter).getEngSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(1, .8f, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
            }
        }));*/

        backButton = new TextButton("Back", textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        buttonRecord = new TextButton("Listen", textButtonStyle);
        buttonRecord.setPosition(800, Gdx.graphics.getHeight() - 550);

        speakButton = new TextButton("Speak", textButtonStyle);
        speakButton.setPosition(100 , Gdx.graphics.getHeight() - 550);


        flipButton = new TextButton("Flip", textButtonStyle);
        flipButton.setPosition(475, Gdx.graphics.getHeight() - 50);

        prevButton = new TextButton("Previous", textButtonStyle);
        prevButton.setPosition(50, 275);

        nextButton = new TextButton("Next", textButtonStyle);
        nextButton.setPosition(Gdx.graphics.getWidth() - 100, 275);

        speakButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(Gdx.files.internal(wordAudioFile).exists()) {
                    Music wordSound  =  Gdx.audio.newMusic(Gdx.files.internal(wordAudioFile));
                    if(wordSound != null) {
                        wordSound.play();
                        wordSound.setLooping(false);
                    }
                }
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX, gameMusic, dbCallback, vocabWordArrayList, prefs));
                previousScreen.dispose();
                dispose(); // dispose of current FlashScreen
            }
        });

        buttonRecord.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // I have it in reverse order like this because it makes more sense
                // but I can't think of a good variable name for it to not be backwards
                try {
                    speechGDX.listenOnce();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });


        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(listCounter < dbListWords.size() - 1) {

                    if(displayWord.equals(dbListWords.get(listCounter).getEngSpelling())){
                        listCounter = listCounter + 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord.equals(dbListWords.get(listCounter).getKanjiSpelling())){
                        listCounter = listCounter + 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                    wordAudioFile = dbListWords.get(listCounter).getAudio();
                    System.out.print(wordAudioFile);
                } else {
                    if(displayWord.equals(dbListWords.get(listCounter).getEngSpelling())){
                        listCounter = 0;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord.equals(dbListWords.get(listCounter).getKanjiSpelling())){
                        listCounter = 0;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                    wordAudioFile = dbListWords.get(listCounter).getAudio();
                }
                cWords = dbListWords.get(listCounter).getCorrectWords();
                correctWordList = cWords.split("\\s*,\\s*");
            }
        });

        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listCounter > 0) {

                    if(displayWord.equals(dbListWords.get(listCounter).getEngSpelling())){
                        listCounter = listCounter - 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord.equals(dbListWords.get(listCounter).getKanjiSpelling())){
                        listCounter = listCounter - 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);

                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                    wordAudioFile = dbListWords.get(listCounter).getAudio();
                    System.out.print(wordAudioFile);
                }

                else if(listCounter == 0){
                    if(displayWord.equals(dbListWords.get(listCounter).getEngSpelling())){
                        listCounter = dbListWords.size() - 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);
                    }
                    else if(displayWord.equals(dbListWords.get(listCounter).getKanjiSpelling())){
                        listCounter = dbListWords.size() - 1;
                        displayWord = dbListWords.get(listCounter).getEngSpelling();
                        Gdx.gl.glClearColor(1, .8f, 1, 1);

                    }
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                    wordAudioFile = dbListWords.get(listCounter).getAudio();
                    System.out.print(wordAudioFile);
                }
                cWords = dbListWords.get(listCounter).getCorrectWords();
                correctWordList = cWords.split("\\s*,\\s*");
            }
        });

        flipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(displayWord.equals(dbListWords.get(listCounter).getEngSpelling())){
                    displayWord = dbListWords.get(listCounter).getKanjiSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(.2f, .8f, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
                else if(displayWord.equals(dbListWords.get(listCounter).getKanjiSpelling())){
                    displayWord = dbListWords.get(listCounter).getEngSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 60;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    Gdx.gl.glClearColor(1, .8f, 1, 1);
                    displayWordLayout.setText(font, displayWord, Color.BLACK,
                            targetWidth, Align.center, true);
                }
            }
        });


        stage.addActor(buttonRecord);
        //stage.addActor(backInstruction);
        stage.addActor(backButton);
        stage.addActor(speakButton);
        stage.addActor(nextButton);
        stage.addActor(prevButton);
        stage.addActor(flipButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        font.draw(batch, displayWordLayout, 300, 350);

        incomingWord = speechGDX.getWord();
        correct = gradeSystem.grade(correctWordList, incomingWord);

        if(correct){
            // Start correct icon display

            this.correctDisplayTimer = this.CORRECT_GREENCIRCLE_DURATION;
            /*

        //batch.draw(index_card, 50, 150);
        font.draw(batch, displayWordLayout, 300, 350);
        //batch.draw(this.cabbage, 750, 40);
        //batch.draw(this.happyneg, 800, 40);
        //batch.draw(this.konjackun, 850, 40);
        //batch.draw(negisan, 700, 20);
        batch.draw(greenCircle, 425, 450,50, 50);
        batch.draw(redX, 525, 450, 50, 50);

        if(gradeSystem.grade(correctWordList, speechGDX.getWord())){
            listCounter++;
            displayWord = dbListWords.get(listCounter).getEngSpelling();
            parameter.characters = displayWord;
            parameter.size = 70;
            parameter.color = Color.BLACK;
            font = generator.generateFont(parameter);
            displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);
            cWords = dbListWords.get(listCounter).getCorrectWords();
            correctWordList = cWords.split("\\s*,\\s*");
            */
            System.out.println("Correct incoming word " + incomingWord);
            incomingWord = null;

        } else if(!correct && incomingWord != null){
            // Start incorrect icon display

            this.incorrectDisplayTimer = this.INCORRECT_REDX_DURATION;
            System.out.println("Incorrect incoming words " + incomingWord);
            incomingWord = null;
        }

        if(correctDisplayTimer > 0) { this.correctAnswerGraphic();}
        if(incorrectDisplayTimer > 0) {this.incorrectAnswerGraphic();}
        batch.end();
        stage.act(delta); // optional to pass delta value
        stage.draw();
    }

    @Override
    public void dispose() {
        font2.dispose();
        font.dispose();
        generator.dispose();
        this.redX.dispose();
        this.greenCircle.dispose();
        batch.dispose();
        stage.dispose();
        //rightArrow.dispose();
        //leftArrow.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gameMusic.pause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    private void correctAnswerGraphic() {
        if (this.correctDisplayTimer == this.CORRECT_GREENCIRCLE_DURATION) {
            // Play sound effect here
        }
        batch.draw(greenCircle, 425, 450,50, 50);
        this.correctDisplayTimer--;
    }

    private void incorrectAnswerGraphic() {
        if (this.incorrectDisplayTimer == this.INCORRECT_REDX_DURATION) {
            // Play sound effect here
        }
        batch.draw(redX, 525, 450, 50, 50);
        this.incorrectDisplayTimer--;
    }
}
