package asu.gunma.ui.screen.game;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import asu.gunma.DatabaseInterface.*;

import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.Animator;
import asu.gunma.ui.util.BackgroundDrawer;
import asu.gunma.ui.util.GradeSystem;

public class GameScreen implements Screen {
        DbInterface dbCallback;
        private Game game;
        private Music gameMusic;
        public static float masterVolume = 0;
        public ActionResolver speechGDX;
        private Screen previousScreen;

        // Game logic variables
        private int score = 0;
        private int listCounter = 0;
        private String displayWord;
        private List<VocabWord> dbListWords;

        // Using these are unnecessary but will make our lives easier.
        private Stage stage;
        private TextureAtlas atlas;
        private Skin skin;
        private Table table;
        private SpriteBatch batch;

        private int lives;
        private float enemyPosition;

        private boolean isGameOver;

        /*
            We will need 5 different buttons for this menu:
              1. Video Tutorials
              2. Flashcards
              3. Game #1
              4. Game #2
              5. Game #3
            This is based on the Project Proposal, I'd like to change this
            before the final release.
         */
        private TextButton pauseButton;
        private TextButton backButton;

        private BitmapFont font;
        private BitmapFont font2;

        FreeTypeFontGenerator generator;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2;

        private Texture gunmaSprite;
        private Texture gunmaFaintedSprite;
        private Texture onionIdleSprite;
        private Texture background;

        private GlyphLayout displayWordLayout;
        private int targetWidth = 400;

        // Animation declarations
        private Animator onionWalkAnimation;
        private Animator gunmaWalkAnimation;

        private BackgroundDrawer backgroundDrawer;

        boolean isNotPaused = true;

        private GradeSystem gradeSystem;
        String cWords;
        String[] correctWordList;

        public GameScreen(Game game, ActionResolver speechGDX, DbInterface dbCallback, Screen previous) {

            this.game = game;
            this.speechGDX = speechGDX;
            this.dbCallback = dbCallback;
            this.previousScreen = previous;
        }

        @Override
        public void show() {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
            gameMusic.setLooping(false);
            gameMusic.setVolume(masterVolume);
            gameMusic.play();


            Gdx.gl.glClearColor(.8f, 1, 1, 1);
            stage = new Stage();

            batch = new SpriteBatch();
            gunmaSprite = new Texture("sprite_gunma.png");
            this.gunmaFaintedSprite = new Texture("gunma_fainted.png");
            //onionIdleSprite = new Texture("")

            background = new Texture("BG_temp.png");
            backgroundDrawer = new BackgroundDrawer(this.batch);

            // Animation initializations
            this.onionWalkAnimation = new Animator("onion_sheet.png", 4, 2, 0.1f);
            this.gunmaWalkAnimation = new Animator("gunma_sheet.png", 8, 1, 0.1f);

            // Spawning variables
            this.enemyPosition = Gdx.graphics.getWidth();
            this.lives = 3;
            this.isGameOver = false;

            Gdx.input.setInputProcessor(stage);

            // Defining the regions of sprite image we're going to create
            //atlas = new TextureAtlas("ui/button.pack"); // ???
            //skin = new Skin(atlas);

            table = new Table();
            table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            //font file
            final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
            generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

            //font for vocab word
            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            //font for other words
            parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

            //db list of vocab words
            dbListWords = dbCallback.getDbVocab();
            displayWord = dbListWords.get(listCounter).getEngSpelling();

            //spliced correct words for grading
            cWords = dbListWords.get(listCounter).getCorrectWords();
            correctWordList = cWords.split("\\s*,\\s*");

            //setting font values
            parameter.characters = displayWord;
            parameter.size = 70;
            parameter.color = Color.BLACK;
            font = generator.generateFont(parameter);
            parameter2.size = 35;
            parameter2.color = Color.BLACK;
            font2 = generator.generateFont(parameter2);

            //Alignment and Text Wrapping for Vocab Word
            displayWordLayout = new GlyphLayout();
            displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            //textButtonStyle.up = skin.getDrawable("button.up");
            //textButtonStyle.down = skin.getDrawable("button.down");
            textButtonStyle.pressedOffsetX = 1;
            textButtonStyle.pressedOffsetY = -1;
            textButtonStyle.font = font2;
            textButtonStyle.fontColor = Color.BLACK;

            backButton = new TextButton("Back", textButtonStyle);
            backButton.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 50);

            Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

