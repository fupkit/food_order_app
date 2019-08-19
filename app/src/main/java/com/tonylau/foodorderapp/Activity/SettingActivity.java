package com.tonylau.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.R;

public class SettingActivity extends AppCompatActivity {
    SharedPreferences sp;
    EditText etURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onPostResume() {
        sp = getSharedPreferences(GlobalData.PREF_SETTING, 0);
        String url = sp.getString(GlobalData.PREF_KEY_URL, "http://192.168.0.103");
        etURL = findViewById(R.id.etURL);
        etURL.setText(url);
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(GlobalData.PREF_KEY_URL, etURL.getText().toString());
        editor.apply();
        super.onPause();
    }
}
