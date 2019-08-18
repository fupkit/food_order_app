package com.tonylau.foodorderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        finish();
    }
}
