package com.tonylau.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.tonylau.foodorderapp.R;

public class MenuActivity extends AppCompatActivity {
LinearLayout llOrder;
LinearLayout llMyOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        llOrder = findViewById(R.id.llOrder);
        llMyOrder = findViewById(R.id.llMyOrder);

        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MyOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}
