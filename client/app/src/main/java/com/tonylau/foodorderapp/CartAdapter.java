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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tonylau.foodorderapp.DB.CartDAO;
import com.tonylau.foodorderapp.Object.OrderItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    private List<OrderItem> mData;
    private LayoutInflater mInflater;
    private Context context;

    public CartAdapter(Context context, List<OrderItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    public void setData(List<OrderItem> data) {
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_cart_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderItem item = mData.get(position);
        ImageTask imageTask = new ImageTask();
        ImageParams imageParams = new ImageParams(holder, item.itemInfo.imgPath);
        imageTask.execute(imageParams);

        holder.tvItemCat.setText(item.itemInfo.category);
        holder.tvItemName.setText(item.itemInfo.name);
        holder.tvQuantity.setText(String.valueOf(item.itemInfo.remain));
        holder.tvPrice.setText(String.valueOf(item.itemInfo.price));
        holder.tvQuantity.setText(String.valueOf(item.quantity));
        holder.tvSubTotal.setText(String.valueOf(item.itemInfo.price * item.quantity));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Delete from cart :" +item.itemInfo.name);
                CartDAO cartDAO = new CartDAO(context);
                cartDAO.delete(item.itemId);
                Intent intent = new  Intent();
                intent.setAction("android.intent.action.cart");
                context.sendBroadcast(intent);
            }
        });
    }



    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImgPath;
        TextView tvItemCat;
        TextView tvItemName;
        TextView tvQuantity;
        TextView tvPrice;
        ImageButton btnDelete;
        TextView tvSubTotal;

        ViewHolder(View itemView) {
            super(itemView);
            ivImgPath = itemView.findViewById(R.id.ivImgPath);
            tvItemCat = itemView.findViewById(R.id.tvItemCat);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSubTotal = itemView.findViewById(R.id.tvSubTotal);
            btnDelete = itemView.findViewById(R.id.ibDelete);
        }

    }
}
