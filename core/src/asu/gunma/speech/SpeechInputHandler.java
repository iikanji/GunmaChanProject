package asu.gunma.speech;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.SpeechSettings;
import com.google.protobuf.ByteString;
import java.util.List;
import java.nio.ByteBuffer;

import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.Gdx;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;




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

        //Take data, and give to API

        try(SpeechClient speechClient = SpeechClient.create())  {

           /* CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream("/Users/tyler/Desktop/key.json")));

            SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
            SpeechClient speechClient = SpeechClient.create(settings);

            // Instantiates a client
          */

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
        }
    }
}


