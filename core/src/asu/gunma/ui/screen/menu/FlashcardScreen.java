package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.SimpleDirectionGestureDetector;

public class FlashcardScreen implements Screen {

    private Game game;
    public ActionResolver speechGDX;
    public DbInterface dbCallback;
    public Screen previousScreen;
    public Music music;
    private int listCounter = 0;
    private String displayWord;
    private List<VocabWord> dbListWords;

    private Stage stage;
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
    private Texture negisan;
    private Texture index_card;

    private Label backInstruction;

    public FlashcardScreen (Game game, ActionResolver speechGDX,
                            DbInterface dbCallback, Screen previous) {
        this.game = game;
        this.speechGDX = speechGDX;
        this.music = music;
        this.dbCallback = dbCallback;
        this.previousScreen = previous;

    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, .8f, 1, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);

        /*cabbage = new Texture("cabbage1.png");
        happyneg = new Texture("happyneg.png");
        konjackun = new Texture("konjackun.jpg");*/
        negisan = new Texture("negisan.png");
        index_card = new Texture("index_card.png");

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
        parameter2.size = 15;
        parameter2.color = Color.BLACK;
        font2 = generator.generateFont(parameter2);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font2, Color.BLACK);
        backInstruction = new Label("Swipe Right On First Word to Go Back", headingStyle);
        backInstruction.setPosition(450, 450);
        backInstruction.setFontScale(2);

        //Alignment and Text Wrapping for Vocab Word
        displayWordLayout = new GlyphLayout();
        displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

        String cWords = dbListWords.get(listCounter).getCorrectWords();
        String[] correctWordList = cWords.split("\\s*,\\s*");
        System.out.println(listCounter);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font2;
        textButtonStyle.fontColor = Color.BLACK;

        /*backButton = new TextButton("Back", textButtonStyle);
        backButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                dispose(); // dispose of current FlashScreen
                previousScreen.dispose();
                game.setScreen(new MainMenuScreen(game, speechGDX, dbCallback));
            }
        });*/

        //maybe swap onUp and onDown functionality to invisible button

        //Enables Gesture swipes
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector
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
        }));


        //stage.addActor(backButton);
        stage.addActor(backInstruction);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        //batch.draw(index_card, 50, 150);
        font.draw(batch, displayWordLayout, 75, 400);
        //batch.draw(this.cabbage, 750, 40);
        //batch.draw(this.happyneg, 800, 40);
        //batch.draw(this.konjackun, 850, 40);
        batch.draw(negisan, 700, 20);
        batch.end();
        stage.act(delta); // optional to pass delta value
        stage.draw();
    }

    @Override
    public void dispose() {
        font2.dispose();
        font.dispose();
        generator.dispose();
        index_card.dispose();
        negisan.dispose();
        batch.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
