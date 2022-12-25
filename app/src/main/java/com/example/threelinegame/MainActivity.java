package com.example.threelinegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ExtendedFloatingActionButton main_BTN_right;
    private ExtendedFloatingActionButton main_BTN_left;
    private AppCompatImageView[][] rocks;
    private ShapeableImageView[] lives;
    private AppCompatImageView[] horses;
    GameManager gameManager;
    private int numOfLives = 3;
    private int numRows = 5;
    private int numCol =5;
    private Timer timer;
    int score =0 ;
    private static final int DELAY = 1000;
    private SensorManager sensorManager;
    private Sensor sensor;
    public  boolean sMode = false;
    public static final String SMode = "sMode";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMode=getIntent().getExtras().getBoolean(SMode);
        findViews();
        if(sMode) {
            initSensor();
        }else
        leftAndRight();
        gameManager = new GameManager(numOfLives, numRows, numCol);
        gameManager.initItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if(sMode){
            startSensor();
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateScreen());
                score+=10;
            }
        }, DELAY, DELAY);
    }

    private void updateScreen()
    {
        gameManager.updateTable();
        boolean isGameFinish = gameManager.isGameFinish();


        if (isGameFinish) {
            showLives();
            showRocks();
            showToast("Game Over");
            makeVibrate();
            Intent gameOverIntent = new Intent(MainActivity.this, Activity_onGameOver.class);
            Bundle bundle = new Bundle();
            bundle.putInt("score", score);
            gameOverIntent.putExtras(bundle);
            startActivity(gameOverIntent);
            finish();


        }/* else if(isCoinHit)   {
            showLives();
                score += 50;
                showToast("ToSS THe Coin");
                gameManager.setIsCoin(false);

        }*/
        else
        {
            boolean isTouched = gameManager.isTouched();
            if (isTouched)
            {
                showLives();
                showToast("!Lost Life!");
                gameManager.setIsCrash(false);
                makeVibrate();
            }
            boolean isCoinHit = gameManager.isCoinHit();
            if(isCoinHit)   {
                score += 50;
                 showToast("ToSS THe Coin");
                gameManager.setIsCoin(false);
            }
        }
        showRocks();
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    private void showRocks() {
        int[][] managerRocks = gameManager.getRocks();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCol; j++) {
                if (managerRocks[i][j] == 1) {
                    rocks[i][j].setImageResource(R.drawable.ic_rock);
                    rocks[i][j].setVisibility(View.VISIBLE);
                } else if(managerRocks[i][j] == 2) {
                    rocks[i][j].setImageResource(R.drawable.coin_ic);
                    rocks[i][j].setVisibility(View.VISIBLE);
                }
                else  {
                    rocks[i][j].setVisibility(View.GONE);
                }
            }
        }
    }

    private void showLives() {
        int[] managerLives = gameManager.getLives();
        for (int i = 0; i < gameManager.getLives().length; i++) {
            if (managerLives[i] == 1) {
                lives[i].setVisibility(View.VISIBLE);
            } else {
                lives[i].setVisibility(View.INVISIBLE);
            }
        }
    }
    private void makeVibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        if(sMode){
            stopSensor();
        }
    }

    public void leftAndRight() {

        main_BTN_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gameManager.moveHorse("right");
                showHorse();
            }
        });


        main_BTN_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.moveHorse("left");
                showHorse();
            }
        });

    }
    private void initSensor() {
        main_BTN_right.setVisibility(View.INVISIBLE);
        main_BTN_left.setVisibility(View.INVISIBLE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    private SensorEventListener sensorEventListener = new SensorEventListener(){

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            gameManager.sensorHorse(sensorEvent.values[0]);
            showHorse();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    public void startSensor() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void stopSensor() {
        sensorManager.unregisterListener(sensorEventListener);

    }

    private void showHorse() {
        int [] managerHorse = gameManager.getHorse();
        for (int i = 0; i < numCol; i++) {
            if(managerHorse[i] == 1){
                horses[i].setVisibility(View.VISIBLE);
            }else{
                horses[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        findRocks();
        findHearts();
        findHorses();


    }

    public void findHearts() {
        lives = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };


    }

    public void findHorses() {
        horses = new AppCompatImageView[]{
                findViewById(R.id.main_IC_horse_0),
                findViewById(R.id.main_IC_horse_1),
                findViewById(R.id.main_IC_horse_2),
                findViewById(R.id.main_IC_horse_3),
                findViewById(R.id.main_IC_horse_4)

        };

    }


    public void findRocks() {

        rocks = new AppCompatImageView[][]{
                {findViewById(R.id.main_IC_rock_00),
                        findViewById(R.id.main_IC_rock_01),
                        findViewById(R.id.main_IC_rock_02),
                        findViewById(R.id.main_IC_rock_03),
                        findViewById(R.id.main_IC_rock_04)

                },

                {findViewById(R.id.main_IC_rock_10),
                        findViewById(R.id.main_IC_rock_11),
                        findViewById(R.id.main_IC_rock_12),
                        findViewById(R.id.main_IC_rock_13),
                        findViewById(R.id.main_IC_rock_14)

                },


                {findViewById(R.id.main_IC_rock_20),
                        findViewById(R.id.main_IC_rock_21),
                        findViewById(R.id.main_IC_rock_22),
                        findViewById(R.id.main_IC_rock_23),
                        findViewById(R.id.main_IC_rock_24)},



                {findViewById(R.id.main_IC_rock_30),
                        findViewById(R.id.main_IC_rock_31),
                        findViewById(R.id.main_IC_rock_32),
                        findViewById(R.id.main_IC_rock_33),
                        findViewById(R.id.main_IC_rock_34)},


                {findViewById(R.id.main_IC_rock_40),
                        findViewById(R.id.main_IC_rock_41),
                        findViewById(R.id.main_IC_rock_42),
                        findViewById(R.id.main_IC_rock_43),
                        findViewById(R.id.main_IC_rock_44)}

        };
    }
}

