package com.tonylau.foodorderapp;

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
}
