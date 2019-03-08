package asu.gunma.speech;


import java.util.ArrayList;

public interface ActionResolver {
     void startRecognition();
     String getWord();
     void signIn();
     public ArrayList<String> androidLoginInfo();
     public String loginMessage();
}
