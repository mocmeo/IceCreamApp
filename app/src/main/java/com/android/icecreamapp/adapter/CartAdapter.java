package com.android.icecreamapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.activity.HomeActivity;
import com.android.icecreamapp.fragment.CartFragment;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.model.OrderLine;
import com.android.icecreamapp.util.UserHelper;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

import q.rorbin.badgeview.QBadgeView;

public class CartAdapter extends ArrayAdapter {

    private Context context;
    private int layout;
    private ArrayList<OrderLine> arrayOrderlines;
    private CartFragment parentFragment;

    public CartAdapter(@NonNull Context context, int resource, @NonNull ArrayList<OrderLine> arrayOrderlines, CartFragment parentFragment) {
        super(context, resource, arrayOrderlines);
        this.context = context;
        this.layout = resource;
        this.arrayOrderlines = arrayOrderlines;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(layout, null);
            holder.txtProductType = convertView.findViewById(R.id.txtProductTypeCart);
            holder.txtProductName = convertView.findViewById(R.id.name_cart_item);
            holder.txtProductDesc = convertView.findViewById(R.id.desc_cart_item);
            holder.txtProductPrice = convertView.findViewById(R.id.price_cart_item);
            holder.imgProduct = convertView.findViewById(R.id.image_cart_item);
            holder.imgProductType = convertView.findViewById(R.id.imgProductTypeCart);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus_cart);
            holder.btnPlus = convertView.findViewById(R.id.btn_plus_cart);
            holder.edtValue = convertView.findViewById(R.id.quantity_cart);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete_cart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        final OrderLine orderline = arrayOrderlines.get(position);

        if (getCount() > 0) {
            holder.txtProductName.setText(orderline.getProduct().getName());
            holder.txtProductPrice.setText(decimalFormat.format(orderline.getProduct().getPrice()) + "đ");
            Glide.with(this.context)
                    .load(orderline.getProduct().getImage())
                    .into(holder.imgProduct);
            if(orderline.getProduct().getIdType() == 1){
                holder.txtProductType.setText("ICE CREAM");
                holder.imgProductType.setImageResource(R.drawable.icecream_label);
            }else{
                holder.txtProductType.setText("MILKSHAKE");
                holder.imgProductType.setImageResource(R.drawable.milkshake_label);
            }
            if(orderline.getProduct().getDescription().length() > 28){
                String des = orderline.getProduct().getDescription().substring(0, 27) + "...";
                holder.txtProductDesc.setText(des);
            }
            holder.edtValue.setText(String.valueOf(orderline.getQuantity()));
        }

        final ViewHolder finalHolder = holder;

        setVisible(finalHolder, Integer.parseInt(finalHolder.edtValue.getText().toString()));
        calculatePrice();
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(finalHolder.edtValue.getText().toString()) + 1;
                int currentQty = orderline.getQuantity();
                long currentPrice = orderline.getTotalPrice();

                arrayOrderlines.get(position).setQuantity(newQty);
                long newPrice = (currentPrice * newQty) / currentQty;
                arrayOrderlines.get(position).setTotalPrice(newPrice);
//                finalHolder.txtProductPrice.setText(decimalFormat.format(newPrice) + " đ");
                finalHolder.edtValue.setText(String.valueOf(newQty));

                setVisible(finalHolder, newQty);
                calculatePrice();
                badgeHandler();
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(finalHolder.edtValue.getText().toString()) - 1;
                int currentQty = orderline.getQuantity();
                long currentPrice = orderline.getTotalPrice();

                arrayOrderlines.get(position).setQuantity(newQty);
                long newPrice = (currentPrice * newQty) / currentQty;
                arrayOrderlines.get(position).setTotalPrice(newPrice);
//                finalHolder.txtProductPrice.setText(decimalFormat.format(newPrice) + " đ");
                finalHolder.edtValue.setText(String.valueOf(newQty));

                setVisible(finalHolder, newQty);
                calculatePrice();
                badgeHandler();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Remove item?");
                alertDialog.setIcon(R.drawable.icon_app);
                alertDialog.setMessage("Do you want to remove this item?");

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cart.orderLinesList.remove(arrayOrderlines.get(position));
                        parentFragment.updateAdapter();
                        badgeHandler();
                        UserHelper.displayMessageToast(getContext(), "Remove item successfully!");

                        if(Cart.orderLinesList.size() <= 0){
                            parentFragment.bottom_navigation_cart.setVisibility(View.GONE);
                        }else{
                            parentFragment.bottom_navigation_cart.setVisibility(View.VISIBLE);
                        }
                        parentFragment.txtFinalPrice.setText("0đ");
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alertDialog.show();

            }
        });
        return convertView;
    }

    private void badgeHandler() {
        HomeActivity homeActivity = (HomeActivity)parentFragment.getActivity();
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) homeActivity.bottomNavigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2); // number of menu from left

        int qty = Cart.countIcecream();
        if (qty > 0) {
            HomeActivity.badge.bindTarget(v)
                    .setBadgeNumber(qty).setGravityOffset(20, 4, true);
        } else {
            HomeActivity.badge.hide(true);
        }
    }

    private void calculatePrice() {
        long finalPrice = 0;
        for (int i = 0; i < Cart.orderLinesList.size(); ++i) {
            finalPrice += Cart.orderLinesList.get(i).getTotalPrice();
        }
        parentFragment.setTextPrice(finalPrice);
    }

    private void setVisible(ViewHolder holder, int quantity) {
        if (quantity >= 10) {
            holder.btnPlus.setVisibility(View.INVISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
        } else if (quantity <= 1) {
            holder.btnPlus.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.INVISIBLE);
        } else {
            holder.btnPlus.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
        }
    }

    private class ViewHolder {
        public TextView txtProductName, txtProductType, txtProductPrice, txtProductDesc;
        public ImageView imgProduct, imgProductType;
        public ImageButton btnMinus, btnPlus;
        public EditText edtValue;
        public ImageButton btnDelete;
    }
}
