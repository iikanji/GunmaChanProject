package asu.gunma.speech;


import java.util.ArrayList;

public interface ActionResolver {
     void startRecognition();
     String getWord();
     boolean signIn();
     boolean signOut();
     ArrayList<String> androidLoginInfo();
}
