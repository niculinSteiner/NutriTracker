package com.niculin.nutritracker;

import static com.niculin.nutritracker.RecipeActivity.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TrackActivity extends AppCompatActivity {

    public static final String START_GOAL_KEY = "startGoal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setGoal();
        setActionListenerToAddCalories();
        setActionListenerToResetCalories();
        setActionListenerToEditCalorieGoal();
        Button generateRecipes = findViewById(R.id.generateRecipesButton);
        generateRecipes.setOnClickListener(v -> {
            TextView goal = (TextView) findViewById(R.id.goalTextView);
            TextView caloriesSoFar = (TextView) findViewById(R.id.alreadyConsumedTextView);
            Intent intentToRecipes = new Intent(this, RecipeActivity.class);
            intentToRecipes.putExtra(START_GOAL_KEY, goal.getText().toString());
            intentToRecipes.putExtra(ACTUAL_CALORIES_KEY, caloriesSoFar.getText().toString());
            this.startActivity(intentToRecipes);
        });
    }

    private void setActionListenerToEditCalorieGoal() {
        Button changeGoalButton = (Button) findViewById(R.id.changeGoalButton);
        changeGoalButton.setOnClickListener(v -> {
            TextView goal = findViewById(R.id.goalTextView);
            goal.setText(((EditText) findViewById(R.id.editCalorieGoalField)).getText());
        });
    }

    private void setActionListenerToResetCalories() {
        Button resetCaloriesButton = (Button) findViewById(R.id.resetCaloriesButton);
        resetCaloriesButton.setOnClickListener(v -> {
            TextView caloriesSoFar = findViewById(R.id.alreadyConsumedTextView);
            caloriesSoFar.setText("0");
        });
    }

    private void setActionListenerToAddCalories() {
        Button addCaloriesButton = findViewById(R.id.addCaloriesButton);
        addCaloriesButton.setOnClickListener(v -> {
            EditText caloriesToAdd = (EditText) findViewById(R.id.addCaloriesField);
            int caloriesToAddAsInt = Integer.parseInt(caloriesToAdd.getText().toString());
            TextView caloriesScore = (TextView) findViewById(R.id.alreadyConsumedTextView);
            if (caloriesScore.getText().toString().isEmpty()){
                caloriesScore.setText(caloriesToAdd.getText());
            } else {
                int caloriesSoFar = Integer.parseInt(caloriesScore.getText().toString());
                caloriesScore.setText(String.valueOf(caloriesSoFar + caloriesToAddAsInt));
            }
        });
    }


    protected void setGoal() {
        Intent intent = getIntent();
        int goal = Integer.parseInt(intent.getExtras().getString(START_GOAL_KEY));
        TextView view = findViewById(R.id.goalTextView);
        view.setText(String.valueOf(goal));
    }
}