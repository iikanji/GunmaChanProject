package asu.gunma.speech;


import java.util.ArrayList;

public interface ActionResolver {
     void startRecognition();
     String getWord();
     void signIn();
     void stopRecognition();
     void signOut();
     ArrayList<String> androidLoginInfo();
     void revokeAccess();
     public String googleLoginMessage();
     public String googleLogoutMessage();
}
