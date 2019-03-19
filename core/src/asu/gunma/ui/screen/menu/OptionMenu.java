package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.Array;
import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.speech.ActionResolver;

public class OptionMenu implements Screen {

    private Game game;
    private Music gameMusic;
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

    //true = instructor, false = student
    private boolean verified = true;
    private boolean active1 = false, active2 = false, active3 = false, active4 = false, active5 = false,
            active6 = false, active7 = false, active8 = false, active9 = false, active10 = false,
            active11 = false, active12 = false, active13 = false, active14 = false, active15 = false;
    public ArrayList<Boolean> activeList;
    public ArrayList<VocabWord> activeVocabList;
    //temp bool until login system works
    private boolean login = false;

    private TextButton buttonCustom1, buttonCustom2, buttonCustom3, buttonCustom4, buttonCustom5, buttonCustom6,
            buttonCustom7, buttonCustom8, buttonCustom9, buttonCustom10, buttonCustom11, buttonCustom12,
            buttonCustom13, buttonCustom14, buttonCustom15;

    private TextButton deleteCustomButton1, deleteCustomButton2, deleteCustomButton3, deleteCustomButton4,
            deleteCustomButton5, deleteCustomButton6, deleteCustomButton7, deleteCustomButton8, deleteCustomButton9,
            deleteCustomButton10, deleteCustomButton11, deleteCustomButton12, deleteCustomButton13, deleteCustomButton14,
            deleteCustomButton15;

    private TextButton newButton, deleteButton, settingsButton, backButton;
    private ArrayList<TextButton> buttonList;
    private ArrayList<TextButton> deleteButtonList;
    private BitmapFont font;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private SpriteBatch batch;
    private Texture texture;

    private Label alphabetHeading;
    private Label colorHeading;
    private Label custom1Heading, custom2Heading, custom3Heading, custom4Heading, custom5Heading,
            custom6Heading, custom7Heading, custom8Heading, custom9Heading, custom10Heading;

    private ScrollPane fileSelectionPane;
    private Table fileTable;

    public OptionMenu(Game game, ActionResolver speechGDX, DbInterface dbInterface, Screen previousScreen, Music music, ArrayList<VocabWord> arrayList) {
        this.game = game;
        this.speechGDX = speechGDX;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        this.gameMusic = music;
        this.activeVocabList = arrayList;
        gameMusic.play();
    }

    public OptionMenu(Game game, ActionResolver speechGDX, DbInterface dbInterface, Music music) {
        this.game = game;
        this.speechGDX = speechGDX;
        this.dbInterface = dbInterface;
        this.gameMusic = music;
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

        table = new Table();
        table2 = new Table();
        table3 = new Table();
        table4 = new Table();
        table5 = new Table();
        table6 = new Table();

        table.setPosition(155, 275);
        table2.setPosition(415, 275);
        table3.setPosition(680, 275);
        table4.setPosition(285, 260);
        table5.setPosition(550, 260);
        table6.setPosition(805, 260);

        font = new BitmapFont(Gdx.files.internal("font-export.fnt")); // needs a font file still
        font.setColor(Color.BLACK); // Does nothing at the moment
        font.getData().setScale(2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //font file
        final String FONT_PATH = "irohamaru-mikami-Regular.ttf";
        generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));

        //font for vocab word
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 15;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        testSkin.getFont("font-big").getData().setScale(0.8f, 0.8f);

        // IMPORTANT: needs localization support
        // Make image buttons
        //use drawable to set image
        buttonCustom1 = new TextButton("Alphabet", testSkin, "small");
        buttonCustom2 = new TextButton("Colors-Shapes", testSkin, "small");
        buttonCustom3 = new TextButton("Countries", testSkin, "small");
        buttonCustom4 = new TextButton("Days-Months", testSkin, "small");
        buttonCustom5 = new TextButton("Feelings", testSkin, "small");
        buttonCustom6 = new TextButton("Fruits-Foods", testSkin, "small");
        buttonCustom7 = new TextButton("Numbers", testSkin, "small");
        buttonCustom8 = new TextButton("Places", testSkin, "small");
        buttonCustom9 = new TextButton("Professions", testSkin, "small");
        buttonCustom10 = new TextButton("Subjects", testSkin, "small");
        buttonCustom11 = new TextButton("Time", testSkin, "small");
        buttonCustom12 = new TextButton("x", testSkin, "small");
        buttonCustom13 = new TextButton("x", testSkin, "small");
        buttonCustom14 = new TextButton("x", testSkin, "small");
        buttonCustom15 = new TextButton("x", testSkin, "small");

