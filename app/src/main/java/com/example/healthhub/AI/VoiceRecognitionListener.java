package com.example.healthhub.AI;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

public class VoiceRecognitionListener implements RecognitionListener {
    private RecognitionCallback callback;
    // Interface to communicate results back to the activity
    public interface RecognitionCallback {
        void onSpeechRecognized(String recognizedText);
        void onRecognitionError(String errorMessage);
        void onListeningStatusChange(String status);
    }
    public VoiceRecognitionListener(RecognitionCallback callback) {
        this.callback = callback;
    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        System.out.println("onReadyForSpeech");
        if (callback != null) {
            callback.onListeningStatusChange("Listening...");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        System.out.println("onBeginningOfSpeech");
        if (callback != null) {
            callback.onListeningStatusChange("Speak now...");
        }
    }

    @Override
    public void onRmsChanged(float rmsdB) {
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        System.out.println("onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        System.out.println("onEndOfSpeech");
        if (callback != null) {
            callback.onListeningStatusChange("Processing...");
        }
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        System.out.println("onError: " + errorMessage);
        if (callback != null) {
            callback.onRecognitionError(errorMessage);
        }
    }

    @Override
    public void onResults(Bundle results) {
        System.out.println("onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String recognizedText = matches.get(0);
            System.out.println("Recognized Text: " + recognizedText);
            if (callback != null) {
                callback.onSpeechRecognized(recognizedText);
            }
        } else {
            System.out.println("No speech recognized.");
            if (callback != null) {
                callback.onSpeechRecognized("No speech recognized."); // Or handle as a specific error/status
            }
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        System.out.println("onPartialResults");
        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            if (callback != null) {
                callback.onListeningStatusChange("Partial: " + matches.get(0) + "...");
            }
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        System.out.println("onEvent");
    }
    private String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "Recognition service busy";
                break;
            case SpeechRecognizer.ERROR_SERVER_DISCONNECTED:
                message = "Server disconnected";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Unknown error";
                break;
        }
        return message;
    }
}
