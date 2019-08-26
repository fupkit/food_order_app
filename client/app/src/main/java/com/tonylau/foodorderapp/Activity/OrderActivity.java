package com.tonylau.foodorderapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.badge.BadgeUtils;
import com.tonylau.foodorderapp.DB.CartDAO;
import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.MenuAdapter;
import com.tonylau.foodorderapp.Object.OrderItem;
import com.tonylau.foodorderapp.R;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    Set<String> category;
    RecyclerView rv;
    MenuAdapter adapter;
    //    Spinner spCat;
    ImageButton ibCart;
    DataServiceReceiver receiver;
    IntentFilter filter;
    TextView tvCartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


//        spCat = findViewById(R.id.spCat);
        ArrayList<String> cats = new ArrayList<>();
        for (int i = 0; i < GlobalData.menu.items.size(); i++) {
            cats.add(GlobalData.menu.items.get(i).category);
        }
        category = new HashSet<>(cats);
        category.add("ALL");
        cats = new ArrayList<>(category);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cats);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCat.setAdapter(dataAdapter);
        //TODO spinner filtering


        rv = findViewById(R.id.rvMenu);
        GridLayoutManager glm = new GridLayoutManager(this, 1);
        rv.setLayoutManager(glm);
        adapter = new MenuAdapter(this, GlobalData.menu.items);
        rv.setAdapter(adapter);

        ibCart = findViewById(R.id.ibCart);
        ibCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        tvCartBadge = findViewById(R.id.tvCartBadge);
        setQuantity();
    }

    private class DataServiceReceiver extends BroadcastReceiver {
        public DataServiceReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Menu data changed.");
            adapter.setData(GlobalData.menu.items);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setQuantity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new DataServiceReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.data");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
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
    public void setQuantity() {
        long quantity = 0;
        CartDAO cartDAO = new CartDAO(this);
        for (OrderItem item : cartDAO.getAll()) {
            quantity += item.quantity;
        }
        tvCartBadge.setText(String.valueOf(quantity));
    }
}