        deleteCustomButton1 = new TextButton("X", testSkin, "default");
        deleteCustomButton2 = new TextButton("X", testSkin, "default");
        deleteCustomButton3 = new TextButton("X", testSkin, "default");
        deleteCustomButton4 = new TextButton("X", testSkin, "default");
        deleteCustomButton5 = new TextButton("X", testSkin, "default");
        deleteCustomButton6 = new TextButton("X", testSkin, "default");
        deleteCustomButton7 = new TextButton("X", testSkin, "default");
        deleteCustomButton8 = new TextButton("X", testSkin, "default");
        deleteCustomButton9 = new TextButton("X", testSkin, "default");
        deleteCustomButton10 = new TextButton("X", testSkin, "default");
        deleteCustomButton11 = new TextButton("X", testSkin, "default");
        deleteCustomButton12 = new TextButton("X", testSkin, "default");
        deleteCustomButton13 = new TextButton("X", testSkin, "default");
        deleteCustomButton14 = new TextButton("X", testSkin, "default");
        deleteCustomButton15 = new TextButton("X", testSkin, "default");

        buttonList = new ArrayList<>();
        buttonList.add(buttonCustom1);
        buttonList.add(buttonCustom2);
        buttonList.add(buttonCustom3);
        buttonList.add(buttonCustom4);
        buttonList.add(buttonCustom5);
        buttonList.add(buttonCustom6);
        buttonList.add(buttonCustom7);
        buttonList.add(buttonCustom8);
        buttonList.add(buttonCustom9);
        buttonList.add(buttonCustom10);
        buttonList.add(buttonCustom11);
        buttonList.add(buttonCustom12);
        buttonList.add(buttonCustom13);
        buttonList.add(buttonCustom14);
        buttonList.add(buttonCustom15);

        deleteButtonList = new ArrayList<>();
        deleteButtonList.add(deleteCustomButton1);
        deleteButtonList.add(deleteCustomButton2);
        deleteButtonList.add(deleteCustomButton3);
        deleteButtonList.add(deleteCustomButton4);
        deleteButtonList.add(deleteCustomButton5);
        deleteButtonList.add(deleteCustomButton6);
        deleteButtonList.add(deleteCustomButton7);
        deleteButtonList.add(deleteCustomButton8);
        deleteButtonList.add(deleteCustomButton9);
        deleteButtonList.add(deleteCustomButton10);
        deleteButtonList.add(deleteCustomButton11);
        deleteButtonList.add(deleteCustomButton12);
        deleteButtonList.add(deleteCustomButton13);
        deleteButtonList.add(deleteCustomButton14);
        deleteButtonList.add(deleteCustomButton15);

        for (TextButton t : buttonList) {
            if (t.getText().toString().equals("x")) {
                t.setVisible(false);
                t.setDisabled(true);
            }
        }

        for (TextButton t : deleteButtonList) {
            t.setVisible(false);
            t.setDisabled(true);
        }

        activeList = new ArrayList<>();

        backButton = new TextButton("Back", textButtonStyle);
        backButton.setPosition(20, 530, Align.left);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);
