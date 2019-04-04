package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import java.util.Iterator;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.speech.ActionResolver;

public class OptionMenu implements Screen {

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

    //true = instructor, false = student
    private boolean verified = true;
    private boolean active1 = true, active2 = true, active3 = true, active4 = true, active5 = true,
            active6 = true, active7 = true, active8 = true, active9 = true, active10 = true,
            active11 = false, active12 = false, active13 = false, active14 = false, active15 = false;
    public ArrayList<VocabWord> activeVocabList = new ArrayList<>();
    //temp bool until login system works
    private boolean login = false;
    private boolean deleteButtonsVisible = false;
    private int activeLimit = 10;

    private TextButton buttonCustom1, buttonCustom2, buttonCustom3, buttonCustom4, buttonCustom5, buttonCustom6,
            buttonCustom7, buttonCustom8, buttonCustom9, buttonCustom10, buttonCustom11, buttonCustom12,
            buttonCustom13, buttonCustom14, buttonCustom15;

    private TextButton deleteCustomButton11, deleteCustomButton12, deleteCustomButton13, deleteCustomButton14,
            deleteCustomButton15;

    private TextButton newButton, deleteButton, settingsButton, backButton;
    private ArrayList<TextButton> buttonList;
    private ArrayList<TextButton> deleteButtonList;
    private BitmapFont font;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private SpriteBatch batch;
    private Texture texture;

    private ScrollPane fileSelectionPane;
    private Table fileTable;
    public Preferences prefs;
    File currentFile = null;

    public OptionMenu(Game game, ActionResolver speechGDX, Music music, DbInterface dbInterface, Screen previousScreen, ArrayList<VocabWord> arrayList, Preferences prefs) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        this.activeVocabList = arrayList;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    public OptionMenu(Game game, ActionResolver speechGDX, Music music, DbInterface dbInterface, Screen previousScreen, Preferences prefs) {
        this.game = game;
        this.prefs = prefs;
        this.speechGDX = speechGDX;
        this.gameMusic = music;
        this.dbInterface = dbInterface;
        this.previousScreen = previousScreen;
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
        gameMusic.setLooping(false);
        gameMusic.setVolume(masterVolume);
        gameMusic.play();
    }

    // need to delete words from database when a delete button is clicked
    // delete buttons in wrong location


