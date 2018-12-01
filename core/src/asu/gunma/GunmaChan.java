package asu.gunma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import asu.gunma.ui.screen.TitleScreen;

public class GunmaChan extends Game {
	// Temporary values
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static final String TITLE = "Gunma-chan Game";

	@Override
	public void create() {
		this.setScreen(new TitleScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
