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

import com.google.gson.Gson;
import com.tonylau.foodorderapp.DB.ItemDAO;
import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.GlobalFunc;
import com.tonylau.foodorderapp.Object.Item;
import com.tonylau.foodorderapp.Object.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataService extends Service {
    private static final String TAG = "DataService";
    private ScheduledThreadPoolExecutor exec;
    private DataServiceTask dst;
    private ItemDAO itemDAO;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "DataService starting. ");
        exec = new ScheduledThreadPoolExecutor(1);
        dst = new DataServiceTask();
        long period = 10; // the period between successive executions
        exec.scheduleAtFixedRate(dst, 0, period, TimeUnit.SECONDS);
        long delay = 10; //the delay between the termination of one execution and the commencement of the next
        exec.scheduleWithFixedDelay(dst, 0, delay, TimeUnit.SECONDS);
        itemDAO = new ItemDAO(this);
        Log.d(TAG, "DataService started.");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(exec != null) {
            exec.shutdown();
            exec = null;
        }
    }

    private class DataServiceTask implements Runnable{

        @Override
        public void run() {
            try {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
//                    Log.v(TAG, "Network connected.");
                    SharedPreferences sp = getSharedPreferences(GlobalData.PREF_SETTING, 0);
                    String url = sp.getString(GlobalData.PREF_KEY_URL, "http://10.0.2.2:3000");
                    updateMenu(url);
                    //send broadcast
                    Intent intent = new  Intent();
                    intent.setAction("android.intent.action.data");
                    sendBroadcast(intent);
                } else {
                    Log.e(TAG, "Network not connected.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private void updateMenu(String myUrl) {
        InputStream is = null;
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

            Gson gson = new Gson();
            is = conn.getInputStream();
            Reader reader = new InputStreamReader(is, "UTF-8");
            Menu menu = gson.fromJson(reader, Menu.class);
//            Log.d(TAG, gson.toJson(menu));
            for(Item item : menu.items) {
                itemDAO.insert(item);
            }
            Menu m2 = new Menu();
            m2.items = itemDAO.getAll();
            GlobalData.menu = m2;
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



}
