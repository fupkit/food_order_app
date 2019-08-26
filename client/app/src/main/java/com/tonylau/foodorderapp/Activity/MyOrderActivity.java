package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
    private OrderDAO orderDAO;
    private OrderAdapter adapter;
    private ExpandableListView elvOrder;
    private List<Order> orders;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        orderDAO = new OrderDAO(this);
        orders = orderDAO.getAll();
        elvOrder = findViewById(R.id.elvOrder);
        adapter = new OrderAdapter(this, orders);
        elvOrder.setAdapter(adapter);

        tvTotal = findViewById(R.id.tvTotal);
        setTotal();
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

    private void setTotal() {
        int total = 0;
        for(Order order : orders) {
            for (OrderItem item : order.orderItems) {
                total += item.itemInfo.price * item.quantity;
            }
        }
        tvTotal.setText(String.valueOf(total));
    }
}
