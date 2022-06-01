package com.example.memorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private final String PREFS_NAME = "firstTimePref";
    private Button enter_button;
    private TextInputLayout pin_text_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Determining if app is being used for the first time:
        SharedPreferences first_time = getSharedPreferences(PREFS_NAME, 0);

        if(first_time.getBoolean("first_time", true) || first_time.getString("general_pass", "None").equals("None")){
            setContentView(R.layout.activity_main);
            first_time.edit().putBoolean("first_time", false).apply();
            pin_text_box = findViewById(R.id.pinFirstTimeTextInputLayout);
            enter_button = findViewById(R.id.enterButtonFirstTime);

        }else{
            setContentView(R.layout.usual_login);
            pin_text_box = findViewById(R.id.pinTextInputLayout);
            enter_button = findViewById(R.id.enterButton);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
    }

    public void onEnterButtonPressed(View view){
        SharedPreferences first_time = getSharedPreferences(PREFS_NAME, 0);

        if(first_time.getBoolean("first_time", true) || first_time.getString("general_pass", "None").equals("None")){

            if(!pin_text_box.getEditText().getText().toString().isEmpty()){
                first_time.edit().putString("general_pass", pin_text_box.getEditText().getText().toString()).apply();
                Intent intent = new Intent(getApplicationContext(), MemoListView.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Please Enter A Pin", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(!pin_text_box.getEditText().getText().toString().isEmpty()){
                String password = first_time.getString("general_pass", "None");
                if(pin_text_box.getEditText().getText().toString().equals(password)){
                    Intent intent = new Intent(getApplicationContext(), MemoListView.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this, "You have entered a wrong pin", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please Enter A Pin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onForgetButtonPressed(View view){
        SharedPreferences first_time = getSharedPreferences(PREFS_NAME, 0);
        String email = first_time.getString("email", "None");
    }
}