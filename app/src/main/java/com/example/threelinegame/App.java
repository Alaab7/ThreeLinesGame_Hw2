package com.example.threelinegame;

import android.app.Application;

import com.google.gson.Gson;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MSPV3.initHelper(this);
        String js = MSPV3.getMe().getString("MY_DB", "");
        dataBase md = new Gson().fromJson(js, dataBase.class);

        if (md == null) {
            dataBase myDB = new dataBase();
            String json = new Gson().toJson(myDB);
            MSPV3.getMe().putString("MY_DB", json);
        }

    }
}
