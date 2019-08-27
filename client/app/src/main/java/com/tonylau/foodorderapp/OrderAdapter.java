package com.tonylau.foodorderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tonylau.foodorderapp.Activity.MyOrderActivity;
import com.tonylau.foodorderapp.DB.CartDAO;
import com.tonylau.foodorderapp.DB.OrderDAO;
import com.tonylau.foodorderapp.Object.Order;
import com.tonylau.foodorderapp.Object.OrderItem;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class OrderAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "OrderAdapter";
    private List<Order> mData;
    private MyOrderActivity context;

    public OrderAdapter(MyOrderActivity context, List<Order> data) {
        this.mData = data;
        this.context = context;
    }

    public void setData(List<Order> data) {
        this.mData = data;
    }

    @Override
    public int getGroupCount() {
        return this.mData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.mData.get(i).orderItems.size();
    }

    @Override
    public Order getGroup(int i) {
        return this.mData.get(i);
    }

    @Override
    public OrderItem getChild(int i, int i1) {
        return this.mData.get(i).orderItems.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String oid = String.valueOf(getGroup(i).orderId);
        final int index = i;
        final Order order = getGroup(index);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.adapter_order_group, null);
        }
        TextView tvGroupTitle = view.findViewById(R.id.tvGroupTitle);
        tvGroupTitle.setText(context.getString(R.string.tag_order_id) + oid);
        TextView tvGroupTotal = view.findViewById(R.id.tvGroupTotal);
        setTotal(tvGroupTotal, getGroup(i));

        Button btnRemove = view.findViewById(R.id.btnRemove);
        btnRemove.setFocusable(false);
        if (order.done) {
            btnRemove.setVisibility(View.VISIBLE);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View btnView) {
                    Log.d(TAG, "View position "+ index + " remove onclick");
                    Log.d(TAG, "Order "+order.orderId + " remove onclick");
                    OrderDAO orderDAO = new OrderDAO(context);
                    orderDAO.delete(order.orderId);
                    mData = orderDAO.getAll();
                    OrderAdapter.this.notifyDataSetChanged();
                    context.setTotal();
                }
            });
        } else {
            btnRemove.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final OrderItem item = getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_order_item, null);
        }
        ViewHolder holder = new ViewHolder(view);
        ImageParams imageParams = new ImageParams(holder, item.itemInfo.imgPath);
        ImageTask imageTask = new ImageTask();
        imageTask.execute(imageParams);

        holder.tvItemCat.setText(item.itemInfo.category);
        holder.tvItemName.setText(item.itemInfo.name);
        holder.tvPrice.setText(String.valueOf(item.itemInfo.price));
        holder.tvQuantity.setText(String.valueOf(item.quantity));
        holder.tvSubTotal.setText(String.valueOf(item.quantity * item.itemInfo.price));


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    private class ImageTask extends AsyncTask<ImageParams, Void, ImageParams> {

        @Override
        protected ImageParams doInBackground(ImageParams... imageParams) {
            SharedPreferences sp = context.getSharedPreferences(GlobalData.PREF_SETTING, 0);
            String url = sp.getString(GlobalData.PREF_KEY_URL, "http://10.0.2.2:3000") + imageParams[0].imgPath;
            Bitmap imgBitmap = GlobalFunc.getImageBitmap(url);
            imageParams[0].bitmap = imgBitmap;
            return imageParams[0];
        }

        @Override
        protected void onPostExecute(ImageParams imageParam) {
            super.onPostExecute(imageParam);
            imageParam.holder.ivImgPath.setImageBitmap(imageParam.bitmap);
        }
    }

    private class ImageParams {
        public ImageParams(ViewHolder holder, String imgPath) {
            this.holder = holder;
            this.imgPath = imgPath;
        }

        public ViewHolder holder;
        public String imgPath;
        public Bitmap bitmap;
    }

    // stores and recycles views as they are scrolled off screen
    private class ViewHolder {
        ImageView ivImgPath;
        TextView tvItemCat;
        TextView tvItemName;
        TextView tvQuantity;
        TextView tvPrice;
        TextView tvSubTotal;

        ViewHolder(View itemView) {
            ivImgPath = itemView.findViewById(R.id.ivImgPath);
            tvItemCat = itemView.findViewById(R.id.tvItemCat);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);

        }

    }

    private void setTotal(TextView tvTotal, Order order) {
        int total = 0;
        for (OrderItem item : order.orderItems) {
            total += item.itemInfo.price * item.quantity;
        }
        tvTotal.setText(context.getString(R.string.subtotal_tag) + total);
    }
}
