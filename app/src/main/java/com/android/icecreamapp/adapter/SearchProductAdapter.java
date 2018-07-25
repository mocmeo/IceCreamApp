package com.android.icecreamapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.icecreamapp.model.IceCream;
import com.android.icecreamapp.R;
import com.android.icecreamapp.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int layout;
    private List<Product> arrProduct;



    public SearchProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.arrProduct = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(layout, null);
            holder.textViewProductType = convertView.findViewById(R.id.textViewProductType);
            holder.textViewProductName = convertView.findViewById(R.id.textViewProductName);
            holder.textViewProductDescription = convertView.findViewById(R.id.textViewProductDescription);
            holder.textViewProductPrice = convertView.findViewById(R.id.textViewProductPrice);
            holder.imageViewProduct = convertView.findViewById(R.id.imageViewProduct);
            holder.imageViewProducType = convertView.findViewById(R.id.imageViewProducType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product p = arrProduct.get(position);
        if (getCount() > 0) {
            holder.textViewProductName.setText(p.getName());
            holder.textViewProductPrice.setText(String.valueOf(p.getPrice()));
            Glide.with(this.context).load(p.getImage()).into(holder.imageViewProduct);
            if(p.getIdType() == 1){
                holder.textViewProductType.setText("ICE CREAM");
                holder.imageViewProducType.setImageResource(R.drawable.icecream_label);
            }else{
                holder.textViewProductType.setText("MILKSHAKE");
                holder.imageViewProducType.setImageResource(R.drawable.milkshake_label);
            }
            if(p.getDescription().length() > 30){
                String des = p.getDescription().substring(0, 29) + "...";
                holder.textViewProductDescription.setText(des);
            }

        }
        //add animation
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_search_list);
        convertView.startAnimation(anim);
        return convertView;
    }

    private class ViewHolder {
        TextView textViewProductType;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductDescription;
        ImageView imageViewProduct;
        ImageView imageViewProducType;

    }
}