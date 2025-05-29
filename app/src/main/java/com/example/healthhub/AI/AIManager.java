package com.example.healthhub.AI;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * Singleton class to manage all AI-related services (Speech-to-Text, NLU, Text-to-Speech)
 * across the entire application.
 * It implements CustomRecognitionListener.RecognitionCallback to receive speech results.
 */
public class AIManager implements VoiceRecognitionListener.RecognitionCallback{
    private static AIManager instance; // Singleton instance

    private Context appContext; // Application context for long-lived services
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private TextToSpeech tts;

    private NLUParser nluParser;
    private IntentDispatcher intentDispatcher;
    private AppActionSpeaker currentSpeaker; // Reference to the Activity/Fragment currently interacting with AI
    private TFLiteNLUModel tfliteNLUModel;
    /**
     * Private constructor for Singleton pattern.
     * Initializes all AI components with the application context.
     * @param context The application context.
     */
    private AIManager(@NonNull Context context) {
        this.appContext = context.getApplicationContext(); // Use application context to avoid leaks

        // Initialize NLU components
        nluParser = new NLUParser();
        // Pass null initially for speaker. It will be set dynamically by Activities.
        intentDispatcher = new IntentDispatcher(null);
        // Initialize your TFLite NLU model here
        tfliteNLUModel = new TFLiteNLUModel(appContext); // <--- NEW: Initialize TFLite model

        // Initialize SpeechRecognizer
        if (SpeechRecognizer.isRecognitionAvailable(appContext)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext);
            // Pass 'this' (AIManager) as the callback for the custom listener
            speechRecognizer.setRecognitionListener(new VoiceRecognitionListener(this));

            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            // This is required for speech recognizer in some Android versions/ROMs
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, appContext.getPackageName());
        } else {
            System.out.println("Speech recognition is not available on this device.");
            Toast.makeText(appContext, "Speech recognition not available.", Toast.LENGTH_LONG).show();
        }

        // Initialize TextToSpeech
        tts = new TextToSpeech(appContext, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    System.out.println("TTS Language not supported or missing data.");
                } else {
                    System.out.println("TTS Language supported.");
                }
            } else {
                System.out.println("TTS Initialization failed.");
                Toast.makeText(appContext, "Text-to-Speech not available.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Returns the singleton instance of AIManager.
     * @param context An application context (or any context) to create the instance if it doesn't exist.
     * @return The AIManager singleton instance.
     */
    public static synchronized AIManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new AIManager(context);
        }
        return instance;
    }

    /**
     * Sets the current AppActionSpeaker. This method should be called by the active Activity/Fragment
     * in its onResume() method to ensure AI feedback goes to the correct UI.
     * @param speaker The currently active AppActionSpeaker (e.g., an Activity instance).
     */
    public void setCurrentSpeaker(@NonNull AppActionSpeaker speaker) {
        this.currentSpeaker = speaker;
        // Update the dispatcher with the active speaker as well
        this.intentDispatcher.setSpeaker(speaker);
        System.out.println("Setting current speaker to: " + speaker.getClass().getSimpleName());
    }

    /**
     * Clears the current AppActionSpeaker. This method should be called by the Activity/Fragment
     * in its onPause() method to prevent memory leaks and ensure feedback is not sent to an inactive UI.
     */
    public void clearCurrentSpeaker() {
        if (this.currentSpeaker != null) {
            System.out.println("Clearing current speaker: " + this.currentSpeaker.getClass().getSimpleName());
        }
        this.currentSpeaker = null;
        this.intentDispatcher.setSpeaker(null);
    }

    /**
     * Starts the speech recognition process.
     */
    public void startListening() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
            if (currentSpeaker != null) {
                currentSpeaker.showToast("Listening...");
                currentSpeaker.updateStatus("Listening...");
            }
            System.out.println("Starting speech recognition...");
        } else {
            if (currentSpeaker != null) {
                currentSpeaker.showToast("Speech Recognizer not available.");
            }
            System.out.println("Speech Recognizer is null. Check initialization.");
        }
    }

    /**
     * Stops the current speech recognition process.
     */
    public void stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            System.out.println("Stopping speech recognition...");
        }
    }

    /**
     * Cancels the current speech recognition process immediately.
     */
    public void cancelListening() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            System.out.println("Cancelling speech recognition...");
        }
    }

    /**
     * Shuts down all AI-related services. This should be called only when the application
     * is completely exiting or no longer needs AI services.
     */
    public void shutdown() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if (tfliteNLUModel != null) { // <--- NEW: Close TFLite model
            tfliteNLUModel.close();
            tfliteNLUModel = null;
        }
        instance = null; // Clear the singleton instance
    }

    /**
     * Internal method for AIManager to speak text using its own TTS instance.
     * @param text The text to speak.
     */
    private void speakResponse(String text) {
        if (tts != null && text != null && !text.isEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            System.out.println("TextToSpeech not initialized or text is empty/null in AIManager.");
        }
    }

    @Override
    public void onSpeechRecognized(String recognizedText) {
        if (currentSpeaker != null) {
            currentSpeaker.updateStatus("You said: " + recognizedText);
        }
        System.out.println("Recognized Text: " + recognizedText);

        /*// --- IMPORTANT: REPLACE THIS SIMULATION WITH YOUR ACTUAL TFLITE INFERENCE ---
        String nluModelOutputString;
        if (recognizedText.toLowerCase(Locale.ROOT).contains("home")) {
            nluModelOutputString = "intent: navigate_home | response: Just follow the instructions on the map and you will arrive home.";
        } else if (recognizedText.toLowerCase(Locale.ROOT).contains("hospital")) {
            nluModelOutputString = "intent: find_hospitals | filter: nearby | response: Displaying nearby hospitals.";
        } else if (recognizedText.toLowerCase(Locale.ROOT).contains("hello") || recognizedText.toLowerCase(Locale.ROOT).contains("hi")) {
            nluModelOutputString = "intent: greet | response: Hello there! How can I assist you today?";
        } else if (recognizedText.toLowerCase(Locale.ROOT).contains("thank you")) {
            nluModelOutputString = "intent: thank_you | response: You're welcome! Happy to help.";
        }
        else {
            nluModelOutputString = "intent: unknown | response: I didn't understand that. Can you please rephrase?";
        }
        // --- END OF NLU MODEL SIMULATION ---*/
        // --- NEW: CALL YOUR TFLITE NLU MODEL HERE ---
        String nluModelOutputString = null; // Initialize to null
        if (tfliteNLUModel != null) {
            try {
                // Call the TFLite model to get the intent and response string
                nluModelOutputString = tfliteNLUModel.runInference(recognizedText);
            } catch (Exception e) {
                System.out.println("Error running TFLite NLU model inference: " + e.getMessage());
                if (currentSpeaker != null) {
                    currentSpeaker.showToast("AI model error. Please try again.");
                }
                // Fallback to a generic response if the model fails
                nluModelOutputString = "intent: unknown | response: I apologize, there was an error processing your request with the AI model. Please try again.";
            }
        } else {
            System.out.println("TFLite NLU model is not initialized.");
            if (currentSpeaker != null) {
                currentSpeaker.showToast("AI model not ready. Please restart app.");
            }
            nluModelOutputString = "intent: unknown | response: I am not ready to respond yet. Please try again later.";
        }
        // --- END OF TFLITE MODEL CALL ---
        // Parse the NLU output string
        ParsedNLUResult parsedResult = nluParser.parse(nluModelOutputString);

        // Dispatch the intent to the appropriate handler.
        // The chosen handler will perform the action and return the response string.
        String spokenResponse = intentDispatcher.dispatch(parsedResult);

        // AIManager speaks the final response, and also updates the UI of the current speaker.
        speakResponse(spokenResponse);
        if (currentSpeaker != null) {
            currentSpeaker.updateStatus("AI: " + spokenResponse);
        }
    }

    @Override
    public void onRecognitionError(String errorMessage) {
        if (currentSpeaker != null) {
            currentSpeaker.updateStatus("Speech Error: " + errorMessage);
            currentSpeaker.showToast("Speech Error: " + errorMessage);
        }
        System.out.println("Speech Recognition Error: " + errorMessage);
        // Also speak out the error for voice-first interaction
        speakResponse("Sorry, I had a problem understanding your speech. " + errorMessage);
    }

    @Override
    public void onListeningStatusChange(String status) {
        if (currentSpeaker != null) {
            currentSpeaker.updateStatus(status);
        }
        System.out.println("Speech Recognizer Status: " + status);
    }
}
