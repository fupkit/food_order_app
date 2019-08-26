package com.tonylau.foodorderapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tonylau.foodorderapp.Object.Item;
import com.tonylau.foodorderapp.Object.Order;
import com.tonylau.foodorderapp.Object.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String TAG = "OrderDAO";
    private MyDbHelper dbHelper = null;

    public OrderDAO(Context context) {
        dbHelper = new MyDbHelper(context);
    }

    public void insert(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (OrderItem item : order.orderItems) {
            ContentValues values = new ContentValues();
            values.put(OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID, order.orderId);
            values.put(OrderDbContract.OrderDbEntry.COLUMN_NAME_ITEMID, item.itemId);
            values.put(OrderDbContract.OrderDbEntry.COLUMN_NAME_QUANTITY, item.quantity);
            values.put(OrderDbContract.OrderDbEntry.COLUMN_NAME_DONE, order.done);
            db.insert(OrderDbContract.OrderDbEntry.TABLE_NAME, null, values);
        }
    }

    public int update(long orderId, Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(OrderDbContract.OrderDbEntry.COLUMN_NAME_DONE, order.done);
        return db.update(OrderDbContract.OrderDbEntry.TABLE_NAME, cv, OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID + "=?", new String[]{String.valueOf(orderId)});
    }

    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OrderDbContract.OrderDbEntry.TABLE_NAME, OrderDbContract.OrderDbEntry.COLUMN_NAME_ITEMID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(OrderDbContract.OrderDbEntry.TABLE_NAME, null, null);
        db.close();
    }

    public List<Order> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Order> result = new ArrayList<>();
        try {
            Cursor c = db.rawQuery(
                    "SELECT distinct " + OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID + " FROM " + OrderDbContract.OrderDbEntry.TABLE_NAME, null);
            while (c.moveToNext()) {
                long orderId = c.getInt(c.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID));
                Order order = get(orderId, db);
                result.add(order);
            }
            c.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        db.close();
        return result;
    }

    public Order get(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Order order = new Order();
        order.orderId = id;
        try {
            Cursor c2 = db.rawQuery(
                    "SELECT * FROM " + OrderDbContract.OrderDbEntry.TABLE_NAME + " WHERE " + OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID + " = " + id, null);
            while (c2.moveToNext()) {
                OrderItem item = new OrderItem();
                item.itemId = c2.getInt(c2.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_ITEMID));
                item.quantity = c2.getInt(c2.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_QUANTITY));
                order.orderItems.add(item);
            }
            c2.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        db.close();
        return order;
    }

    public Order get(long id, SQLiteDatabase db) {
        Order order = new Order();
        order.orderId = id;
        try {
            Cursor c2 = db.rawQuery(
                    "SELECT * FROM " + OrderDbContract.OrderDbEntry.TABLE_NAME + " a INNER JOIN " +
                            ItemDbContract.ItemDbEntry.TABLE_NAME + " b ON " +
                            "a.ITEMID=b.ITEMID"
                            + " WHERE " + OrderDbContract.OrderDbEntry.COLUMN_NAME_ORDERID + " = " + id, null);

            while (c2.moveToNext()) {
                OrderItem item = new OrderItem();
                item.itemId = c2.getInt(c2.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_ITEMID));
                item.quantity = c2.getInt(c2.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_QUANTITY));
                item.itemInfo = new Item();
                item.itemInfo.itemId = c2.getInt(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_ITEMID));
                item.itemInfo.name = c2.getString(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_NAME));
                item.itemInfo.category = c2.getString(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_CATEGORY));
                item.itemInfo.price = c2.getInt(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_PRICE));
                item.itemInfo.remain = c2.getInt(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_REMAIN));
                item.itemInfo.imgPath = c2.getString(c2.getColumnIndex(ItemDbContract.ItemDbEntry.COLUMN_NAME_IMGPATH));
                order.orderItems.add(item);
                order.done = c2.getInt(c2.getColumnIndex(OrderDbContract.OrderDbEntry.COLUMN_NAME_DONE)) == 1;
            }
            c2.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        return order;
    }
}
