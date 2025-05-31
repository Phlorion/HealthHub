package com.example.healthhub;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.healthhub.AI.AIManager;
import com.example.healthhub.AI.AppActionSpeaker;
import com.example.healthhub.AI.PredictionRequest;
import com.example.healthhub.AI.PredictionResponse;
import com.example.healthhub.AI.RetrofitClient;

public class AITesting extends AppCompatActivity implements AppActionSpeaker {
    private static final String TAG = "AITesting";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 500;
    Button aiButton;
    TextView aiResponse;
    private AIManager aiManager; // Instance of the AI manager

    private String[] permissions = {android.Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aitesting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        aiButton = findViewById(R.id.ai_button);
        aiResponse = findViewById(R.id.ai_response);

        // Get the AIManager singleton instance.
        // It's crucial to pass application context here for long-lived services.
        aiManager = AIManager.getInstance(getApplicationContext());
        aiButton.setOnClickListener(v -> {
            // Always check microphone permission before starting AI interaction
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                aiManager.startListening();
            } else {
                showToast("Microphone permission is required to speak.");
                requestAudioPermission(); // Request permission if not granted
            }
        });
        // Initial permission check on creation
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestAudioPermission();
        } else {
            aiResponse.setText("Press mic to speak...");
        }
    }

    private void requestAudioPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            System.out.println("RECORD_AUDIO permission already granted.");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio recording permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Audio recording permission denied. Speech recognition may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateStatus(String status) {
        // Update the TextView on this specific screen with the AI's status/response
        aiResponse.setText(status);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // IMPORTANT: Set this Activity as the current speaker for AIManager.
        // This ensures that all AI feedback (toasts, UI updates, TTS) is directed
        // to this currently active screen.
        aiManager.setCurrentSpeaker(this);
        System.out.println("Current speaker set");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // IMPORTANT: Clear the speaker reference when this Activity is no longer active.
        // This prevents memory leaks and ensures AIManager doesn't try to update a destroyed UI.
        aiManager.clearCurrentSpeaker();
        aiManager.cancelListening(); // Cancel any ongoing speech recognition
        System.out.println("Current speaker cleared and listening cancelled.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Do NOT call aiManager.shutdown() here if other activities might still use it.
        // AIManager's lifecycle is tied to the application context.
        // Only call shutdown() when the entire application process is truly ending.
        // For a simple app, you might add a clean shutdown hook if appropriate.
    }
}