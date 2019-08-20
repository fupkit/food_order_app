package com.tonylau.foodorderapp;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

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
}
