package asu.gunma.speech;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.BidiStreamingCallable;
import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.cloud.speech.v1.WordInfo;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



import java.io.IOException;
import java.util.Random;



import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Game;

public class SpeechInputHandler {

        public void run() {

                //Records data
                System.out.println("Record: Start");
                int samples = 44100;
                int seconds = 5;
                boolean isMono = true;
                short[] data = new short[samples * seconds];

                AudioRecorder recorder = Gdx.audio.newAudioRecorder(samples, isMono);
                AudioDevice player = Gdx.audio.newAudioDevice(samples, isMono);

            recorder.read(data, 0, data.length);
            recorder.dispose();
            System.out.println("Record: End");



            //Turn data into Text





            //System.out.println("Play : Start");
            //player.writeSamples(data, 0, data.length);
            //System.out.println("Play : End");
            //player.dispose();
        }
}

