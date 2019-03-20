package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.speech.ActionResolver;

public class SettingsScreen implements Screen {

    private Game game;
    private Music gameMusic;
    public static float masterVolume = 5;
    private ActionResolver speechGDX;
    private DbInterface dbInterface;
    private AssetManager assetManager;
    private Screen previousScreen;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion region;
    private Skin testSkin;
    private Table table, table2, table3, table4, table5, table6;

    private TextButton homeScreenLockButton, googleLoginButton,backButton, googleLogoutButton;

    private SpriteBatch batch;
    private Texture texture;

    private BitmapFont font;

    private boolean homeLock = false;
    private String buttonText = "Home key unlocked";
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private FreeTypeFontGenerator generator;
    private String googleLoginMessage = "";
    private String googleLogoutMessage = "";
    private boolean signedIn = false;
    public Preferences prefs;

    public SettingsScreen(Game game, ActionResolver speechGDX, Music music, DbInterface dbInterface, Screen previousScreen, Preferences prefs){
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        this.gameMusic = music;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();
        batch = new SpriteBatch();
        texture = new Texture("title_gunma.png");

        Gdx.input.setInputProcessor(stage);
        assetManager = new AssetManager();
        testSkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        font = new BitmapFont(Gdx.files.internal("font-export.fnt")); // needs a font file still
        font.setColor(Color.BLACK); // Does nothing at the moment
        font.getData().setScale(2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        testSkin.getFont("font-big").getData().setScale(0.8f,0.8f);

        homeScreenLockButton = new TextButton(buttonText, testSkin, "default");
        homeScreenLockButton.setTransform(true);
        homeScreenLockButton.setScale(0.5f);
        homeScreenLockButton.setPosition(50, 400);
        homeScreenLockButton.getLabel().setAlignment(Align.center);

        googleLoginButton = new TextButton("Google Login", testSkin, "default");
        googleLoginButton.setTransform(true);
        googleLoginButton.setScale(0.5f);
        googleLoginButton.setPosition(50, 300);
        googleLoginButton.getLabel().setAlignment(Align.center);

        googleLogoutButton = new TextButton("Google Logout", testSkin, "default");
        googleLogoutButton.setTransform(true);
        googleLogoutButton.setScale(0.5f);
        googleLogoutButton.setPosition(50, 200);
        googleLogoutButton.getLabel().setAlignment(Align.center);

        //font file
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        //setting font values
        parameter.size = 70;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        //Theme button
        //google login/logout

        backButton = new TextButton("Back", testSkin, "default");
        backButton.setTransform(true);
        backButton.setScale(0.5f);
        backButton.setPosition(0, 540);
        backButton.getLabel().setAlignment(Align.center);

        homeScreenLockButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        googleLoginButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                speechGDX.signIn();
            }
        });

        googleLogoutButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                speechGDX.signOut();
            }
        });

        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                googleLoginMessage = "";
                gameMusic.pause();
                gameMusic.dispose();
                game.setScreen(new OptionMenu(game, speechGDX, gameMusic, dbInterface, previousScreen, prefs));
                dispose(); // dispose of current GameScreen
            }
        });

        stage.addActor(homeScreenLockButton);
        stage.addActor(backButton);
        stage.addActor(googleLoginButton);
        stage.addActor(googleLogoutButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 415, Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2, texture.getHeight()/2);
        font.draw(batch, googleLoginMessage, 350, 200);
        batch.end();

        stage.act(delta); // optional to pass delta value
        stage.draw();
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

    @Override
    public void dispose() {
        font.dispose();
        testSkin.dispose();
        assetManager.dispose();
        texture.dispose();
        batch.dispose();
        stage.dispose();
    }
}
