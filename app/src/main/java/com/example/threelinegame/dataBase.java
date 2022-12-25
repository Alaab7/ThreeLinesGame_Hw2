package com.example.threelinegame;

import java.util.ArrayList;

public class dataBase {

    private ArrayList<Score> scores;

    public dataBase() { }

    public ArrayList<Score> getRecords() {
        if(scores == null){
            scores = new ArrayList<>();
        }
        return scores;
    }


}
