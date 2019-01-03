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

public class TitleScreen implements Screen {

    // This is needed to change screens later on
    private Game game;
    public ActionResolver speechGDX;

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
    private TextButton buttonTutorial, buttonFlashcard, buttonGameFirst, buttonGameSecond, buttonGameThird;

    private SpriteBatch batch;
    private Texture texture;

    private BitmapFont font;
    private Label heading;

    public TitleScreen(Game game, ActionResolver speechGDX) {

        this.game = game;
        this.speechGDX = speechGDX;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        batch = new SpriteBatch();
        texture = new Texture("title_gunma.png");

        Gdx.input.setInputProcessor(stage);

        // Defining the regions of sprite image we're going to create
        //atlas = new TextureAtlas("ui/button.pack"); // ???
        //skin = new Skin(atlas);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont(); // needs a font file still
        font.setColor(Color.BLACK); // Does nothing at the moment
        font.getData().setScale(2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        //textButtonStyle.up = skin.getDrawable("button.up");
        //textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        // IMPORTANT: needs localization support
        buttonTutorial = new TextButton("Play", textButtonStyle);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);
        //

        heading = new Label("Gunma-chan Takes a Hike", headingStyle);
        heading.setFontScale(3);
        //

        // Actually, should probably custom class this process
        buttonTutorial.pad(20);

        /*
            If you want to test functions with UI instead of with console,
            add it into one of these Listeners. Each of them correspond to
            one of the buttons on the screen in top-down order.
         */
        buttonTutorial.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going from TitleScreen to MainMenuScreen");
                game.setScreen(new MainMenuScreen(game, speechGDX));
            }
        });

        table.add(heading);
        table.row();
        table.add(buttonTutorial);
        table.row();

        // Remove this later
        //table.debug();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta); // optional to pass delta value
        stage.draw();

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 400, Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2, texture.getHeight()/2);
        batch.end();
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
