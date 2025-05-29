package com.example.healthhub;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthhub.Adapters.UserMedicationReminder_RecyclerViewAdapter;
import com.example.healthhub.DAO.Medication;
import com.example.healthhub.Utils.Utils;
import com.example.healthhub.AI.VoiceRecognitionListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class UserMedicationReminder extends AppCompatActivity implements VoiceRecognitionListener.RecognitionCallback{
    ArrayList<Medication> medications;
    ArrayList<String> medicationsNames,medicationsTimes;
    Button backBtn, voiceAssistantBtn;
    RecyclerView recyclerView;
    TextView currentTime,currentTimeAMPM;
    UserMedicationReminder_RecyclerViewAdapter adapter;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private VoiceRecognitionListener voiceRecognitionListener; // Instance of our external listener

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 500;
    private final String[] permissions = {android.Manifest.permission.RECORD_AUDIO};
    // For displaying current time
    private final Handler handler = new Handler();
    private Runnable runnable;
//    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_medication_reminder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medications = fetchTodaysMedications();
        if(medications!=null && !medications.isEmpty()){
            medications.forEach(System.out::println);//TODO: Remove
            medicationsNames = getMedicationsNames(medications);
            medicationsTimes = getMedicationsTimes(medications);
            adapter = new UserMedicationReminder_RecyclerViewAdapter(this,medicationsNames,medicationsTimes);
            recyclerView = findViewById(R.id.todays_medications_slots_recycler_view);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            //Clean the recycler view
            LinearLayout medSlotsLayout = findViewById(R.id.todays_medications_slots);
            medSlotsLayout.removeAllViews();
            //Create a new text view
            TextView noMedicationsTextView = new TextView(this);
            noMedicationsTextView.setText(R.string.no_medications_today);
            noMedicationsTextView.setTextSize(20);
            noMedicationsTextView.setTextColor(getResources().getColor(R.color.black));
            Typeface typeface = ResourcesCompat.getFont(this, R.font.inter_medium);
            noMedicationsTextView.setTypeface(typeface);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            noMedicationsTextView.setLayoutParams(params);
            //Add the text view to the layout
            medSlotsLayout.addView(noMedicationsTextView);
        }

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        currentTime = findViewById(R.id.current_time);
        currentTimeAMPM = findViewById(R.id.current_time_ap_pm);
        runnable = new Runnable() {
            @Override
            public void run() {
                currentTime.setText(Utils.getCurrentTimeString());
                currentTimeAMPM.setText(Utils.getCurrentTimeAMPM());
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

//        UserMedicationAlarmPermissionAssistant.requestPermissionsIfNeeded(this);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 2025);
//        calendar.set(Calendar.MONTH, Calendar.MAY);      // 0-based: Jan = 0
//        calendar.set(Calendar.DAY_OF_MONTH, 25);
//        calendar.set(Calendar.HOUR_OF_DAY, 23);
//        calendar.set(Calendar.MINUTE, 45);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        UserMedicationScheduler.scheduleAlarm(this, calendar.getTimeInMillis());
        voiceAssistantBtn = findViewById(R.id.voice_assistant_button);
        voiceAssistantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListening();
            }
        });

    }
    private void startListening(){
        requestAudioPermission();
        initializeSpeechRecognizer();
        if (speechRecognizer != null) {
            speechRecognizer.startListening(speechRecognizerIntent);
        }else{
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeSpeechRecognizer() {
        voiceRecognitionListener = new VoiceRecognitionListener(this);
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(voiceRecognitionListener); // Set our custom listener

            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "el-GR");
//            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        } else {
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_LONG).show();
            System.out.println("Speech recognition is not available.");
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
    private ArrayList<Medication> fetchMedications(){
        return Utils.medicationDAO.findMedicationsByUsedID(Utils.getStoredUserId(getApplicationContext()));
    }
    private ArrayList<Medication> fetchTodaysMedications(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Utils.medicationDAO.findMedicationsByUserIDAndDate(Utils.getStoredUserId(getApplicationContext()),LocalDate.now());
        }
        return null;
    }
    private ArrayList<String> getMedicationsNames(ArrayList<Medication> medications){
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i <medications.size() ; i++) {
            for (int j = 0; j <medications.get(i).getTime().size() ; j++) {
                names.add(medications.get(i).getName());
            }
        }
        return names;
    }
    private ArrayList<String> getMedicationsTimes(ArrayList<Medication> medications){
        ArrayList<String> times = new ArrayList<>();
        for (int i = 0; i <medications.size() ; i++) {
            times.addAll(medications.get(i).getTime());
        }
        return times;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onSpeechRecognized(String recognizedText) {
        System.out.println("Reminder Activity: "+recognizedText);
    }

    @Override
    public void onRecognitionError(String errorMessage) {
        System.out.println("Reminder Activity: "+errorMessage);
    }

    @Override
    public void onListeningStatusChange(String status) {
        System.out.println("Reminder Activity: "+status);
    }
}