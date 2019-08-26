package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tonylau.foodorderapp.DB.OrderDAO;
import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.GlobalFunc;
import com.tonylau.foodorderapp.R;
import com.tonylau.foodorderapp.Services.DataService;
import com.tonylau.foodorderapp.Services.MessageService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                        GlobalData.fcmToken = token;
                        // Log and toast
                        String msg = token;
                        Log.d(TAG, "Firebase ID : " + msg);
                    }
                });

        SharedPreferences sp = getSharedPreferences(GlobalData.PREF_SETTING, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        Date date = new Date();
        String today = sdf.format(date);
        String settingDay = sp.getString(GlobalData.PREF_KEY_DATE, today);
        if(!today.equals(settingDay)) {
            Log.d(TAG, "New Day Order reset.");
            OrderDAO orderDAO = new OrderDAO(this);
            orderDAO.deleteAll();
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(GlobalData.PREF_KEY_DATE, today);
        editor.apply();
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
