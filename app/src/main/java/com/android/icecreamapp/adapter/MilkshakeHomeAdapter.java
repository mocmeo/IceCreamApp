package com.android.icecreamapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class MilkshakeHomeAdapter extends RecyclerView.Adapter<MilkshakeHomeAdapter.ViewHolder> {

    private static final String TAG = "MilkshakeHomeAdapter";

    //vars
    private ArrayList<Product> mProducts = new ArrayList<>();
    private Context mContext;

    public MilkshakeHomeAdapter(Context mContext, ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_milkshake_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter())
                .load(mProducts.get(position).image)
                .into(holder.image);

        holder.imageName.setText(mProducts.get(position).name);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on an image: " + mProducts.get(position).name);
                Toast.makeText(mContext, mProducts.get(position).name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageName;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image_home_milkshake);
            imageName = itemView.findViewById(R.id.image_name_home_milkshake);
        }
    }
}
