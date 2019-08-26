package com.tonylau.foodorderapp;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tonylau.foodorderapp.Object.Item;
import com.tonylau.foodorderapp.Object.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpHelper {
    private static final String TAG = "HttpHelper";
    private Context context;
    private String path;
    String urlString;

    public HttpHelper(Context context, String path) throws Exception{
        this.context = context;
        this.path = path;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences sp = context.getSharedPreferences(GlobalData.PREF_SETTING, 0);
            this.urlString = sp.getString(GlobalData.PREF_KEY_URL, "http://10.0.2.2:3000") + this.path;
        } else {
            Log.e(TAG, "Network not available.");
            throw new NetworkErrorException("Network not connected!");
        }
    }

    public Object doGet(String classType){
        InputStream is = null;
        Reader reader = null;
        Object result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();

            is = conn.getInputStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            result = gson.fromJson(reader, getType(classType));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
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
        return result;
    }

    public Object doPost(String dataClass, Object data, String reusltClass){
        InputStream is = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        Reader reader = null;
        Object result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            os = conn.getOutputStream();
            osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            String out = gson.toJson(data, getType(dataClass));
            osw.write(out);
            osw.flush();
            osw.close();

            int response = conn.getResponseCode();
            is = conn.getInputStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            result = gson.fromJson(reader, getType(reusltClass));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return result;
    }

    private Type getType(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            TypeToken<?> typeToken = TypeToken.get(clazz);
            return typeToken.getType();
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unsupported type: " + name, e);
        }
    }
}
