package asu.gunma.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import asu.gunma.speech.ActionResolver;


public class GameScreen implements Screen {

    private Game game;
    public ActionResolver speechGDX;

    // Game logic variables
    private int score;
    private String word;
    private boolean recordState;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;

    private int testInt = 0;

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

    private BitmapFont font;
    private Label heading;

    private SpriteBatch batch;
    private Texture gunmaSprite;
    private Texture frenemySprite;
    private Texture background;


    public GameScreen(Game game, ActionResolver speechGDX) {

        this.speechGDX = speechGDX;
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        batch = new SpriteBatch();
        gunmaSprite = new Texture("sprite_gunma.png");
        frenemySprite = new Texture("sprite_frenemy1.png");
        background = new Texture("BG_temp.png");

        font = new BitmapFont();

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

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

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

        stage.addActor(buttonRecord);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(gunmaSprite, 90, 60, gunmaSprite.getWidth()*3, gunmaSprite.getHeight()*3);
        //batch.draw(frenemySprite, 730, 60, frenemySprite.getWidth()/11, frenemySprite.getHeight()/11);

        font.draw(batch, "Word: " + speechGDX.getWord(), 400, 380);
        font.draw(batch, "Score: " + score, 0, 595);
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

    }

}
