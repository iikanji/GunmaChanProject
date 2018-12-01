package asu.gunma.ui.util;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

// This needs to be moved to its own file later.
enum Type {
    MAIN, OPTION, PAUSE_MENU;
}

/*
    This class provides a button with custom text.
*/
public class MenuButton extends TextButton {
    private TextButton button;

    public MenuButton(String text, Skin skin) {
        super(text, skin);
//        switch (type) {
//            case MAIN:
//                background = new Texture("");
//            case OPTION:
//                background = new Texture("");
//            case PAUSE_MENU:
//                background = new Texture("");
//            default:
//                System.out.println("Error: Please check Type enum in MenuButton class.");
//                break;
//        }


    }

//    // It may be better to store background as a String and create the Texture elsewhere.
//    public Texture getBackground() {
//        return background;
//    }
//
//    public String getText() {
//        return this.text;
//    }
//
//    // Uncertain if this method is necessary
//    public void setText(String text) {
//        this.text = text;
//    }
}