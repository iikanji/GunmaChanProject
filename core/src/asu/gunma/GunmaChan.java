package asu.gunma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;

import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.screen.GameScreen;
import asu.gunma.ui.screen.TitleScreen;

public class GunmaChan extends Game {
	// Temporary values
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static final String TITLE = "Gunma-chan Game";
	private Music background_music;
	public ActionResolver speechGDX;

		public GunmaChan() {}
		public GunmaChan(ActionResolver speechGDX) {
			this.speechGDX = speechGDX;
		}

		@Override
		public void create() {
			background_music = Gdx.audio.newMusic(Gdx.files.internal("PerituneMaterial_Sakuya.mp3"));
			background_music.setLooping(true);
			background_music.play();
			this.setScreen(new TitleScreen(this, speechGDX, background_music));

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

