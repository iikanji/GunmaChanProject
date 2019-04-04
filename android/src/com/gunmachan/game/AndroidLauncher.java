package com.gunmachan.game;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.User;
import com.gunmachan.SQLite.Instructor;
import com.gunmachan.SQLite.InstructorDb;
import com.gunmachan.SQLite.VocabDb;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;
import asu.gunma.GunmaChan;
import asu.gunma.speech.ActionResolver;

import static android.content.ContentValues.TAG;

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
    public SharedPreferences preferences;

    private Intent signInIntent;
    private GoogleSignInOptions gso;
    private Scope googleDriveScope;
    private GoogleSignInClient mGoogleSignInClient;
    private Boolean verificationBool = false;
    private String googleLoginMessage = "";
    private static final int RC_SIGN_IN = 100;
    private static final int RC_SIGN_OUT = 101;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 102;
    private static final int REQUEST_PICKER = 103;
    private String googleSignOutMessage = "";
    private GoogleSignInAccount account;

    private DriveServiceHelper mDriveServiceHelper;
    private String mOpenFileId;
    public EditText mFileTitleEditText;
    public EditText mDocContentEditText;
    public ArrayList<java.io.File> csvFileList;
    DroidSpeech droidSpeech;

    // add permission to hide navigation bar?
    // create button to exit to home screen under instructor menu
    String[] perms = {"android.permission.RECORD_AUDIO", "android.permission.INTERNET",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferences("initialState");
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

        // Store the EditText boxes to be updated when files are opened/created/modified.
        mFileTitleEditText = findViewById(R.id.file_title_edittext);
        mDocContentEditText = findViewById(R.id.doc_content_edittext);

        googleDriveScope = new Scope("https://www.googleapis.com/auth/drive");
        //DEFAULT_SIGN_IN will request user ID, email address, and profile
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("423723200587-i62dqi74dmheebmv2uqb55n12har4i2m.apps.googleusercontent.com")
                .requestEmail()
                .build();
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

            public String googleLoginMessage() {
                return googleLoginMessage;
            }

            public void signOut() {
                //do signOut operation
                try {
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            revokeAccess();
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            public String googleLogoutMessage() {
                return googleSignOutMessage;
            }

            public void revokeAccess() {
                try {
                    mGoogleSignInClient.revokeAccess();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            public ArrayList<java.io.File> googleDriveAccess() {
                try {
                    csvFileList = new ArrayList<>();
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(account.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();
                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    //query();
                    FileList googleFiles = googleDriveService.files().list().setSpaces("drive").execute();
                    List<File> googleFileList = googleFiles.getFiles();
                    for(File f : googleFileList){
                        System.out.println(f.getName());
                    }
                    //TESTING
                    for(File f : googleFileList){
                        if(f.getMimeType().equals("text/csv")) {
                            try {
                                System.out.println("FILENAME: " + f.getName());
                                System.out.println("EXTENSION: " + "csv");
                                System.out.println("MIME: " + f.getMimeType());
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                googleDriveService.files().get(f.getId())
                                    .executeMediaAndDownloadTo(byteArrayOutputStream);
                                java.io.File csvFile = new java.io.File(android.os.Environment.getExternalStorageDirectory(), f.getName());
                                OutputStream outputStream = new FileOutputStream(csvFile, false);
                                byteArrayOutputStream.writeTo(outputStream);
                                byteArrayOutputStream.close();
                                outputStream.close();
                                System.out.println("PATH: " + csvFile.getAbsolutePath());
                                csvFileList.add(csvFile);
                            }catch(Exception e){
                                System.out.println(e);
                            }
                        }
                    }

                    //startActivityForResult(pickerIntent, REQUEST_PICKER);
                    //openFilePicker();
                } catch (Exception e) {
                    if(e instanceof UserRecoverableAuthIOException) {
                        Intent intent = ((UserRecoverableAuthIOException)e).getIntent();
                        startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT);
                    }
                    System.out.println(e);
                }
                return csvFileList;
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
                String temp = sendWord;
                sendWord = null;
                return temp;
            }

            public Boolean getVerificationBool(){
                return verificationBool;
            }
        };

        dbInterface = new DbInterface() {
            public List<VocabWord> getDbVocab() {
                return androidDB.viewDb();
            }
            public void importCSVFile(String filename){
                try {
                    androidDB.importExternalCSV(filename);
                }
                catch(Exception e){
                    System.out.println(e);
                }
            }
            public void importALLCSV(){
                test(androidDB);
            }
        };

        initialize(new GunmaChan(callback, dbInterface), config);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        androidDB = newVocabDb();
        instructorDb = newInstructorDb();

          //  test(androidDB);


        //androidDB.viewDb();
    }

    public void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();
        sendWord = matches.get(0);
        System.out.println("recognize word " + sendWord);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    account = task.getResult(ApiException.class);
                    System.out.println(account.getDisplayName());
                    System.out.println(account.getEmail());
                    //parse out first, last name from display name
                    Instructor loggedInInstructor = new Instructor();
                    if (!instructorDb.CheckIsDataAlreadyInDBorNot(account.getEmail())) {
                        System.out.println("NOT IN DATABASE");
                        loggedInInstructor.setInstructorFName(account.getGivenName());
                        loggedInInstructor.setInstructorLName(account.getFamilyName());
                        loggedInInstructor.setInstructorEmail(account.getEmail());
                        loggedInInstructor.setInstructorID(parseEmailId(account.getEmail())[0]);
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
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                    if(parseEmailId(account.getEmail())[1].equals("edu-g.gsn.ed.jp")) {
                        verificationBool = true;
                    }
                } else {
                    // Signed in failed.
                    System.out.println("Google Login Failed");
                    googleLoginMessage = "Login Failed";
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                System.out.println("Sign-In failed: " + e);
            }
        }
        if (requestCode == RC_SIGN_OUT) {
            if(!csvFileList.isEmpty()) {
                for (java.io.File f : csvFileList) {
                    csvFileList.remove(f);
                }
            }
            if (mGoogleSignInClient.signOut().isSuccessful()) {
                googleSignOutMessage = "Logout Successful";
            } else {
                googleSignOutMessage = "Logout Failed";
            }
        }
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT) {
            Uri uri = data.getData();
            if (uri != null) {
                openFileFromFilePicker(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideNavigationBar();
        try {
            mGoogleSignInClient.signOut();
        }catch(Exception e){
            e.printStackTrace();
        }
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
            //
            //  CHECK THAT THIS IS ONLY DONE ONCE WHEN CLOSING AND REOPENING THE APP
            //
            vDB.importCSV("Colors-Shapes.csv");
            vDB.importCSV("Countries.csv");
            vDB.importCSV("Days-Months.csv");
            vDB.importCSV("Feelings.csv");
            vDB.importCSV("Fruits-Foods.csv");
            vDB.importCSV("Numbers.csv");
            vDB.importCSV("Places.csv");
            vDB.importCSV("Professions.csv");
            vDB.importCSV("Subjects.csv");
            vDB.importCSV("Time.csv");
            //add time later when fixed
        } catch (Exception e) {
            //System.out.println(e);
        }
        List<VocabWord> currentDb = vDB.viewDb();
       /* for (VocabWord element : currentDb) {
            System.out.println("KANJI: " + element.getKanjiSpelling());
            System.out.println("KANA: " + element.getKanaSpelling());
            System.out.println("ENG: " + element.getEngSpelling());
            System.out.println("Module: " + element.getModuleCategory());
            System.out.println("Correct Word: " + element.getCorrectWords());
            System.out.println("Audio: " + element.getAudio());
        }*/
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

    public String[] parseEmailId(String input) {
        String[] fullname = input.split("@");
        return fullname;
    }
    public String[] parseFile(String input) {
        return input.split(".");
    }

    /**
     * Opens the Storage Access Framework file picker using {@link #REQUEST_CODE_OPEN_DOCUMENT}.
     */
    private void openFilePicker() {
        if (mDriveServiceHelper != null) {
            System.out.println("Opening file picker.");

            Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();

            // The result of the SAF Intent is handled in onActivityResult.
            startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
        }
    }

    /**
     * Opens a file from its {@code uri} returned from the Storage Access Framework file picker
     * initiated by {@link #openFilePicker()}.
     */
    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            System.out.println("Opening " + uri.getPath());

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                        mFileTitleEditText.setText(name);
                        mDocContentEditText.setText(content);

                        // Files opened through SAF cannot be modified.
                        setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            System.out.println("Unable to open file from picker."));
        }
    }

    /**
     * Creates a new file via the Drive REST API.
     */
    private void createFile() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Creating a file.");

            mDriveServiceHelper.createFile()
                    .addOnSuccessListener(fileId -> readFile(fileId))
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't create file.", exception));
        }
    }

    /**
     * Retrieves the title and content of a file identified by {@code fileId} and populates the UI.
     */
    private void readFile(String fileId) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Reading file " + fileId);

            mDriveServiceHelper.readFile(fileId)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                        mFileTitleEditText.setText(name);
                        mDocContentEditText.setText(content);

                        setReadWriteMode(fileId);
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't read file.", exception));
        }
    }

    /**
     * Saves the currently opened file created via {@link #createFile()} if one exists.
     */
    private void saveFile() {
        if (mDriveServiceHelper != null && mOpenFileId != null) {
            Log.d(TAG, "Saving " + mOpenFileId);

            String fileName = mFileTitleEditText.getText().toString();
            String fileContent = mDocContentEditText.getText().toString();

            mDriveServiceHelper.saveFile(mOpenFileId, fileName, fileContent)
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to save file via REST.", exception));
        }
    }

    /**
     * Queries the Drive REST API for files visible to this app and lists them in the content view.
     */
    private void query() {

        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Querying for files.");
            mDriveServiceHelper.queryFiles().addOnSuccessListener(fileList -> {
                StringBuilder builder = new StringBuilder();
                for (File file : fileList.getFiles()) {
                    builder.append(file.getName()).append("\n");
                }
                String fileNames = builder.toString();

                mFileTitleEditText.setText("File List");
                mDocContentEditText.setText(fileNames);
                setReadOnlyMode();
            })
                    .addOnFailureListener(UserRecoverableAuthIOException -> Log.e(TAG, "Unable to query files.", UserRecoverableAuthIOException));
        }
    }

    /**
     * Updates the UI to read-only mode.
     */
    private void setReadOnlyMode() {
        mFileTitleEditText.setEnabled(false);
        mDocContentEditText.setEnabled(false);
        mOpenFileId = null;
    }

    /**
     * Updates the UI to read/write mode on the document identified by {@code fileId}.
     */
    private void setReadWriteMode(String fileId) {
        mFileTitleEditText.setEnabled(true);
        mDocContentEditText.setEnabled(true);
        mOpenFileId = fileId;
    }

}

