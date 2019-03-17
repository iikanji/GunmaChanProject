package com.gunmachan.game;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gunmachan.SQLite.Instructor;
import com.gunmachan.SQLite.InstructorDb;
import com.gunmachan.SQLite.VocabDb;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.GunmaChan;
import asu.gunma.speech.ActionResolver;

public class AndroidLauncher extends AndroidApplication {

    public AssetManager assetManager;
    public VocabDb androidDB;
    public InstructorDb instructorDb;
    protected DbInterface dbInterface;
    public static final int REQUEST_SPEECH = 0;
    public SpeechRecognizer speechRecognizer;
    public ActionResolver callback;
    protected String sendWord;
    public View decorView;
    public int uiOptions;
    private AndroidApplicationConfiguration config;
    private Intent signInIntent;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private String googleLoginMessage = "";
    private static final int RC_SIGN_IN = 100;
    private static final int RC_SIGN_OUT = 101;
    private String googleSignOutMessage = "";
    DroidSpeech droidSpeech;

    // add permission to hide navigation bar?
    // create button to exit to home screen under instructor menu
    String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.INTERNET",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        droidSpeech = new DroidSpeech(this, null);

        OnDSListener onDSListener = new OnDSListener() {
            @Override
            public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {

            }

            @Override
            public void onDroidSpeechRmsChanged(float rmsChangedValue) {

            }

            @Override
            public void onDroidSpeechLiveResult(String liveSpeechResult) {

            }

            @Override
            public void onDroidSpeechFinalResult(String finalSpeechResult) {
                System.out.println("Recognized Word " + finalSpeechResult);
                sendWord = finalSpeechResult;
            }

            @Override
            public void onDroidSpeechClosedByUser() {

            }

            @Override
            public void onDroidSpeechError(String errorMsg) {

            }
        };

        droidSpeech.setOnDroidSpeechListener(onDSListener);

        hideNavigationBar();
        config = new AndroidApplicationConfiguration();

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

        //DEFAULT_SIGN_IN will request user ID, email address, and profile
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("423723200587-i62dqi74dmheebmv2uqb55n12har4i2m.apps.googleusercontent.com").requestEmail().build();
        //creating sign in object with options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInIntent = new Intent(mGoogleSignInClient.getSignInIntent());

