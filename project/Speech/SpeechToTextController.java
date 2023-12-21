package Speech;

import AdventureModel.AdventureGame;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import javafx.application.Platform;
import views.AdventureGameView;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.io.IOException;

import javax.swing.*;
/**
 * SpeechToTextController
 * a class that uses the Google Speech-to-Text API to
 * achieve functionality and implement the speech to text user story
 *
 */
public class SpeechToTextController {
    private Speech.SpeechRecognitionListener listener;
    private boolean eventSubmitted = false;

    private boolean stopRequested = false;

    /**
     * SpeechToTextController
     * constructor method
     *
     * @param listener the object that listens to the speech output
     */
    public SpeechToTextController(Speech.SpeechRecognitionListener listener) {
        this.listener = listener;
    }

    /**
     * convertSpeechToText
     *
     * main method that converts the speech input from the microphone into text
     *
     */
    public void convertSpeechToText() {
        try {
            SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder()
                    .build());

            // Configure microphone input
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
            microphone.start();

            // Capture audio input and create a RecognitionConfig
            ClientStream<StreamingRecognizeRequest> clientStream = speechClient.streamingRecognizeCallable().splitCall(new ResponseObserver<StreamingRecognizeResponse>() {
                @Override
                public void onStart(StreamController controller) {
                }

                @Override
                public void onResponse(StreamingRecognizeResponse response) {
                    if (!response.getResultsList().isEmpty()) {
                        StringBuilder transcriptBuilder = new StringBuilder();

                        for (StreamingRecognitionResult result : response.getResultsList()) {
                            if (!result.getAlternativesList().isEmpty()) {
                                String transcript = result.getAlternatives(0).getTranscript();
                                transcriptBuilder.append(transcript).append(" ");
                            }
                        }

                        String finalTranscript = transcriptBuilder.toString().trim();
                        if (!finalTranscript.isEmpty()) {
                            // Set inputTextField to the concatenated transcript
                            Platform.runLater(() -> listener.onSpeechRecognized(finalTranscript, SpeechToTextController.this));
                        }
                    }
                }

                @Override
                public void onComplete() {
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                }
            });

            // Configure recognition config
            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .setModel("default")
                            .build();

            // Send initial recognition config
            clientStream.send(StreamingRecognizeRequest.newBuilder()
                    .setStreamingConfig(StreamingRecognitionConfig.newBuilder()
                            .setConfig(recognitionConfig)
                            .setInterimResults(true)
                            .build())
                    .build());

            // Continuously send audio data
            byte[] data = new byte[3200];
            long startTime, elapsedTime;

            while (microphone.isOpen() && !eventSubmitted && !stopRequested) {
                startTime = System.currentTimeMillis();
                int bytesRead = microphone.read(data, 0, data.length);
                if (bytesRead > 0) {
                    ByteString audioData = ByteString.copyFrom(data, 0, bytesRead);
                    clientStream.send(StreamingRecognizeRequest.newBuilder()
                            .setAudioContent(audioData)
                            .build());
                }
                elapsedTime = System.currentTimeMillis() - startTime;

                try {
                    Thread.sleep(Math.max(0, 20 - elapsedTime));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Stop the streaming recognizer when done
            clientStream.closeSend();
            speechClient.close();

        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.OUT_OF_RANGE) {
                // do nothing
            } else {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * setEventSubmitted
     * ---
     * changes the class instance for acknowledging eventSubmissions
     * prevents multiple submissions of the speech-to-text output at once
     */
    public void setEventSubmitted(boolean eventSubmitted) {
        this.eventSubmitted = eventSubmitted;
    }

    /**
     * stopRecording
     * ---
     * Acknowledges when the stopButton is pressed in the AdventureGameView
     * prevents the controller from continuing to record and convert speech to text.
     */
    public void stopRecording() { this.stopRequested = true; }

    /**
     * setStopRecording
     * ---
     * similar to stopRecording, class instance can be set to any boolean value. For example,
     * I would setStopRecording(false) when I want to input speech to text.
     * @param eventSubmitted the boolean that you want to flag the class instance with.
     */
    public void setStopRecording(boolean eventSubmitted) {
        this.stopRequested = eventSubmitted;
    }
}
