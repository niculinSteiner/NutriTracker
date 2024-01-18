package com.niculin.nutritracker;

import static com.niculin.nutritracker.activities.TrackActivity.START_GOAL_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.niculin.nutritracker.activities.TrackActivity;
import com.niculin.nutritracker.services.BackgroundService;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);


        Intent intent = new Intent(MainActivity.this, BackgroundService.class);
        PendingIntent startServicePendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 30);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60, startServicePendingIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button saveButton = (Button)findViewById(R.id.saveGoalButton);
        saveButton.setOnClickListener(v -> {
            EditText goal = (EditText) findViewById(R.id.startGoalField);
            Intent intentToTrackActivity = new Intent(this, TrackActivity.class);
            intentToTrackActivity.putExtra(START_GOAL_KEY, goal.getText().toString());
            this.startActivity(intentToTrackActivity);
        });
    }

}