package com.tonylau.foodorderapp.Activity;

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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.MenuAdapter;
import com.tonylau.foodorderapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    Set<String> category;
    RecyclerView rv;
    MenuAdapter adapter;
    Spinner spCat;
    DataServiceReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        spCat = findViewById(R.id.spCat);
        ArrayList<String> cats = new ArrayList<>();
        for(int i = 0; i< GlobalData.menu.items.size(); i++){
            cats.add(GlobalData.menu.items.get(i).category);
        }
        category = new HashSet<>(cats);
        category.add("ALL");
        cats = new ArrayList<>();
        cats.addAll(category);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cats);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCat.setAdapter(dataAdapter);
        //TODO spinner filtering
        rv = findViewById(R.id.rvMenu);
        GridLayoutManager glm = new GridLayoutManager(this, 1);
        rv.setLayoutManager(glm);
        adapter = new MenuAdapter(this, GlobalData.menu.items);
        rv.setAdapter(adapter);


        receiver = new DataServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.data");
        registerReceiver(receiver, filter);
    }

    private class DataServiceReceiver extends BroadcastReceiver
    {
        public DataServiceReceiver (){}

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Menu data changed.");
            adapter.notifyDataSetChanged();
        }
    }
}


