package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.GlobalFunc;
import com.tonylau.foodorderapp.R;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SplashAsyncTask splashAsyncTask = new SplashAsyncTask();
        splashAsyncTask.execute();

    }
    private class SplashAsyncTask  extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                if(GlobalData.menu != null && GlobalData.menu.items.size() > 0) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
