package Hack;

import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;

import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1beta1.AsyncRecognizeRequest;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SpeechRecognition {
    SpeechClient client;


    public SpeechRecognition() throws IOException {
         client = SpeechClient.create();

    }
    public ArrayList<String> recognizeFromFile(String dataPath) throws Exception {
        ArrayList<String> result = new ArrayList<>();



        Path path = Paths.get(dataPath);
        byte[] data = Files.readAllBytes(path);
        ByteString dataFlow = ByteString.copyFrom(data);

        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setLanguageCode("en-US")
                .setSampleRateHertz(44100)
                .build();

        RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder().setContent(dataFlow).build();

        RecognizeResponse response = client.recognize(config,recognitionAudio);

        List<SpeechRecognitionResult> results = response.getResultsList();

        for (SpeechRecognitionResult result1: results) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result1.getAlternativesList().get(0);

            result.add(alternative.getTranscript()+"\n");
            System.out.println("Printing results out of: "+results.size());
        }
        client.close();
        return result;
    }
}
