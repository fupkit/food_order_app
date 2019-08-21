package com.tonylau.foodorderapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemDbContract.ItemDbEntry.TABLE_NAME + " (" +
                    ItemDbContract.ItemDbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID + " INTEGER, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME + " TEXT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE + " INTEGER, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN + " INTEGER, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH + " TEXT, " +
                    ItemDbContract.ItemDbEntry.COLUMN_NAME_FINISHED + " INTEGER DEFAULT 0)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemDbContract.ItemDbEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ItemDb.db";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}