            pauseButton = new TextButton("Pause", textButtonStyle);
            pauseButton.setPosition(25, 200);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */


            pauseButton.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(isNotPaused) {
                        gameMusic.pause();
                        isNotPaused = false;
                    }
                    else {
                        gameMusic.setVolume(masterVolume);
                        gameMusic.play();
                        isNotPaused = true;
                    }
                }
            });

            backButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    speechGDX.stopRecognition();
                    gameMusic.pause();
                    isNotPaused = false;
                    dispose(); // dispose of current GameScreen
                    game.setScreen(previousScreen);
                }
            });

            // Remove this later
            table.debug();

            stage.addActor(pauseButton);
            stage.addActor(backButton);


            //Start Speech Recognition
            try {
                speechGDX.startRecognition();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // SpriteBatch is resource intensive, try to use it for only brief moments
            batch.begin();
            backgroundDrawer.render(this.isNotPaused, this.isGameOver);
            //batch.draw(background, 0, 0);

            if (!isGameOver) {

               /* try {
                    if(!restartSpeech) {
                        speechGDX.startRecognition();
                        restartSpeech = true;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }*/

                font.draw(batch, displayWordLayout, 325, 425);

                if (score >= 0){
                    font2.draw(batch, "Score: " + score, 850, 450);
                }

                font2.draw(batch, "Lives: " + lives, 25, 450);

                //incomingWord = speechGDX.getWord();

                //need to parse out correct words separating by comma and check
                //with all correct words then use grading functionality


                //Returns false if word is null(no word has been said), or if word is incorrect
                if(gradeSystem.grade(correctWordList, speechGDX.getWord())){
                    listCounter++;
                    displayWord = dbListWords.get(listCounter).getEngSpelling();
                    parameter.characters = displayWord;
                    parameter.size = 70;
                    parameter.color = Color.BLACK;
                    font = generator.generateFont(parameter);
                    displayWordLayout.setText(font, displayWord, Color.BLACK, targetWidth, Align.center, true);
                    score = score + 1;
                    lives = lives + 1;

                    //spliced correct words for grading
                    cWords = dbListWords.get(listCounter).getCorrectWords();
                    correctWordList = cWords.split("\\s*,\\s*");
                }

                if (this.isNotPaused) {
                    batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 90, 35);
                } else {
                    batch.draw(this.gunmaWalkAnimation.getCurrentFrame(0), 90, 35);
                }
                this.walkOntoScreenFromRight(delta);
            } else {
                speechGDX.stopRecognition();
                font2.draw(batch, "Game Over", 450, 380);
                batch.draw(this.gunmaFaintedSprite, 70, 10);
            }

            batch.end();

            stage.act(delta); // optional to pass delta value
            stage.draw();
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

        @Override
        public void dispose() {
            font.dispose();
            background.dispose();
            this.backgroundDrawer.dispose();
            this.onionWalkAnimation.dispose();
            this.gunmaWalkAnimation.dispose();
            batch.dispose();
            stage.dispose();
            gameMusic.stop();
            gameMusic.dispose();
        }

        private void walkOntoScreenFromRight(float delta) {
            if (isNotPaused) {
                // This is a temporary fix. There's a more elegant solution that's less intensive I believe.
                TextureRegion tmp = onionWalkAnimation.getCurrentFrame(delta);
                tmp.flip(true, false);
                batch.draw(tmp, this.enemyPosition, 40);
                tmp.flip(true, false);
                this.enemyPosition -= 2;
                if (this.enemyPosition < 100) {
                    this.takeDamage();
                }
            } else {
                TextureRegion tmp = onionWalkAnimation.getCurrentFrame(0);
                tmp.flip(true, false);
                batch.draw(tmp, this.enemyPosition, 40);
                tmp.flip(true, false);
            }
        }

        private void takeDamage() {
            this.enemyPosition = Gdx.graphics.getWidth();
            this.lives--;

            if (this.lives == 0) {
                this.isGameOver = true;
            }
        }

        private void defeatEnemy() {
            this.enemyPosition = Gdx.graphics.getWidth();
            // However you want to change the current vocab would go here
        }
}
