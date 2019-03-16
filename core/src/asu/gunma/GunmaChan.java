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
			this.setScreen(new TitleScreen(this, speechGDX, dbCallback));

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