        callback = new ActionResolver() {
            @Override
            //method that starts the Google login client
            public void signIn() {
                startActivityForResult(signInIntent, RC_SIGN_IN);
                setResult(RESULT_OK, signInIntent);
            }
            public String googleLoginMessage(){
                return googleLoginMessage;
            }

            public void signOut() {
                //do signOut operation
                try {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            revokeAccess();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }
            public String googleLogoutMessage(){
                return googleSignOutMessage;
            }

            public void revokeAccess() {
                try {
                    mGoogleSignInClient.revokeAccess();
                } catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }

            public ArrayList<String> androidLoginInfo() {
                ArrayList<String> loginCredentials = new ArrayList<>();
                Account acct;
                AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                acct = getAccount(manager);
                String accountName = acct.name;
                String fullName = accountName.substring(0, accountName.lastIndexOf("@"));
                loginCredentials.add(accountName);
                loginCredentials.add(fullName);
                return loginCredentials;
            }

            public void startRecognition() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            droidSpeech.startDroidSpeechRecognition();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            public void listenOnce() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
                            speechRecognizer.startListening(intent);
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            public void stopListeningOnce(){
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            speechRecognizer.stopListening();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
            public void stopRecognition() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            droidSpeech.closeDroidSpeechOperations();
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

        dbInterface = new DbInterface() {
            public List<VocabWord> getDbVocab() {
                return androidDB.viewDb();
            }
        };

        initialize(new GunmaChan(callback, dbInterface), config);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }
        androidDB = newVocabDb();
        instructorDb = newInstructorDb();
        test(androidDB);
        androidDB.viewDb();
    }

    public void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();
        sendWord = matches.get(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
            // Get the spoken sentence..
            ArrayList<String> thingsYouSaid =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // ..and pass it to the textField:
            //speechGDX.setTextFieldText(thingsYouSaid.get(0));
            Gdx.app.log("you said: ", thingsYouSaid.get(0));
        }
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (googleSignInResult.isSuccess()) {
                    // Signed in successfully.
                    System.out.println("Google Login Success");
                    googleLoginMessage = "Login Successful";
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    System.out.println(account.getDisplayName());
                    System.out.println(account.getEmail());
                    //parse out first, last name from display name
                    Instructor loggedInInstructor = new Instructor();
                    //System.out.println(instructorDb.CheckIsDataAlreadyInDBorNot(account.getEmail()));
                    if (!instructorDb.CheckIsDataAlreadyInDBorNot(account.getEmail())) {
                        System.out.println("NOT IN DATABASE");
                        loggedInInstructor.setInstructorFName(account.getGivenName());
                        loggedInInstructor.setInstructorLName(account.getFamilyName());
                        loggedInInstructor.setInstructorEmail(account.getEmail());
                        loggedInInstructor.setInstructorID(parseEmailId(account.getEmail()));
                        instructorDb.dbInsertInstructor(loggedInInstructor);
                    } else {
                        System.out.println("INSTRUCTOR ALREADY EXISTS");
                    }
                    //insert this into instructor database;
                    List<Instructor> instructorList = instructorDb.viewDb();
                    for (Instructor element : instructorList) {
                        System.out.println(element.getId());
                        System.out.println(element.getInstructorFName());
                        System.out.println(element.getInstructorLName());
                        System.out.println(element.getInstructorID());
                        System.out.println(element.getInstructorEmail());
                    }
                    Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
                } else {
                    // Signed in failed.
                    System.out.println("Google Login Failed");
                    googleLoginMessage = "Login Failed";
                }
            }catch(ApiException e){
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                System.out.println("Sign-In failed: " + e.getStackTrace());
            }
        }
        if(requestCode == RC_SIGN_OUT){
            if (mGoogleSignInClient.signOut().isSuccessful()) {
                googleSignOutMessage = "Logout Successful";
            } else {
                googleSignOutMessage = "Logout Failed";
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.onResume();
        hideNavigationBar();
    }

    @Override
    public void onBackPressed() {
        hideNavigationBar();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            hideNavigationBar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * Called as part of the activity lifecycle when an activity is about to go into the background
     * as the result of user choice. For example, when the user presses the Home key, onUserLeaveHint() will be called,
     * but when an incoming phone call causes the in-call Activity to be automatically brought to the foreground,
     * onUserLeaveHint() will not be called on the activity being interrupted.
     * In cases when it is invoked, this method is called right before the activity's onPause() callback.
     */
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
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

    public void test(VocabDb vDB) {
        try {
            vDB.importCSV("Numbers.csv");
            vDB.importCSV("Colors-Shapes.csv");
            vDB.importCSV("Countries.csv");
            vDB.importCSV("Days-Months.csv");
            vDB.importCSV("Feelings.csv");
            vDB.importCSV("Subjects.csv");
            vDB.importCSV("Fruits-Foods.csv");
            vDB.importCSV("Professions.csv");
            vDB.importCSV("Places.csv");
            vDB.importCSV("Time.csv");
        } catch (Exception e) {
            System.out.println(e);
        }
        List<VocabWord> currentDb = vDB.viewDb();
        for (VocabWord element : currentDb) {
            System.out.println("KANJI: " + element.getKanjiSpelling());
            System.out.println("KANA: " + element.getKanaSpelling());
            System.out.println("ENG: " + element.getEngSpelling());
            System.out.println("Module: " + element.getModuleCategory());
            System.out.println("Correct Word: " + element.getCorrectWords());
        }
    }

    public VocabDb newVocabDb() {
        VocabDb testDb = new VocabDb(AndroidLauncher.this);
        return testDb;
    }

    public InstructorDb newInstructorDb() {
        InstructorDb testDb = new InstructorDb(AndroidLauncher.this);
        return testDb;
    }

    private void hideNavigationBar() {
        decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    public String parseEmailId(String input) {
        String[] fullname = input.split("@");
        return fullname[0];
    }
}

