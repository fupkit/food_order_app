package com.tonylau.foodorderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tonylau.foodorderapp.Object.Item;

import java.net.URL;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private static final String TAG = "MenuAdapter";
    private List<Item> mData;
    private LayoutInflater mInflater;
    private Context context;

    public MenuAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = mData.get(position);
        ImageTask imageTask = new ImageTask();
        ImageParams imageParams = new ImageParams(holder, item.imgPath);
        imageTask.execute(imageParams);

        holder.tvItemCat.setText(item.category);
        holder.tvItemName.setText(item.item);
        holder.tvRemain.setText(String.valueOf(item.remain));
        holder.tvPrice.setText(String.valueOf(item.pricePerItem));

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                Log.d(TAG, item.item +" Add to cart.");
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
        TextView tvRemain;
        TextView tvPrice;
        Button btnAddToCart;

        ViewHolder(View itemView) {
            super(itemView);
            ivImgPath = itemView.findViewById(R.id.ivImgPath);
            tvItemCat = itemView.findViewById(R.id.tvItemCat);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvRemain = itemView.findViewById(R.id.tvRemain);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }

    }
}
