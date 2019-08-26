package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tonylau.foodorderapp.GlobalFunc;
import com.tonylau.foodorderapp.R;
import com.tonylau.foodorderapp.Services.DataService;
import com.tonylau.foodorderapp.Services.MessageService;

public class MainActivity extends AppCompatActivity {
    TextView btnEnter;
    private static final String TAG = "MainActivity";

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

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, "Firebase ID : " + msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        stopService(intent1);
        startService(intent1);

        Intent intent2 = new Intent(MainActivity.this, DataService.class);
        stopService(intent2);
        startService(intent2);
    }
}
