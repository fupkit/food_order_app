package com.tonylau.foodorderapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class GlobalFunc {
    public static final String TAG = "GlobalFunc";

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            Log.e(TAG, "sleep error", e);
            e.printStackTrace();
        }
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

    public synchronized static String deviceId(Context context) {
        if (GlobalData.uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    GlobalData.PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            GlobalData.uniqueID = sharedPrefs.getString(GlobalData.PREF_UNIQUE_ID, null);
            if (GlobalData.uniqueID == null) {
                GlobalData.uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(GlobalData.PREF_UNIQUE_ID, GlobalData.uniqueID);
                editor.commit();
            }
        }
        return GlobalData.uniqueID;
    }

    public static void showDialog(Context context, String message, String buttonText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNeutralButton(buttonText, listener);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
