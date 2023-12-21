package Speech;

/**
 * SpeechRecognitionListener
 * Interface that classes can implement to have access to speech-to-text input
 *
 */
public interface SpeechRecognitionListener {

    /**
     * onSpeechRecognized
     * a method to manipulate the input from Speech-To-Text function, so that it can
     * be used by another class
     *
     * @param transcript the text derived from the speech
     * @param controller the controller that operates the functions for speech-to-text
     */
    void onSpeechRecognized(String transcript, SpeechToTextController controller);
}
