package asu.gunma.ui.screen.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class OptionMenu implements Screen {

    private Game game;

    // Using these are unnecessary but will make our lives easier.
    private Stage stage;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion region;
    private Skin testSkin;
    private Table table, table2, table3, table4, table5, table6;

    //true = instructor, false = student
    private boolean verified = true;
    private boolean active = true;
    //temp bool until login system works
    private boolean login = false;

    private TextButton buttonCustom1, buttonCustom2, buttonCustom3, buttonCustom4, buttonCustom5, buttonCustom6,
            buttonCustom7, buttonCustom8, buttonCustom9, buttonCustom10, buttonCustom11, buttonCustom12,
            buttonCustom13, buttonCustom14, buttonCustom15;

    private TextButton deleteCustomButton1, deleteCustomButton2, deleteCustomButton3, deleteCustomButton4,
            deleteCustomButton5, deleteCustomButton6, deleteCustomButton7, deleteCustomButton8, deleteCustomButton9,
            deleteCustomButton10, deleteCustomButton11, deleteCustomButton12, deleteCustomButton13,deleteCustomButton14,
            deleteCustomButton15;

    private TextButton newButton, deleteButton, settingsButton;
    private ArrayList<TextButton> buttonList;
    private ArrayList<TextButton> deleteButtonList;

    private SpriteBatch batch;
    private Texture texture;

    private BitmapFont font;

    public OptionMenu(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(.8f, 1, 1, 1);
        stage = new Stage();

        testSkin = new Skin(Gdx.files.internal("glassy-ui.json"));

        batch = new SpriteBatch();
        texture = new Texture("title_gunma.png");

        Gdx.input.setInputProcessor(stage);

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

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

        testSkin.getFont("font-big").getData().setScale(0.8f,0.8f);


        // IMPORTANT: needs localization support
        // Make image buttons
        //use drawable to set image
        buttonCustom1 = new TextButton("Alphabet", testSkin, "default");
        buttonCustom2 = new TextButton("Colors/Shapes", testSkin, "default");
        buttonCustom3 = new TextButton("Countries", testSkin, "default");
        buttonCustom4 = new TextButton("Days/Months", testSkin, "default");
        buttonCustom5 = new TextButton("Feelings", testSkin, "default");
        buttonCustom6 = new TextButton("Fruits/Foods", testSkin, "default");
        buttonCustom7 = new TextButton("Numbers", testSkin, "default");
        buttonCustom8 = new TextButton("Places", testSkin, "default");
        buttonCustom9 = new TextButton("Professions", testSkin, "default");
        buttonCustom10 = new TextButton("Subjects", testSkin, "default");
        buttonCustom11 = new TextButton("Time", testSkin, "default");
        buttonCustom12 = new TextButton("x", testSkin, "default");
        buttonCustom13 = new TextButton("x", testSkin, "default");
        buttonCustom14 = new TextButton("x", testSkin, "default");
        buttonCustom15 = new TextButton("x", testSkin, "default");

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

        for(TextButton t : buttonList) {
            if (t.getText().toString().equals("x")) {
                t.setVisible(false);
                t.setDisabled(true);
            }
        }

        for(TextButton t : deleteButtonList) {
                t.setVisible(false);
                t.setDisabled(true);
        }

        // Actually, should probably custom class this process

        /*
            If you want to test functions with UI instead of with console,
            add it into one of these Listeners. Each of them correspond to
            one of the buttons on the screen in top-down order.
         */

        buttonCustom1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if (verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom1.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom1.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom2.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom2.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom3.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom3.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom4.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom4.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom5.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom5.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom6.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom6.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom7.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom7.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom8.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom8.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom9.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom9.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom10.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom10.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom11.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom11.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom12.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom12.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom13.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom13.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom14.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom14.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        buttonCustom15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hit");
                if(verified) {
                    if (active) {
                        System.out.println("Hit2");
                        buttonCustom15.setStyle(testSkin.get("small", TextButton.TextButtonStyle.class));
                        active = false;
                    } else {
                        buttonCustom15.setStyle(testSkin.get("default", TextButton.TextButtonStyle.class));
                        //buttonCustom1.setStyle(testSkin.get(Button.ButtonStyle.class));
                        active = true;
                    }
                }
            }
        });

        deleteCustomButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 0; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 1; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 2; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 3; i < buttonList.size()-1; i++) {
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 4; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 5; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 6; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 7; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 8; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 9; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton11.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 10; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton12.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 11; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton13.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 12; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton14.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(int i = 13; i < buttonList.size()-1; i++){
                    buttonList.get(i).setText(buttonList.get(i + 1).getText().toString());
                }
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x") && buttonList.get(i).isVisible()){
                        buttonList.get(i).setVisible(false);
                        buttonList.get(i).setDisabled(true);
                        deleteButtonList.get(i).setVisible(false);
                        deleteButtonList.get(i).setDisabled(true);
                    }
                }
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });
        deleteCustomButton15.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonList.get(buttonList.size()-1).setText("x");
                buttonList.get(buttonList.size()-1).setVisible(false);
                buttonList.get(buttonList.size()-1).setDisabled(true);
                for(TextButton t : deleteButtonList){
                    t.setVisible(false);
                    t.setDisabled(true);
                }
            }
        });

        newButton = new TextButton("Add", testSkin, "default");
        deleteButton = new TextButton("Delete", testSkin, "default");
        settingsButton = new TextButton("Settings", testSkin, "default");
        newButton.setTransform(true);
        newButton.setScale(0.5f);
        deleteButton.setTransform(true);
        deleteButton.setScale(0.5f);
        settingsButton.setTransform(true);
        settingsButton.setScale(0.5f);

        newButton.setPosition(850, 250);
        deleteButton.setPosition(850, 175);
        settingsButton.setPosition(850, 100);
        newButton.getLabel().setAlignment(Align.center);
        deleteButton.getLabel().setAlignment(Align.center);
        settingsButton.getLabel().setAlignment(Align.center);


        //log out button or use google button?

        newButton.setVisible(verified);
        deleteButton.setVisible(verified);
        settingsButton.setVisible(verified);

        newButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                for(int i = 0; i < buttonList.size(); i++){
                    if(buttonList.get(i).getText().toString().equals("x")){
                        //this will become something else later
                        buttonList.get(i).setText("Module Name");
                        buttonList.get(i).setVisible(true);
                        buttonList.get(i).setDisabled(false);
                        if(table.getRows() < 5){
                            table.addActor(buttonList.get(i));
                        }
                        else if(table2.getRows() < 5){
                            table2.addActor(buttonList.get(i));
                        }
                        else if(table3.getRows() < 5){
                            table3.addActor(buttonList.get(i));
                        }
                        break;
                    }
                }
            }
        });

        deleteButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //change this to if button in button list selected
                //need to make it do something if the button is held down
                for(int i = 0; i < deleteButtonList.size(); i++){
                    if(!buttonList.get(i).getText().toString().equals("x")) {
                        deleteButtonList.get(i).setVisible(true);
                        deleteButtonList.get(i).setDisabled(false);
                    }
                }

                //maybe change to boolean for closing delete buttons for custom buttons
            }
        });

        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                System.out.println("Going from OptionsScreen to SettingsScreen");
                active = false;
                /*gameMusic.pause();
                /musicOnOff = false;
                dispose(); // dispose of current GameScreen
                previousScreen.dispose();
                game.setScreen(new SettingsScreen(game, speechGDX, dbCallback));*/
            }
        });

        //use to activate the lists in the database
        for(int i = 0; i < buttonList.size(); i++){
            if(buttonList.get(i).getStyle() == textButtonStyle){
                String s = buttonList.get(i).getText().toString();
                //do stuff with database
            }
        }

        int count = 0;
        for(TextButton t : buttonList) {
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
        for(TextButton t : deleteButtonList) {
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
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // SpriteBatch is resource intensive, try to use it for only brief moments
        batch.begin();
        batch.draw(texture, Gdx.graphics.getWidth()/2 - texture.getWidth()/4 + 415, Gdx.graphics.getHeight()/4 - texture.getHeight()/2 + 400, texture.getWidth()/2, texture.getHeight()/2);
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
