package asu.gunma.speech;


import java.util.ArrayList;

public interface ActionResolver {
     void startRecognition();
     void stopRecognition();
     void listenOnce();
     void stopListeningOnce();
     String getWord();
     void signIn();
     void signOut();
     public ArrayList<java.io.File> googleDriveAccess();
     ArrayList<String> androidLoginInfo();
     void revokeAccess();
     public String googleLoginMessage();
     public String googleLogoutMessage();
     public Boolean getVerificationBool();
}
