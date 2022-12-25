package com.example.threelinegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Activity_Menu extends AppCompatActivity {
    Button button_0;
    Button button_2;
    Button button_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        button_0 = findViewById(R.id.button_0);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        clicked();

    }

    private void clicked() {
        button_2.setOnClickListener(view -> moveToGame(false));
        button_0.setOnClickListener(view -> moveToGame(true));
        button_3.setOnClickListener(view -> {
                 Intent ScoreIntent = new Intent(this,Activity_Scores.class);
                startActivity(ScoreIntent);


        });
    }
    public void moveToGame(boolean sensorMode) {
        Intent gameIntent = new Intent(this, MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(MainActivity.SMode, sensorMode);

        gameIntent.putExtras(bundle);
        startActivity(gameIntent);
    }
}


