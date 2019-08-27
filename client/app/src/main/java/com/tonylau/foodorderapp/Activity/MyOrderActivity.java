package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tonylau.foodorderapp.CartAdapter;
import com.tonylau.foodorderapp.DB.OrderDAO;
import com.tonylau.foodorderapp.Object.Order;
import com.tonylau.foodorderapp.Object.OrderItem;
import com.tonylau.foodorderapp.OrderAdapter;
import com.tonylau.foodorderapp.R;

import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private static final String TAG = "MyOrderActivity";
    private OrderDAO orderDAO;
    private OrderAdapter adapter;
    private ExpandableListView elvOrder;
    private List<Order> orders;
    private TextView tvTotal;
    private MessageServiceReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        orderDAO = new OrderDAO(this);

        tvTotal = findViewById(R.id.tvTotal);
        setTotal();
        elvOrder = findViewById(R.id.elvOrder);
        adapter = new OrderAdapter(this, orders);
        elvOrder.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.cart):
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public void setTotal() {
        orders = orderDAO.getAll();
        int total = 0;
        for(Order order : orders) {
            for (OrderItem item : order.orderItems) {
                total += item.itemInfo.price * item.quantity;
            }
        }
        tvTotal.setText(String.valueOf(total));
    }

    private class MessageServiceReceiver extends BroadcastReceiver {
        public MessageServiceReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "My order data changed.");
            orders = orderDAO.getAll();
            adapter.setData(orders);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new MessageServiceReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.order.finished");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
