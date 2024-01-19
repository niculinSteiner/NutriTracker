package com.niculin.nutritracker;

import static com.niculin.nutritracker.activities.TrackActivity.START_GOAL_KEY;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.niculin.nutritracker.activities.TrackActivity;
import com.niculin.nutritracker.services.BackgroundService;

public class MainActivity extends AppCompatActivity {

    public static boolean IS_FIRST_TIME = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFirstTime()) {
            setContentView(R.layout.activity_main);
            setIsFirstTimeToFalse();
        } else {
            AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BackgroundService.class);
            PendingIntent startServicePendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 1000 * 60, 1000 * 60, startServicePendingIntent);

            intentToTrackActivity("");
        }
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getSharedPreferences("isFirstTimeState", Context.MODE_PRIVATE);
        return preferences.getBoolean("isFirstTime", true);
    }

    void setIsFirstTimeToFalse() {
        SharedPreferences preferences = getSharedPreferences("isFirstTimeState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
        IS_FIRST_TIME = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button saveButton = (Button) findViewById(R.id.saveGoalButton);
        saveButton.setOnClickListener(v -> {
            EditText goal = (EditText) findViewById(R.id.startGoalField);
            intentToTrackActivity(goal.getText().toString());
        });
    }

    private void intentToTrackActivity(String goal) {
        Intent intentToTrackActivity = new Intent(this, TrackActivity.class);
        intentToTrackActivity.putExtra(START_GOAL_KEY, goal);
        this.startActivity(intentToTrackActivity);
    }

}