/*
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
*/
        /*
            If you want to test functions with UI instead of with console,
            add it into one of these Listeners. Each of them correspond to
            one of the buttons on the screen in top-down order.
         */
        List<VocabWord> dbVocab = dbInterface.getDbVocab();
        buttonCustom1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active1) {
                        System.out.println("Hit2");
                        buttonCustom1.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deactivateModule(buttonCustom1, 0);
                        active1 = false;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(0).getText().toString())){
                                activeVocabList.remove(v);
                            }
                        }
                    } else {
                        buttonCustom1.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        activateModule(buttonCustom1, 0);
                        active1 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(0).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active2) {
                        System.out.println("Hit2");
                        buttonCustom2.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active2 = false;
                    } else {
                        buttonCustom2.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active2 = true;
                    }
                }
            }
        });

        buttonCustom3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active3) {
                        System.out.println("Hit2");
                        buttonCustom3.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active3 = false;
                    } else {
                        buttonCustom3.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active3 = true;
                    }
                }
            }
        });

        buttonCustom4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active4) {
                        System.out.println("Hit2");
                        buttonCustom4.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active4 = false;
                    } else {
                        buttonCustom4.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active4 = true;
                    }
                }
            }
        });

        buttonCustom5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active5) {
                        System.out.println("Hit2");
                        buttonCustom5.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active5 = false;
                    } else {
                        buttonCustom5.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active5 = true;
                    }
                }
            }
        });

        buttonCustom6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active6) {
                        System.out.println("Hit2");
                        buttonCustom6.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active6 = false;
                    } else {
                        buttonCustom6.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active6 = true;
                    }
                }
            }
        });

        buttonCustom7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active7) {
                        System.out.println("Hit2");
                        buttonCustom7.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active7 = false;
                    } else {
                        buttonCustom7.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active7 = true;
                    }
                }
            }
        });

        buttonCustom8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active8) {
                        System.out.println("Hit2");
                        buttonCustom8.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active8 = false;
                    } else {
                        buttonCustom8.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active8 = true;
                    }
                }
            }
        });

        buttonCustom9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active9) {
                        System.out.println("Hit2");
                        buttonCustom9.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active9 = false;
                    } else {
                        buttonCustom9.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active9 = true;
                    }
                }
            }
        });

        buttonCustom10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active10) {
                        System.out.println("Hit2");
                        buttonCustom10.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active10 = false;
                    } else {
                        buttonCustom10.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active10 = true;
                    }
                }
            }
        });

        buttonCustom11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active11) {
                        System.out.println("Hit2");
                        buttonCustom11.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active11 = false;
                    } else {
                        buttonCustom11.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active11 = true;
                    }
                }
            }
        });

        buttonCustom12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active12) {
                        System.out.println("Hit2");
                        buttonCustom12.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active12 = false;
                    } else {
                        buttonCustom12.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active12 = true;
                    }
                }
            }
        });

        buttonCustom13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active13) {
                        System.out.println("Hit2");
                        buttonCustom13.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active13 = false;
                    } else {
                        buttonCustom13.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active13 = true;
                    }
                }
            }
        });

        buttonCustom14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active14) {
                        System.out.println("Hit2");
                        buttonCustom14.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active14 = false;
                    } else {
                        buttonCustom14.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active14 = true;
                    }
                }
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

        buttonCustom15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active15) {
                        System.out.println("Hit2");
                        buttonCustom15.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active15 = false;
                    } else {
                        buttonCustom15.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active15 = true;
                    }
                }
            }
        });

        deleteCustomButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Gdx.files.local("/data/data/com.gunmachan.game/files/" + buttonCustom1.getText().toString()).file().exists()) {
                    Gdx.files.local("/data/data/com.gunmachan.game/files/" + buttonCustom1.getText().toString()).delete();
                    for (int i = 0; i < buttonList.size() - 1; i++) {
                        buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                    }
                    //delete out of database
                    //loop through table looking for tuples that match module name
                    //if match
                    //  dbDeleteVocab
                    for (int i = 0; i < buttonList.size(); i++) {
                        if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                            buttonList.get(i).setVisible(false);
                            buttonList.get(i).setDisabled(true);
                            deleteButtonList.get(i).setVisible(false);
                            deleteButtonList.get(i).setDisabled(true);
                        }
                    }
                    for (TextButton t : deleteButtonList) {
                        t.setVisible(false);
                        t.setDisabled(true);
                    }
                } else {
                    //make label to display this
                    System.out.println("Error file does not exist");
                }
            }
        });
        deleteCustomButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Gdx.files.internal(buttonList.get(1).getText().toString() + ".csv").delete();
                /*if(Gdx.files.internal("ColorsShapes.csv").exists()){
                    System.out.println("ColorsShapes.csv");
                }*/
                for (int i = 1; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 2; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 3; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 4; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 5; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 6; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 7; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 8; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 9; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 10; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 11; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 12; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 13; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonList.get(buttonList.size() - 1).setText("x");
                buttonList.get(buttonList.size() - 1).setVisible(false);
                buttonList.get(buttonList.size() - 1).setDisabled(true);
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });

        newButton = new TextButton("Add", testSkin, "default");
        deleteButton = new TextButton("Delete", testSkin, "default");
        settingsButton = new TextButton("Settings", testSkin, "default");
        backButton = new TextButton("Back", testSkin, "default");
        newButton.setTransform(true);
        newButton.setScale(0.5f);
        deleteButton.setTransform(true);
        deleteButton.setScale(0.5f);
        settingsButton.setTransform(true);
        settingsButton.setScale(0.5f);
        backButton.setTransform(true);
        backButton.setScale(0.5f);

        newButton.setPosition(850, 250);
        deleteButton.setPosition(850, 175);
        settingsButton.setPosition(850, 100);
        newButton.getLabel().setAlignment(Align.center);
        deleteButton.getLabel().setAlignment(Align.center);
        settingsButton.getLabel().setAlignment(Align.center);

        backButton.setPosition(0, 540);
        backButton.getLabel().setAlignment(Align.center);


        //log out button or use google button?

        newButton.setVisible(verified);
        deleteButton.setVisible(verified);
        settingsButton.setVisible(verified);

        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //BEST OPTION IS TO CHANGE THE TABLE TO A SCROLLPANE
                ArrayList<File> fileList = speechGDX.googleDriveAccess();
                fileTable = new Table(testSkin);
                Dialog fileDialog = new Dialog("Select File To Upload", testSkin);
                fileDialog.setPosition(400, 300, Align.center);
                fileDialog.setMovable(false);
                fileDialog.getTitleLabel().setAlignment(Align.center);
                TextButton cancelButton = new TextButton("Cancel", testSkin, "small");
                for (int i = 0; i < fileList.size(); i++) {
                    File currentFile = fileList.get(i);
                    if (currentFile.exists()) {
                        String filename = currentFile.getName();
                        String filenameSplit = filename.split("\\.")[0];
                        System.out.println(filenameSplit);
                        Label fileNameLabel = new Label(filenameSplit + "         ", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("font-export.fnt")), Color.BLACK));
                        Label fileExtensionLabel = new Label("csv" + "        ", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("font-export.fnt")), Color.BLACK));
                        TextButton temp = new TextButton("SELECT", testSkin, "small");
                        temp.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                System.out.println("I HAVE SELECTED " + filenameSplit);
                                //importCSV(currentFile) to dbInterface, call it on currentCSVFILE if not null
                                dbInterface.importCSVFile(currentFile.getName());
                                currentFile.delete();
                                fileDialog.hide();
                                for (int i = 0; i < buttonList.size(); i++) {
                                    if (buttonList.get(i).getText().toString().equals("x")) {
                                        buttonList.get(i).setText(filenameSplit);
                                        buttonList.get(i).setVisible(true);
                                        buttonList.get(i).setDisabled(false);
                                        if (table.getRows() < 5) {
                                            table.addActor(buttonList.get(i));
                                        } else if (table2.getRows() < 5) {
                                            table2.addActor(buttonList.get(i));
                                        } else if (table3.getRows() < 5) {
                                            table3.addActor(buttonList.get(i));
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                        fileTable.add(fileNameLabel).center();
                        fileTable.add(fileExtensionLabel).center();
                        fileTable.add("");
                        fileTable.add(temp).setActorHeight(0.6f);
                        fileTable.row();

                    } else {
                        System.out.println("ERROR FILE DOES NOT EXIST!");
                    }
                }

                cancelButton.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        fileDialog.hide();
                    }
                });
                
                //fileTable.debug();
                fileDialog.getContentTable().add(fileTable).top().fill();
                fileDialog.add(cancelButton).bottom().right().setActorHeight(0.6f);
                fileDialog.show(stage);
                //Google Drive API
            }
        });

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //change this to if button in button list selected
                //need to make it do something if the button is held down
                for (int i = 0; i < deleteButtonList.size(); i++) {
                    if (!buttonList.get(i).getText().toString().equals("x")) {
                        deleteButtonList.get(i).setVisible(true);
                        deleteButtonList.get(i).setDisabled(false);
                    }
                }

                //Delete csv file in assets folder with same name
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going from OptionsScreen to SettingsScreen");
                // need option to enable navigation bar
                if (verified) {
                    gameMusic.pause();
                    game.setScreen(new SettingsScreen(game, speechGDX, dbInterface, game.getScreen(), gameMusic));
                }
            }
        });
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                previousScreen.dispose();
                gameMusic.pause();
                game.setScreen(new MainMenuScreen(game, speechGDX, dbInterface, gameMusic, activeVocabList));
                dispose(); // dispose of current GameScreen
            }
        });

        int count = 0;
        for (TextButton t : buttonList) {
            if (count < 5) {
                table.add(t).padTop(15).padBottom(30);
                table.row();
            } else if (count < 10 && count > 4) {
                table2.add(t).padTop(15).padBottom(30);
                table2.row();
            } else if (count > 9 && count < 15) {
                table3.add(t).padTop(15).padBottom(30);
                table3.row();
            }
            count++;
        }

        count = 0;
        for (TextButton t : deleteButtonList) {
            if (count < 5) {
                table4.add(t).padBottom(85);
                table4.row();
            } else if (count < 10 && count > 4) {
                table5.add(t).padBottom(85);
                table5.row();
            } else if (count > 9 && count < 15) {
                table6.add(t).padBottom(85);
                table6.row();
            }
            count++;
        }

        count = 0;
        /*for(boolean b : activeList){
            List<VocabWord> dbVocab = dbInterface.getDbVocab();
            if(b){

            }
            count++;
        }*/

        for(VocabWord v : activeVocabList){
            System.out.println(v.getEngSpelling());
        }
        //scale buttons not table!
        //create active booleans for custom button
        //
        table.setTransform(true);
        table.setScale(0.5f);
        table2.setTransform(true);
        table2.setScale(0.5f);
        table3.setTransform(true);
        table3.setScale(0.5f);
        table4.setTransform(true);
        table4.setScaleX(0.2f);
        table4.setScaleY(0.4f);
        table5.setTransform(true);
        table5.setScaleX(0.2f);
        table5.setScaleY(0.4f);
        table6.setTransform(true);
        table6.setScaleX(0.2f);
        table6.setScaleY(0.4f);


        //google login button listener
        //google login credentials is a teacher in instructor database
        /*if(login){
            verified = true;
        }
        else{
            verified = false;
        }*/

        // Remove this later
        /*table.debug();
        table2.debug();
        table3.debug();*/
        /*table4.debug();
        table5.debug();
        table6.debug();*/

        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);
        stage.addActor(table5);
        stage.addActor(table6);
        stage.addActor(newButton);
        stage.addActor(deleteButton);
        stage.addActor(settingsButton);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        /*if (speechGDX.getVerificationBool()) {
            verified = true;
        } else {
            verified = false;
        }*/
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 415,
                Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2,
                texture.getHeight()/2);
        activeList.add(active1);
        activeList.add(active2);
        activeList.add(active3);
        activeList.add(active4);
        activeList.add(active5);
        activeList.add(active6);
        activeList.add(active7);
        activeList.add(active8);
        activeList.add(active8);
        activeList.add(active9);
        activeList.add(active10);
        activeList.add(active11);
        activeList.add(active12);
        activeList.add(active13);
        activeList.add(active14);
        activeList.add(active15);
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
        testSkin.dispose();
        assetManager.dispose();
        texture.dispose();
        batch.dispose();
        stage.dispose();
    }

    public void activateModule(TextButton button, int position) {
        //use to activate the lists in the database
        if (buttonList.get(position).getStyle() == testSkin.get("default", TextButton.TextButtonStyle.class)) {
            String s = buttonList.get(position).getText().toString();
            //activate based on module info in database
        }
    }

    public void deactivateModule(TextButton button, int position) {
        if (buttonList.get(position).getStyle() == testSkin.get("default", TextButton.TextButtonStyle.class)) {
            String s = buttonList.get(position).getText().toString();
            //deactivate based on module info in database
        }
    }

}
