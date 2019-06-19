package com.cara2v.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cara2v.util.PreferenceConnector;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLogIn= PreferenceConnector.readBoolean(SplashActivity.this,PreferenceConnector.IsLogIN,false);
        if (isLogIn){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }
        finish();
    }
}
