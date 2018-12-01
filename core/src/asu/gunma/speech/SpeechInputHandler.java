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
import com.google.cloud.speech.v1p1beta1.SpeechSettings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.Gdx;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;



import java.io.IOException;
import java.util.Random;



import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Game;

public class SpeechInputHandler {

    public void run() throws Exception {

        //Records data
        System.out.println();
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

        int dataSize = data.length;

        //Needs environment variable testing.

       /* try(SpeechClient speechClient = SpeechClient.create())  {

            ByteBuffer buffer = ByteBuffer.allocate(dataSize);
            System.out.println("Finished Buffer");
            for(int i = 0; i < dataSize; i++)
                buffer.putShort(data[i]);
            ByteString audioBytes = ByteString.copyFrom(buffer);
            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();


            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();
            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
        } catch(Exception e) {
            System.out.println(e);
        }*/
    }
}