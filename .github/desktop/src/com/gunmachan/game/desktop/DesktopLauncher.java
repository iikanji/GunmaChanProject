package com.gunmachan.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import org.lwjgl.Sys;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;

import asu.gunma.GunmaChan;

public class DesktopLauncher{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// This creates Windowed Borderless mode
		//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");

		config.width = GunmaChan.WIDTH;
		config.height = GunmaChan.HEIGHT;
		config.title = GunmaChan.TITLE;
		// Setting this to true creates Fullscreen mode
		//config.fullscreen = false;
		new LwjglApplication(new GunmaChan(), config);
	}
}
