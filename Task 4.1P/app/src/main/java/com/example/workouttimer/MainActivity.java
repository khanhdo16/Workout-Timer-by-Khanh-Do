package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView timerTextView;
    TextView lastWorkoutTextView;
    EditText workoutNameEditText;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String runtime;
    String workout;
    int seconds = 0;
    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerTextView = findViewById(R.id.timerTextView);
        lastWorkoutTextView = findViewById(R.id.lastWorkoutTextView);
        workoutNameEditText = findViewById(R.id.workoutNameEditText);

        sharedPreferences = getSharedPreferences("com.example.workouttimer", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(savedInstanceState != null) {
            runtime = savedInstanceState.getString("RUNTIME");
            workout = savedInstanceState.getString("WORKOUT");
            seconds = savedInstanceState.getInt("SECONDS");
            running = savedInstanceState.getBoolean("RUNNING");
        }

        checkSharedPreferences();

        timer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("RUNTIME", runtime);
        outState.putString("WORKOUT", workout);
        outState.putInt("SECONDS", seconds);
        outState.putBoolean("RUNNING", running);
    }

    public void saveSharedPreferences() {
        editor.putString("LAST_WORKOUT", lastWorkoutTextView.getText().toString());
        editor.apply();
    }

    public void checkSharedPreferences() {
        lastWorkoutTextView.setText(sharedPreferences.getString("LAST_WORKOUT", "You spent 00:00:00 working out last time."));
    }

    private void timer() {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                runtime = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, secs);

                timerTextView.setText(runtime);

                if (running == true) seconds++;

                handler.postDelayed(this, 1000);
            }
        });
    }

    public void startClick(View view) {
        running = true;
    }

    public void pauseClick(View view) {
        running = false;
    }

    public void stopClick(View view) {
        running = false;
        workout = workoutNameEditText.getText().toString();

        if(workout.isEmpty())
            lastWorkoutTextView.setText("You spent " + runtime + " working out last time.");
        else
            lastWorkoutTextView.setText("You spent " + runtime + " on " + workout + " last time.");

        saveSharedPreferences();

        seconds = 0;
    }
}