    //CREATE COUNTER THAT CONTROLS IF THE MODULE IS THE LAST ONE ACTIVE
    //AND PREVENT IT FROM BEING DEACTIVATED IF TRUE
    @Override
    public void show() {
        int count = 0;
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
        table6.setPosition(805, 230);

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
        buttonCustom1 = new TextButton("Colors-Shapes", testSkin, "small");
        buttonCustom2 = new TextButton("Countries", testSkin, "small");
        buttonCustom3 = new TextButton("Days-Months", testSkin, "small");
        buttonCustom4 = new TextButton("Feelings", testSkin, "small");
        buttonCustom5 = new TextButton("Fruits-Foods", testSkin, "small");
        buttonCustom6 = new TextButton("Numbers", testSkin, "small");
        buttonCustom7 = new TextButton("Places", testSkin, "small");
        buttonCustom8 = new TextButton("Professions", testSkin, "small");
        buttonCustom9 = new TextButton("Subjects", testSkin, "small");
        buttonCustom10 = new TextButton("Time", testSkin, "small");
        buttonCustom11 = new TextButton("x", testSkin, "small");
        buttonCustom12 = new TextButton("x", testSkin, "small");
        buttonCustom13 = new TextButton("x", testSkin, "small");
        buttonCustom14 = new TextButton("x", testSkin, "small");
        buttonCustom15 = new TextButton("x", testSkin, "small");

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

        backButton = new TextButton("Back", textButtonStyle);
        backButton.setPosition(20, 530, Align.left);

        Label.LabelStyle headingStyle = new Label.LabelStyle(font, Color.BLACK);

        /*
            If you want to test functions with UI instead of with console,
            add it into one of these Listeners. Each of them correspond to
            one of the buttons on the screen in top-down order.
         */
        List<VocabWord> dbVocab = dbInterface.getDbVocab();
        buttonCustom1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("Hit");
                if (verified) {
                    if (active1 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active1", false).flush();
                        System.out.println("Active 1(true) is set to -> " + prefs.getBoolean("active1"));
                        //System.out.println("Hit2");
                        buttonCustom1.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deactivateModule(buttonCustom1, 0);
                        active1 = false;
                        System.out.println("*********REMOVING VOCAB********");
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(0).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active1){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active1", true).flush();
                        System.out.println("Active 1(false) is set to -> " + prefs.getBoolean("active1"));
                        buttonCustom1.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        activateModule(buttonCustom1, 0);
                        active1 = true;
                        for(int i = 0; i < dbVocab.size(); i++){
                            if(dbVocab.get(i).getModuleCategory().equals(buttonList.get(0).getText().toString())){
                                activeVocabList.add(dbVocab.get(i));
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
                    if (active2 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active2", false).flush();
                        buttonCustom2.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active2 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(1).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active2){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active2", true).flush();
                        buttonCustom2.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active2 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(1).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active3 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active3", false).flush();
                        buttonCustom3.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active3 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(2).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active3){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active3", true).flush();
                        buttonCustom3.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active3 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(2).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active4 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active4", false).flush();
                        buttonCustom4.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active4 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(3).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active4){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active4", true).flush();
                        buttonCustom4.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active4 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(3).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active5 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active5", false).flush();
                        buttonCustom5.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active5 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(4).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active5){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active5", true).flush();
                        buttonCustom5.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active5 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(4).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active6 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active6", false).flush();
                        buttonCustom6.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active6 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(5).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active6){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active6", true).flush();
                        buttonCustom6.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active6 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(5).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active7 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active7", false).flush();
                        buttonCustom7.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active7 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(6).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active7){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active8", true).flush();
                        buttonCustom7.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active7 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(6).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active8 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active8", false).flush();
                        buttonCustom8.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active8 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(7).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active8) {
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active9", true).flush();
                        buttonCustom8.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active8 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(7).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active9 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active9", false).flush();
                        buttonCustom9.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active9 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(8).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active9){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active9", true).flush();
                        buttonCustom9.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active9 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(8).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active10 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active10", false).flush();
                        buttonCustom10.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active10 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(9).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active10){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active10", true).flush();
                        buttonCustom10.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active10 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(9).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active11 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active11", false).flush();
                        buttonCustom11.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active11 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(10).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active11){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active11", true).flush();
                        buttonCustom11.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active11 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(10).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active12 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active12", false).flush();
                        buttonCustom12.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(0).setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active12 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(11).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }

                    } else if(!active12){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active12", true).flush();
                        buttonCustom12.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(0).setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        active12 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(11).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active13 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active13", false).flush();
                        buttonCustom13.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(1).setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active13 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(12).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if (!active13){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active13", true).flush();
                        buttonCustom13.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(1).setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        active13 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(12).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active14 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active14", false).flush();
                        buttonCustom14.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(2).setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active14 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(13).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active14){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active14", true).flush();
                        buttonCustom14.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(2).setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        active14 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(13).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        buttonCustom15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active15 && activeLimit > 1) {
                        activeLimit = activeLimit - 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        System.out.println("Hit2");
                        prefs.putBoolean("active15", false).flush();
                        buttonCustom15.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(3).setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active15 = false;
                        for(int i = activeVocabList.size()-1; i >= 0; i--){
                            if(activeVocabList.get(i).getModuleCategory().equals(buttonList.get(14).getText().toString())){
                                System.out.println(activeVocabList.get(i).getEngSpelling());
                                activeVocabList.remove(activeVocabList.get(i));
                            }
                        }
                    } else if(!active15){
                        activeLimit = activeLimit + 1;
                        if(prefs.contains("activeLimit")){
                            prefs.remove("activeLimit");
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        else if(!prefs.contains("activeLimit")){
                            prefs.putInteger("activeLimit", activeLimit).flush();
                        }
                        prefs.putBoolean("active15", true).flush();
                        buttonCustom15.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        deleteButtonList.get(3).setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        active15 = true;
                        for(VocabWord v : dbVocab){
                            if(v.getModuleCategory().equals(buttonList.get(14).getText().toString())){
                                activeVocabList.add(v);
                            }
                        }
                    }
                }
            }
        });

        deleteCustomButton11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 10; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                    buttonList.get(i).setStyle(buttonList.get(i + 1).getStyle());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
                deleteButtonsVisible = false;
                //need to add preference boolean
                for(VocabWord v : activeVocabList){
                    if(v.getModuleCategory().equals(buttonList.get(10).getText().toString())){
                        activeVocabList.remove(v);
                    }
                }
            }
        });

        deleteCustomButton12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 11; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                    buttonList.get(i).setStyle(buttonList.get(i + 1).getStyle());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
                deleteButtonsVisible = false;
                //need to add preference boolean
                for(VocabWord v : activeVocabList){
                    if(v.getModuleCategory().equals(buttonList.get(11).getText().toString())){
                        activeVocabList.remove(v);
                    }
                }
            }
        });
        deleteCustomButton13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 12; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                    buttonList.get(i).setStyle(buttonList.get(i + 1).getStyle());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
                deleteButtonsVisible = false;
                //need to add preference boolean
                for(VocabWord v : activeVocabList){
                    if(v.getModuleCategory().equals(buttonList.get(12).getText().toString())){
                        activeVocabList.remove(v);
                    }
                }
            }
        });
        deleteCustomButton14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (int i = 13; i < buttonList.size() - 1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                    buttonList.get(i).setStyle(buttonList.get(i + 1).getStyle());
                }
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()) {
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                    }
                }
                for (TextButton t : deleteButtonList) {
                    t.setVisible(false);
                    t.setDisabled(true);
                }
                deleteButtonsVisible = false;
                //need to add preference boolean
                for(VocabWord v : activeVocabList){
                    if(v.getModuleCategory().equals(buttonList.get(13).getText().toString())){
                        activeVocabList.remove(v);
                    }
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
                deleteButtonsVisible = false;
                //need to add preference boolean
                for(VocabWord v : activeVocabList){
                    if(v.getModuleCategory().equals(buttonList.get(14).getText().toString())){
                        activeVocabList.remove(v);
                    }
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
                ArrayList<File> fileList;
                if(verified) {
                    fileList = speechGDX.googleDriveAccess();
                    fileTable = new Table(testSkin);
                    Dialog fileDialog = new Dialog("Select File To Upload", testSkin);
                    fileDialog.setPosition(400, 300, Align.center);
                    fileDialog.setMovable(false);
                    fileDialog.getTitleLabel().setAlignment(Align.center);
                    TextButton cancelButton = new TextButton("Cancel", testSkin, "small");
                    for (int i = 0; i < fileList.size(); i++) {
                        currentFile = fileList.get(i);
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
                                    for(int i = 0; i < deleteButtonList.size(); i++){
                                        if(deleteButtonsVisible){
                                            deleteButtonList.get(i).setVisible(true);
                                            deleteButtonList.get(i).setDisabled(false);
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
                    cancelButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            fileDialog.hide();
                            for(int i = 0; i < fileList.size(); i++){
                                currentFile = fileList.get(i);
                                if(currentFile != null){
                                    currentFile.delete();
                                }
                            }

                        }
                    });

                    //fileTable.debug();
                    fileDialog.getContentTable().add(fileTable).top().fill();
                    fileDialog.add(cancelButton).bottom().right().setActorHeight(0.6f);
                    fileDialog.show(stage);
                    //Google Drive API
                }
            }
        });

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //change this to if button in button list selected
                //need to make it do something if the button is held down
                if(verified) {
                    if(!deleteButtonsVisible) {
                        for (int i = 0; i < deleteButtonList.size(); i++) {
                            if (!buttonList.get(i + 10).getText().toString().equals("x")) {
                                deleteButtonList.get(i).setVisible(true);
                                deleteButtonList.get(i).setDisabled(false);
                                deleteButtonsVisible = true;
                            }
                        }
                    }
                    else{
                        for (int i = 0; i < deleteButtonList.size(); i++) {
                            if (!buttonList.get(i + 10).getText().toString().equals("x")) {
                                deleteButtonList.get(i).setVisible(false);
                                deleteButtonList.get(i).setDisabled(true);
                                deleteButtonsVisible = false;
                            }
                        }
                    }

                    for(int i = 0; i < deleteButtonList.size(); i++){
                        if(buttonList.get(i+10).getStyle() == testSkin.get("default", TextButton.TextButtonStyle.class)){
                            deleteButtonList.get(i).setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        }
                        else if (buttonList.get(i+10).getStyle() == testSkin.get("small", TextButton.TextButtonStyle.class)){
                            deleteButtonList.get(i).setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        }
                    }
                }

                //Delete csv file in assets folder with same name
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going from OptionsScreen to SettingsScreen");
                gameMusic.pause();
                gameMusic.dispose();
                // need option to enable navigation bar
                /*if (verified){
                    game.setScreen(new SettingsScreen(game, speechGDX, gameMusic, dbInterface, game.getScreen()));
                }*/
              game.setScreen(new SettingsScreen(game, speechGDX, gameMusic, dbInterface, game.getScreen(), prefs));
            }
        });
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                gameMusic.pause();
                gameMusic.dispose();
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
                gameMusic.setLooping(false);
                gameMusic.setVolume(masterVolume);
                gameMusic.play();
                game.setScreen(new MainMenuScreen(game, speechGDX, gameMusic, dbInterface, activeVocabList, prefs));
                previousScreen.dispose();
                dispose(); // dispose of current GameScreen
            }
        });

        count = 0;
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
        for (TextButton t : deleteButtonList) {
            table6.add(t).padBottom(85);
            table6.row();
        }

        count = 0;

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

        if(prefs.contains("activeLimit")){
            activeLimit = prefs.getInteger("activeLimit");
        }

        //test
        if(prefs.contains("active1")) {
            System.out.println("Contains active1");
            active1 = prefs.getBoolean("active1");
        }

        if(prefs.contains("active2"))
        active2 = prefs.getBoolean("active2");

        if(prefs.contains("active3"))
        active3 = prefs.getBoolean("active3");

        if(prefs.contains("active4"))
        active4 = prefs.getBoolean("active4");

        if(prefs.contains("active5"))
        active5 = prefs.getBoolean("active5");

        if(prefs.contains("active6"))
        active6 = prefs.getBoolean("active6");

        if(prefs.contains("active7"))
        active7 = prefs.getBoolean("active7");

        if(prefs.contains("active8"))
        active8 = prefs.getBoolean("active8");

        if(prefs.contains("active9"))
        active9 = prefs.getBoolean("active9");

        if(prefs.contains("active10"))
        active10 = prefs.getBoolean("active10");

        if(prefs.contains("active11"))
        active11 = prefs.getBoolean("active11");

        if(prefs.contains("active12"))
        active12 = prefs.getBoolean("active12");

        if(prefs.contains("active13"))
        active13 = prefs.getBoolean("active13");

        if(prefs.contains("active14"))
        active14 = prefs.getBoolean("active14");

        if(prefs.contains("active15"))
        active15 = prefs.getBoolean("active15");

        if (!active1) {
            buttonCustom1.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom1, 0);
        } else {
            buttonCustom1.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom1, 0);
        }

        if (!active2) {
            buttonCustom2.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom2, 0);
        } else {
            buttonCustom2.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom2, 0);
        }

        if (!active3) {
            buttonCustom3.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom3, 0);
        } else {
            buttonCustom3.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom3, 0);
        }

        if (!active4) {
            buttonCustom4.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom4, 0);
        } else {
            buttonCustom4.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom4, 0);
        }

        if (!active5) {
            buttonCustom5.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom5, 0);
        } else {
            buttonCustom5.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom5, 0);
        }

        if (!active6) {
            buttonCustom6.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom6, 0);
        } else {
            buttonCustom6.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom6, 0);
        }

        if (!active7) {
            buttonCustom7.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom7, 0);
        } else {
            buttonCustom7.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom7, 0);
        }

        if (!active8) {
            buttonCustom8.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom8, 0);
        } else {
            buttonCustom8.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom8, 0);
        }

        if (!active9) {
            buttonCustom9.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom9, 0);
        } else {
            buttonCustom9.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom9, 0);
        }

        if (!active10) {
            buttonCustom10.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom10, 0);
        } else {
            buttonCustom10.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom10, 0);
        }

        if (!active11) {
            buttonCustom11.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom11, 0);
        } else {
            buttonCustom11.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom11, 0);
        }

        if (!active12) {
            buttonCustom12.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom12, 0);
        } else {
            buttonCustom12.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom12, 0);
        }

        if (!active13) {
            buttonCustom13.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom13, 0);
        } else {
            buttonCustom13.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom13, 0);
        }

        if (!active14) {
            buttonCustom14.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom14, 0);
        } else {
            buttonCustom14.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom14, 0);
        }

        if (!active15) {
            buttonCustom15.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
            deactivateModule(buttonCustom15, 0);
        } else {
            buttonCustom15.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
            activateModule(buttonCustom15, 0);
        }
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
