package asu.gunma.speech;


import java.util.List;

public interface ActionResolver {
     void startRecognition();
     String getWord();
     List<String> signIn();
}
