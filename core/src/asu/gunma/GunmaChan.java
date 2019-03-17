package asu.gunma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.SimpleDirectionGestureDetector;
import asu.gunma.ui.screen.menu.TitleScreen;


public class GunmaChan extends Game {
	// Temporary values
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static final String TITLE = "Gunma-chan Game";
	private Music background_music;
	public ActionResolver speechGDX;
	public DbInterface dbCallback;
	private static float masterVolume = 10;

		public GunmaChan() {}
		public GunmaChan(ActionResolver speechGDX, DbInterface dbCallback){
			this.speechGDX = speechGDX;
			this.dbCallback = dbCallback;
		}
		public GunmaChan(ActionResolver speechGDX) {
			this.speechGDX = speechGDX;
		}
		public GunmaChan(DbInterface database){this.dbCallback = database;}

		@Override
		public void create() {
			background_music = Gdx.audio.newMusic(Gdx.files.internal("IntroMusic.mp3"));
			background_music.setLooping(false);
			background_music.setVolume(masterVolume);
			//background_music.play();
			this.setScreen(new TitleScreen(this, speechGDX, dbCallback, background_music));

		}

		@Override
		public void render () {
			super.render();
		}

		@Override
		public void dispose () {
			// close database here
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

