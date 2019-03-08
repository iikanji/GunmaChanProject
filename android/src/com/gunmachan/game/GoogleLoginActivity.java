package com.gunmachan.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static com.google.android.gms.auth.api.Auth.GoogleSignInApi;

public class GoogleLoginActivity extends Activity {

    private static final int RC_SIGN_IN = 100;
    private Context context;

    public GoogleLoginActivity(){
        this.context = getApplicationContext();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            System.out.println(resultCode);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                System.out.println(account.getDisplayName());
                System.out.println(account.getEmail());
                // Signed in successfully, show authenticated UI.
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                System.out.println(e.getStackTrace());
            }
            /*if (result.isSuccess()) {
                googleLoginMessage = "LOGIN SUCCESSFUL";
                System.out.println("HERE2");
                GoogleSignInAccount account = result.getSignInAccount();
                System.out.println(account.getDisplayName());
                System.out.println(account.getEmail());
            } else {
                googleLoginMessage = "LOGIN FAILED";
            }*/
        }
    }
    public void finish(){
        this.finish();
    }
}
