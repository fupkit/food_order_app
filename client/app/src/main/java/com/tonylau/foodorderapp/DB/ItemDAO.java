package com.tonylau.foodorderapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tonylau.foodorderapp.Object.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private static final String TAG = "ItemDAO";
    private MyDbHelper dbHelper = null;

    public ItemDAO(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    public long insert(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = setValues(item);
        long newRowId;
        newRowId = db.insertWithOnConflict(ItemDbContract.ItemDbEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
//        Log.d(TAG, "Item " + item.itemId + " inserted / updated.");
        return newRowId;
    }

    public void update(long id, Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(ItemDbContract.ItemDbEntry.TABLE_NAME, setValues(item), ItemDbContract.ItemDbEntry._ID + " = ?", new String[]{String.valueOf(item.itemId)});
//        Log.d(TAG, "Item " + item.itemId + " updated.");
        db.close();
    }

    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ItemDbContract.ItemDbEntry.TABLE_NAME, ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID + " = ?", new String[]{String.valueOf(id)});
//        Log.d(TAG, "Item " + id + " deleted.");
        db.close();
    }

    public List<Item> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Item> result = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(
                    "SELECT * FROM " + ItemDbContract.ItemDbEntry.TABLE_NAME, null);
            while (c.moveToNext()) {
                Item item = new Item();
                item.itemId = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID));
                item.name = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME));
                item.category = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY));
                item.price = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE));
                item.remain = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN));
                item.imgPath = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH));
                result.add(item);
            }
            c.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        db.close();
        return result;
    }

    private ContentValues setValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID, item.itemId);
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME, item.name);
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY, item.category);
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE, item.price);
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN, item.remain);
        values.put(ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH, item.imgPath);
        return values;
    }
}
