package asu.gunma;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.speech.ActionResolver;
import asu.gunma.ui.util.SimpleDirectionGestureDetector;
import asu.gunma.ui.screen.menu.TitleScreen;


public class GunmaChan extends Game {
	// Temporary values
	public static int WIDTH = 1024;
	public static int HEIGHT = 600;
	public static final String TITLE = "Gunma-chan Game";
	public Music background_music;
	public ActionResolver speechGDX;
	public DbInterface dbCallback;
	private static float masterVolume = 5;
	private ArrayList<VocabWord> activeVocabList;

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
			background_music.play();
			activeVocabList = new ArrayList<>();
			List<VocabWord> dbVocab = dbCallback.getDbVocab();
			activeVocabList.addAll(dbVocab);

			System.out.println("ACTIVE WORD LIST");
			for(VocabWord v : activeVocabList){
				System.out.println(v.getEngSpelling());
			}
			this.setScreen(new TitleScreen(this, speechGDX, dbCallback, background_music, activeVocabList));
		}

		@Override
		public void render () {
			super.render();
		}

		@Override
		public void dispose () {
			// close database here
			background_music.dispose();
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

