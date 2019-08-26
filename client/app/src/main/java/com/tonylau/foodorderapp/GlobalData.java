package com.tonylau.foodorderapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.tonylau.foodorderapp.Object.Menu;

import java.util.UUID;

public class GlobalData {
    public static final String PREF_SETTING = "FoorOrderAppSetting";
    public static final String PREF_KEY_URL = "URL";

    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";

    public static Menu menu = null;

    public static String uniqueID = null;
    public static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";


}
