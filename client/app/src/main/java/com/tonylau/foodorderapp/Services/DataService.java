package com.tonylau.foodorderapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.GlobalFunc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataService extends Service {
    private static final String TAG = "DataService";

    ConnectivityManager cm;
    NetworkInfo networkInfo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "DataService starting.");
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.v(TAG, "Network connected.");
            SharedPreferences sp = getSharedPreferences(GlobalData.PREF_SETTING, 0);
            String url = sp.getString(GlobalData.PREF_KEY_URL, "http://10.0.2.2:3000");
            DataServiceAsyncTask dataServiceAsyncTask = new DataServiceAsyncTask();
            dataServiceAsyncTask.execute(url);
        } else {
            Log.e(TAG, "Network not connected.");
        }
        Log.v(TAG, "DataService started.");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private class DataServiceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            updateMenu(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "updateMenu finished.");
            GlobalFunc.sleep(10000);
        }
    }

    private void updateMenu(String myUrl) {
        InputStream is = null;
        int len = 500;
        try {
            myUrl = myUrl + "/mobile_app/food_order_app/get_menu";
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = readIt(is, len);
            Log.d(TAG, contentAsString);

        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "", e);
                }
            }
        }
    }

    private String readIt(InputStream stream, int len)
            throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
