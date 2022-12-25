package com.example.threelinegame;

public class Score {
    private String playerName;
    private int score = 0;
    private double lat = 0.0;
    private double lon = 0.0;

    public Score() { }

    public String getName() {
        return playerName;
    }

    public Score setName(String name) {
        this.playerName = name;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Score setScore(int score) {
        this.score = score;
        return this;
    }

}
