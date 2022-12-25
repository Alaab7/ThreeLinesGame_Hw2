package com.example.threelinegame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;

public class Activity_onGameOver extends AppCompatActivity {

   private TextView activity_score_LBL_score;
   private Button save_button;
    private Button back_button;
    private EditText name_field;
    int score;
    dataBase DB ;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_game_over);
        score = getIntent().getExtras().getInt("score");
        findViews();
        activity_score_LBL_score.setText( "" +score);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        save_button.setOnClickListener(view -> {


            playerName = name_field.getText().toString();

                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

            name_field.setVisibility(View.INVISIBLE);
            save_button.setVisibility(View.INVISIBLE);

            saveRecord(playerName, score);
        });

    }

    public void findViews(){
        activity_score_LBL_score = findViewById(R.id.activity_score_LBL_score);
        save_button = findViewById(R.id.save_button);
        back_button = findViewById(R.id.back_button);
        name_field = findViewById(R.id.name_field);

    }
    private void saveRecord(String player_name, int score) {

        String js = MSPV3.getMe().getString("MY_DB", "");
        DB = new Gson().fromJson(js, dataBase.class);

        DB.getRecords().add(new Score()
                .setName(player_name)
                .setScore(score)

        );


        Collections.sort(DB.getRecords() , new SortByScore());

        String json = new Gson().toJson(DB);
        MSPV3.getMe().putString("MY_DB", json);
    }
}
class SortByScore implements Comparator<Score> {

    @Override
    public int compare(Score rec1, Score rec2) {

        return rec2.getScore() - rec1.getScore();
    }
}