package com.tonylau.foodorderapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tonylau.foodorderapp.CartAdapter;
import com.tonylau.foodorderapp.DB.CartDAO;
import com.tonylau.foodorderapp.DB.OrderDAO;
import com.tonylau.foodorderapp.GlobalData;
import com.tonylau.foodorderapp.GlobalFunc;
import com.tonylau.foodorderapp.HttpHelper;
import com.tonylau.foodorderapp.Object.HttpOrder;
import com.tonylau.foodorderapp.Object.Order;
import com.tonylau.foodorderapp.Object.OrderItem;
import com.tonylau.foodorderapp.R;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    CartDAO cartDAO;
    List<OrderItem> items;
    RecyclerView rvCart;
    CartAdapter adapter;
    TextView tvTotal;
    CartDataReceiver receiver;
    IntentFilter filter;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        rvCart = findViewById(R.id.rvOrder);

        cartDAO = new CartDAO(this);
        items = cartDAO.getAll();

        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvCart.setLayoutManager(lm);
        adapter = new CartAdapter(this, items);
        rvCart.setAdapter(adapter);

        tvTotal = findViewById(R.id.tvTotal);
        setTotal();

        btnConfirm = findViewById(R.id.btnOcfm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderTask orderTask = new OrderTask();
                orderTask.execute();
            }
        });
    }

    private class OrderTask extends AsyncTask<Void, Void, Order> {

        @Override
        protected Order doInBackground(Void... voids) {
            Order order = new Order();
            try {
                HttpHelper http = new HttpHelper(CartActivity.this, "/mobile_app/food_order_app/post_order");
                HttpOrder httpOrder = new HttpOrder();
                httpOrder.fcmToken = GlobalData.fcmToken;
                order.orderItems = items;
                httpOrder.order = order;
                order = (Order) http.doPost(HttpOrder.class.getName(), httpOrder, Order.class.getName());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return order;
        }

        @Override
        protected void onPostExecute(final Order order) {
            super.onPostExecute(order);
            if (order.orderId == null) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                };
                GlobalFunc.showDialog(CartActivity.this, "Error on getting order", "OK", listener);
            }
            else if (order.orderId == -1) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                };
                GlobalFunc.showDialog(CartActivity.this, "Not enough in stock!", "OK", listener);
            }
            else {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OrderDAO orderDAO = new OrderDAO(CartActivity.this);
                        CartDAO cartDAO = new CartDAO(CartActivity.this);
                        order.orderItems = cartDAO.getAll();
                        orderDAO.insert(order);
                        cartDAO.deleteAll();
                        Intent intent = new Intent(CartActivity.this, MyOrderActivity.class);
                        startActivity(intent);
                    }
                };
                GlobalFunc.showDialog(CartActivity.this, "Order placed! Order id: " +order.orderId, "OK", listener);
            }
        }
    }

    private class CartDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            dataChanged();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        dataChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiver = new CartDataReceiver();
        filter = new IntentFilter();
        filter.addAction("android.intent.action.cart");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    private void setTotal() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.itemInfo.price * item.quantity;
        }
        tvTotal.setText(String.valueOf(total));
    }

    private void dataChanged(){
        Log.d(TAG, "Cart data changed.");
        items = cartDAO.getAll();
        setTotal();
        adapter.setData(items);
        adapter.notifyDataSetChanged();
    }
}
