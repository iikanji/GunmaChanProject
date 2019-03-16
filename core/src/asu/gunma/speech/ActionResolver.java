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
     ArrayList<String> androidLoginInfo();
     void revokeAccess();
     public String googleLoginMessage();
     public String googleLogoutMessage();
}
