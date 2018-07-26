package com.android.icecreamapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.model.Product;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
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
            holder.txtProductType = convertView.findViewById(R.id.textViewProductType);
            holder.txtProductName = convertView.findViewById(R.id.name_cart_item);
            holder.txtProductDesc = convertView.findViewById(R.id.textViewProductDescription);
            holder.txtProductPrice = convertView.findViewById(R.id.textViewProductPrice);
            holder.imgProduct = convertView.findViewById(R.id.image_cart_item);
            holder.imgProductType = convertView.findViewById(R.id.imageViewProducType);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product p = arrProduct.get(position);
        if (getCount() > 0) {
            holder.txtProductName.setText(p.getName());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            holder.txtProductPrice.setText(decimalFormat.format(p.getPrice()) + "đ");
            Glide.with(this.context).load(p.getImage()).into(holder.imgProduct);
            if(p.getIdType() == 1){
                holder.txtProductType.setText("ICE CREAM");
                holder.imgProductType.setImageResource(R.drawable.icecream_label);
            }else{
                holder.txtProductType.setText("MILKSHAKE");
                holder.imgProductType.setImageResource(R.drawable.milkshake_label);
            }
            if(p.getDescription().length() > 30){
                String des = p.getDescription().substring(0, 29) + "...";
                holder.txtProductDesc.setText(des);
            }

        }
        //add animation
//        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_search_list);
//        convertView.startAnimation(anim);
        return convertView;
    }

    private class ViewHolder {
        TextView txtProductType;
        TextView txtProductName;
        TextView txtProductPrice;
        TextView txtProductDesc;
        ImageView imgProduct;
        ImageView imgProductType;
    }
}