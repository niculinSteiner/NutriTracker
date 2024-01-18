package com.niculin.nutritracker;

import static com.niculin.nutritracker.activities.TrackActivity.START_GOAL_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.niculin.nutritracker.activities.TrackActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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