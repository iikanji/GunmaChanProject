package com.gunmachan.game;

import android.os.Build;
import android.os.Bundle;


import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.io.File;

import android.content.pm.PackageManager;
import android.util.Log;
import SQLite.*;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.assets.AssetManager;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.GunmaChan;

public class AndroidLauncher extends AndroidApplication {
	public AssetManager assetManager;
	public VocabDb androidDB;
	protected DbInterface dbInterface;

	@Override
	public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
		switch(permsRequestCode){
			case 200:
				boolean RecordingAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
				boolean InternetAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
				break;
		}
	}

	Button buttonStart, buttonStop;
	String AudioSavePathInDevice = null;
	MediaRecorder mediaRecorder;
	MediaPlayer mediaPlayer;

	String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.INTERNET" };
	int permsRequestCode = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GunmaChan(), config);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(perms, permsRequestCode);
		}
		androidDB = newDb();


		test(androidDB);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void test(VocabDb vDB) {
		int vocabIndex = 0;
		String jSpell = "ゼロ";
		String eSpell = "Zero";

		VocabWord vocabWord = new VocabWord(vocabIndex, jSpell, eSpell);
		//VocabWord gameWord = dbInterface.getGameVocabWord();
		//dbInterface.setGameVocabWord(vocabWord);

		vDB.dbInsertVocab(vocabWord);
		List<VocabWord> dbWords = vDB.viewDb();
		for (VocabWord element : dbWords) {
			Log.d("DATABASE", element.getJpnSpelling());
			Log.d("DATABASE", element.getEngSpelling());
		}

		for(VocabWord element : dbWords){
			try {
				vDB.dbDeleteVocab(element);
			} catch(Exception e){
				System.out.println("ENTRY IS NULL");
			}
		}

		try {
			vDB.importCSV("testCSV.csv", getAssets());
		} catch(Exception e){
			System.out.println("FILE NOT FOUND!");
		}

		List<VocabWord> currentDb = vDB.viewDb();
		for(VocabWord element : currentDb){
			System.out.println("JPN: " + element.getJpnSpelling());
			System.out.println("ENG: " + element.getEngSpelling());
		}
	}

	public VocabDb newDb() {
		VocabDb testDb = new VocabDb(AndroidLauncher.this);
		return testDb;
	}
}
