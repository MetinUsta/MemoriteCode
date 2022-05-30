package com.example.memorite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final String PREFS_NAME = "firstTimePref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Determining if app is being used for the first time:
        SharedPreferences firstTime = getSharedPreferences(PREFS_NAME, 0);

        if(firstTime.getBoolean("first_time", true)){
            setContentView(R.layout.activity_main);
        }else{
//            TODO
        }
    }
}