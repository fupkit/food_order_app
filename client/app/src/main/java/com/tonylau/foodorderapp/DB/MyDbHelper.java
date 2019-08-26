package com.tonylau.foodorderapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String ITEM_CREATE_ENTRIES =
            "CREATE TABLE " + ItemDbContract.ItemDbEntry.TABLE_NAME + " (" +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID + " INTEGER PRIMARY KEY, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME + " TEXT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE + " INTEGER, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN + " INTEGER, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH + " TEXT);";
    private static final String ORDER_CREATE_ENTRIES =
            "CREATE TABLE " + OrderDbContract.OrderDbEntry.TABLE_NAME + " (" +
                    OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID + " INTEGER, " +
                    OrderDbContract.OrderDbEntry.COLUMN_NAME_ITEMID + " INTEGER, " +
                    OrderDbContract.OrderDbEntry.COLUMN_NAME_QUANTITY + " INTEGER, " +
                    OrderDbContract.OrderDbEntry.COLUMN_NAME_DONE + " INTEGER);";
    private static final String CART_CREATE_ENTRIES =
            "CREATE TABLE " + CartDbContract.CartDbEntry.TABLE_NAME + " (" +
                    CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID + " INTEGER PRIMARY KEY, " +
                    CartDbContract.CartDbEntry.COLUMN_NAME_QUANTITY + " INTEGER);";


    private static final String ITEM_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemDbContract.ItemDbEntry.TABLE_NAME + ";";
    private static final String ORDER_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + OrderDbContract.OrderDbEntry.TABLE_NAME + ";";
    private static final String CART_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CartDbContract.CartDbEntry.TABLE_NAME + ";";

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FoodOrderDB.db";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEM_CREATE_ENTRIES);
        db.execSQL(ORDER_CREATE_ENTRIES);
        db.execSQL(CART_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ITEM_DELETE_ENTRIES);
        db.execSQL(ORDER_DELETE_ENTRIES);
        db.execSQL(CART_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}