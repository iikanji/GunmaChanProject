package asu.gunma.ui.screen.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.audio.Music;

import asu.gunma.DatabaseInterface.*;

import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.menu.MainMenuScreen;
import asu.gunma.ui.util.Animator;

public class GameScreen implements Screen {
        DbCallback dbCallback = new DbCallback();
        private Game game;
        private Music gameMusic;
        public ActionResolver speechGDX;
        private Screen previousScreen;

        // Game logic variables
        private int score;
        private String word;
        private boolean recordState;

        // Using these are unnecessary but will make our lives easier.
        private Stage stage;
        private TextureAtlas atlas;
        private Skin skin;
        private Table table;

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
        private TextButton buttonRecord;
        private TextButton pauseButton;
        private TextButton backButton;

        private BitmapFont font;

        private SpriteBatch batch;
        private Texture gunmaSprite;
        private Texture gunmaFaintedSprite;
        private Texture onionIdleSprite;
        private Texture background;

        // Animation declarations
        private Animator onionWalkAnimation;
        private Animator gunmaWalkAnimation;

        boolean musicOnOff = true;

        public GameScreen(Game game, ActionResolver speechGDX, Screen previous) {

            this.speechGDX = speechGDX;
            this.game = game;
            this.previousScreen = previous;
        }

        @Override
        public void show() {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Naruto_Theme-The_Raising_Fighting_Spirit.mp3"));
            gameMusic.play();
            Gdx.gl.glClearColor(.8f, 1, 1, 1);
            stage = new Stage();

            batch = new SpriteBatch();
            gunmaSprite = new Texture("sprite_gunma.png");
            this.gunmaFaintedSprite = new Texture("gunma_fainted.png");
            //onionIdleSprite = new Texture("")

            background = new Texture("BG_temp.png");

            // Animation initializations
            this.onionWalkAnimation = new Animator("onion_sheet.png", 4, 2, 0.1f);
            this.gunmaWalkAnimation = new Animator("gunma_sheet.png", 8, 1, 0.1f);

            font = new BitmapFont();

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

            /*
            font = new BitmapFont(); // needs a font file still
            */
            font.setColor(Color.BLACK); // Does nothing at the moment
            font.getData().setScale(2);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            //textButtonStyle.up = skin.getDrawable("button.up");
            //textButtonStyle.down = skin.getDrawable("button.down");
            textButtonStyle.pressedOffsetX = 1;
            textButtonStyle.pressedOffsetY = -1;
            textButtonStyle.font = font;
            textButtonStyle.fontColor = Color.BLACK;

            // IMPORTANT: needs localization support
            buttonRecord = new TextButton("Microphone", textButtonStyle);
            //buttonRecord.setPosition(x, y);

            backButton = new TextButton("Back", textButtonStyle);
            backButton.setPosition(Gdx.graphics.getWidth()-70, Gdx.graphics.getHeight()-32);

            Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

            pauseButton = new TextButton("Pause", textButtonStyle);
            pauseButton.setPosition(0, 100);

            /*
                If you want to test functions with UI instead of with console,
                add it into one of these Listeners. Each of them correspond to
                one of the buttons on the screen in top-down order.
             */
            buttonRecord.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // I have it in reverse order like this because it makes more sense
                    // but I can't think of a good variable name for it to not be backwards
                    try {
                        speechGDX.startRecognition();

                        //add delay
                        /*Timer.schedule(new Timer.Task(){
                            @Override
                            public void run() {
                                word = speechGDX.getWord();
                            }
                        }, 5);*/

                    } catch(Exception e) {
                        System.out.println(e);
                    }

                }
            });

            // Remove this later
            table.debug();

            //inputWord = new VocabWord();
            //studentMetric = studentMetric();
            //if english word
            //  set inputWord.setEngSpelling = google word
            //if japanese word
            //  set inputWord.setJpnSpelling = google word
            //search for word.
            //  WRITE FUNCTION FOR SEARCHING WORDS IF ENG SPELLING = NULL
            //  OR IF JPN SPELLING = NULL


            pauseButton.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(musicOnOff) {
                        gameMusic.pause();
                        musicOnOff = false;
                    }
                    else if(!musicOnOff){
                        gameMusic.play();
                        musicOnOff = true;
                    }
                }
            });

            backButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    gameMusic.pause();
                    musicOnOff = false;
                    dispose(); // dispose of current GameScreen
                    previousScreen.dispose();
                    game.setScreen(new MainMenuScreen(game, speechGDX));
                }
            });

            stage.addActor(buttonRecord);
            stage.addActor(pauseButton);
            stage.addActor(backButton);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // SpriteBatch is resource intensive, try to use it for only brief moments
            batch.begin();
            batch.draw(background, 0, 0);



            if (!isGameOver) {
                font.draw(batch, "Word: " + speechGDX.getWord(), 400, 380);
                font.draw(batch, "Lives: " + this.lives, 0, 400);

                batch.draw(this.gunmaWalkAnimation.getCurrentFrame(delta), 90, 60);
                this.walkOntoScreenFromRight(delta);
            } else {
                font.draw(batch, "Game Over", 400, 380);
                batch.draw(this.gunmaFaintedSprite, 70, 40);
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
            this.onionWalkAnimation.dispose();
            this.gunmaWalkAnimation.dispose();
            batch.dispose();
            stage.dispose();
            gameMusic.dispose();
        }

        private void walkOntoScreenFromRight(float delta) {
            batch.draw(onionWalkAnimation.getCurrentFrame(delta), this.enemyPosition, 60);
            this.enemyPosition--;
            if (this.enemyPosition < 100) {
                this.takeDamage();
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