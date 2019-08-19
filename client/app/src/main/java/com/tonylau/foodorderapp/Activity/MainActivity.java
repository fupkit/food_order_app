package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tonylau.foodorderapp.R;
import com.tonylau.foodorderapp.Services.DataService;
import com.tonylau.foodorderapp.Services.MessageService;

public class MainActivity extends AppCompatActivity {
    TextView btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnter = findViewById(R.id.tvEnter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.settings):
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
        }
        return true;
    }

    private void startService() {
        Intent intent1 = new Intent(MainActivity.this, MessageService.class);
        startService(intent1);

        Intent intent2 = new Intent(MainActivity.this, DataService.class);
        startService(intent2);
    }
}
