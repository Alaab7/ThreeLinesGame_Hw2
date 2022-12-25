package com.example.threelinegame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Activity_Scores extends AppCompatActivity  {

    private  Button[] players;
    private ArrayList<Score> scores;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        findViews();
        addPlayer();
        SupportMapFragment map= SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameMap, map)
                .commit();
    }
    public void findViews(){
        players = new Button[] {
                findViewById(R.id.player_1_button),
                findViewById(R.id.player_2_button),
                findViewById(R.id.player_3_button),
                findViewById(R.id.player_4_button),
                findViewById(R.id.player_5_button),
                findViewById(R.id.player_6_button),
                findViewById(R.id.player_7_button),
                findViewById(R.id.player_8_button),
                findViewById(R.id.player_9_button),
                findViewById(R.id.player_10_button),
        };
    }
    private void addPlayer() {
        String js = MSPV3.getMe().getString("MY_DB", "");
        dataBase DB= new Gson().fromJson(js, dataBase.class);
        scores = DB.getRecords();


            for (int i = 0; i < scores.size(); i++) {
                players[i].setText("Name: "+scores.get(i).getName()+ " Score: "+scores.get(i).getScore());


            }


    }


}