package com.tonylau.foodorderapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tonylau.foodorderapp.Object.Item;
import com.tonylau.foodorderapp.Object.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private static final String TAG = "CartDAO";
    private MyDbHelper dbHelper = null;

    public CartDAO(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    public void insert(OrderItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID, item.itemId);
        cv.put(CartDbContract.CartDbEntry.COLUMN_NAME_QUANTITY, item.quantity);
        long success = db.insertWithOnConflict(CartDbContract.CartDbEntry.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        if (success == -1) {
            String sql = "UPDATE " + CartDbContract.CartDbEntry.TABLE_NAME +
                    " SET QUANTITY=QUANTITY+1 WHERE " + CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID + "=?";
            db.execSQL(sql, new String[]{String.valueOf(item.itemId)});
        }
        db.close();
        Log.d(TAG, "CartItem " + item.itemId + " inserted / updated.");
    }

    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CartDbContract.CartDbEntry.TABLE_NAME, CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID + "=?", new String[]{String.valueOf(id)});
        db.close();
        Log.d(TAG, "CartItem " + id + " deleted.");
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CartDbContract.CartDbEntry.TABLE_NAME, null, null);
//        Log.d(TAG, "Item " + id + " deleted.");
        db.close();
    }

    public OrderItem get(long id, SQLiteDatabase db) {
        Cursor c2 = db.rawQuery(
                "SELECT * FROM " + CartDbContract.CartDbEntry.TABLE_NAME + " WHERE " +
                        CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID + "=?", new String[]{String.valueOf(id)});
        if(c2.moveToNext()) {
            OrderItem item = new OrderItem();
            item.itemId = c2.getInt(c2.getColumnIndex(CartDbContract.CartDbEntry.COLUMN_NAME_ITEMID));
            item.quantity = c2.getInt(c2.getColumnIndex(CartDbContract.CartDbEntry.COLUMN_NAME_QUANTITY));
            return item;
        } else {
            return null;
        }

    }

    public List<OrderItem> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<OrderItem> result = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(
                    "SELECT * FROM " + CartDbContract.CartDbEntry.TABLE_NAME + " a INNER JOIN " +
                            ItemDbContract.ItemDbEntry.TABLE_NAME + " b ON " +
                            "a.ITEMID=b.ITEMID", null);
            while (c.moveToNext()) {
                Item info = new Item();
                info.itemId = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID));
                info.name = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME));
                info.category = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY));
                info.price = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE));
                info.remain = c.getInt(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN));
                info.imgPath = c.getString(c.getColumnIndex(
                        ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH));
                OrderItem orderItem = new OrderItem();
                orderItem.itemId = info.itemId;
                orderItem.quantity = c.getInt(c.getColumnIndex(
                        CartDbContract.CartDbEntry.COLUMN_NAME_QUANTITY));
                orderItem.itemInfo = info;
                result.add(orderItem);
            }
            c.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        db.close();
        return result;
    }

}
