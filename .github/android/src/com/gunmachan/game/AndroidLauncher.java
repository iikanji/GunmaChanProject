package com.gunmachan.game;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.util.Log;
import com.badlogic.gdx.Gdx;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.gunmachan.SQLite.*;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import asu.gunma.GunmaChan;
import asu.gunma.speech.ActionResolver;

public class AndroidLauncher extends AndroidApplication {

	/*public VocabDb androidDB;
	Button buttonStart, buttonStop;
	String AudioSavePathInDevice = null;
	MediaRecorder mediaRecorder;
	MediaPlayer mediaPlayer;*/
	public View view;
	public static final int REQUEST_SPEECH = 0;
	public SpeechRecognizer speechRecognizer;
	public ActionResolver callback;
	protected String sendWord;

	String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.INTERNET", "android.permission.WRITE_EXTERNAL_STORAGE"};
	int permsRequestCode = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

		final RecognitionProgressView recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);

		recognitionProgressView.setSpeechRecognizer(speechRecognizer);

		recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
			@Override
			public void onResults(Bundle results) {
				showResults(results);
			}
		});

		ActionResolver callback = new ActionResolver() {
			@Override
			public void startRecognition() {

				try {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							System.out.println("Start Recognition");
							Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
							intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
							intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
							intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
							speechRecognizer.startListening(intent);
							System.out.println("End Recognition");
						}
					});
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			public String getWord() {
				return sendWord;
			}
		};


		initialize(new GunmaChan(callback),config);
		if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)

			{
				requestPermissions(perms, permsRequestCode);
			}
			//androidDB = newDb();
			//test(androidDB);

	}

	public void showResults(Bundle results) {
		ArrayList<String> matches = results
				.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();
		sendWord = matches.get(0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
			// Get the spoken sentence..
			ArrayList<String> thingsYouSaid =
					data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			// ..and pass it to the textField:
			//speechGDX.setTextFieldText(thingsYouSaid.get(0));
			Gdx.app.log("you said: ", thingsYouSaid.get(0));
		}
	}
	@Override
	public void onResume(){
		super.onResume();
	}

	public void test(VocabDb vDB) {
		int vocabIndex = 0;
		String jSpell = "ゼロ";
		String eSpell = "Zero";
		VocabWord vocabWord = new VocabWord(vocabIndex, jSpell, eSpell);
		vDB.dbInsertVocab(vocabWord);
		List<VocabWord> dbWords = vDB.viewDb();
		for (VocabWord element : dbWords) {
			Log.d("DATABASE", element.getJpnSpelling());
			Log.d("DATABASE", element.getEngSpelling());
		}
	}

	public VocabDb newDb() {
		VocabDb testDb = new VocabDb(AndroidLauncher.this);
		return testDb;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
		switch (permsRequestCode) {
			case 200:
				boolean RecordingAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
				boolean InternetAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
				boolean ExternalAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

				break;
		}
	}
}


