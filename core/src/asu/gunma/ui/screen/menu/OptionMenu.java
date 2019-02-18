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
import asu.gunma.speech.SpeechInputHandler;

public class OptionMenu implements Screen {

    private Game game;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;

    private int testInt = 0;

    private TextButton buttonAlphabet, buttonColor, buttonCustom1,buttonCustom2,buttonCustom3,buttonCustom4,buttonCustom5,buttonCustom6,buttonCustom7,buttonCustom8,buttonCustom9,buttonCustom10;

    private SpriteBatch batch;
    private Texture texture;

    private BitmapFont font;
    private Label alphabetHeading;
    private Label colorHeading;
    private Label custom1Heading, custom2Heading,custom3Heading,custom4Heading,custom5Heading,custom6Heading,custom7Heading,custom8Heading,custom9Heading,custom10Heading;


    public OptionMenu(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        batch = new SpriteBatch();
        texture = new Texture("title_gunma.png");

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font = new BitmapFont(); // needs a font file still
        font.setColor(Color.BLACK); // Does nothing at the moment
        font.getData().setScale(2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        // IMPORTANT: needs localization support
        buttonAlphabet = new TextButton("Alphabet", textButtonStyle);
        buttonColor = new TextButton("Colors", textButtonStyle);
        buttonCustom1 = new TextButton("Custom 1", textButtonStyle);
        buttonCustom2 = new TextButton("Custom 2", textButtonStyle);
        buttonCustom3 = new TextButton("Custom 3", textButtonStyle);
        buttonCustom4 = new TextButton("Custom 4", textButtonStyle);
        buttonCustom5 = new TextButton("Custom 5", textButtonStyle);
        buttonCustom6 = new TextButton("Custom 6", textButtonStyle);
        buttonCustom7 = new TextButton("Custom 7", textButtonStyle);
        buttonCustom8 = new TextButton("Custom 8", textButtonStyle);
        buttonCustom9 = new TextButton("Custom 9", textButtonStyle);
        buttonCustom10 = new TextButton("Custom 10", textButtonStyle);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

        alphabetHeading = new Label("Alphabet", headingStyle);
        alphabetHeading.setFontScale(2);
        colorHeading = new Label("Colors", headingStyle);
        colorHeading.setFontScale(2);
        custom1Heading = new Label("Custom Set 1", headingStyle);
        custom1Heading.setFontScale(2);
        custom2Heading = new Label("Custom Set 2", headingStyle);
        custom2Heading.setFontScale(2);
        custom3Heading = new Label("Custom Set 3", headingStyle);
        custom3Heading.setFontScale(2);
        custom4Heading = new Label("Custom Set 4", headingStyle);
        custom4Heading.setFontScale(2);
        custom5Heading = new Label("Custom Set 5", headingStyle);
        custom5Heading.setFontScale(2);
        custom6Heading = new Label("Custom Set 6", headingStyle);
        custom6Heading.setFontScale(2);
        custom7Heading = new Label("Custom Set 7", headingStyle);
        custom7Heading.setFontScale(2);
        custom8Heading = new Label("Custom Set 8", headingStyle);
        custom8Heading.setFontScale(2);
        custom9Heading = new Label("Custom Set 9", headingStyle);
        custom9Heading.setFontScale(2);
        custom10Heading = new Label("Custom Set 10", headingStyle);
        custom10Heading.setFontScale(2);

        // Actually, should probably custom class this process
        buttonAlphabet.pad(20);
        buttonColor.pad(20);
        buttonCustom1.pad(20);
        buttonCustom2.pad(20);
        buttonCustom3.pad(20);
        buttonCustom4.pad(20);
        buttonCustom5.pad(20);
        buttonCustom6.pad(20);
        buttonCustom7.pad(20);
        buttonCustom8.pad(20);
        buttonCustom9.pad(20);
        buttonCustom10.pad(20);

        /*
            If you want to test functions with UI instead of with console,
            add it into one of these Listeners. Each of them correspond to
            one of the buttons on the screen in top-down order.
         */

        buttonAlphabet.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        buttonColor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        buttonCustom1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        buttonCustom10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        table.add(alphabetHeading);
        table.row();
        table.add(colorHeading);
        table.row();
        table.add(custom1Heading);
        table.row();
        table.add(custom2Heading);
        table.row();
        table.add(custom3Heading);
        table.row();
        table.add(custom4Heading);
        table.row();
        table.add(custom5Heading);
        table.row();
        table.add(custom6Heading);
        table.row();
        table.add(custom7Heading);
        table.row();
        table.add(custom8Heading);
        table.row();
        table.add(custom9Heading);
        table.row();
        table.add(custom10Heading);
        table.row();


        // Remove this later
        table.debug();

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 400, Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2, texture.getHeight()/2);
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
