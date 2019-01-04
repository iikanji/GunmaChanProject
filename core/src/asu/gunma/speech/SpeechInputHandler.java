package asu.gunma.speech;

import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.Gdx;


import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.StudentMetric;
import asu.gunma.DbContainers.VocabWord;

public class SpeechInputHandler {


    public void run() throws Exception {
        DbCallback dbCallback = new DbCallback();
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


        //inputWord = new VocabWord();
        //studentMetric = studentMetric();
        //if english word
        //  set inputWord.setEngSpelling = google word
        //if japanese word
        //  set inputWord.setJpnSpelling = google word
        //search for word.
        //  WRITE FUNCTION FOR SEARCHING WORDS IF ENG SPELLING = NULL
        //  OR IF JPN SPELLING = NULL


    }
}