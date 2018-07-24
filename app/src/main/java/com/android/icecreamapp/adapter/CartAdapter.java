package com.android.icecreamapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.fragment.CartFragment;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.model.OrderLine;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderLine> arrayOrderlines;
    private CartFragment parentFragment;

    public CartAdapter(Context context, ArrayList<OrderLine> arrayOrderlines, CartFragment parentFragment) {
        this.context = context;
        this.arrayOrderlines = arrayOrderlines;
        this.parentFragment = parentFragment;

    }

    @Override
    public int getCount() {
        return arrayOrderlines.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayOrderlines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_cart_item, null);

            holder.txtCartName = convertView.findViewById(R.id.name_cart_item);
            holder.txtCartPrice = convertView.findViewById(R.id.price_cart_item);
            holder.imgCart = convertView.findViewById(R.id.image_cart_item);
            holder.btnMinus = convertView.findViewById(R.id.btn_minus_cart);
            holder.btnPlus = convertView.findViewById(R.id.btn_plus_cart);
            holder.edtValue = convertView.findViewById(R.id.quantity_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        final OrderLine orderLine = arrayOrderlines.get(position);

        holder.txtCartName.setText(orderLine.getProduct().getName());
        holder.txtCartPrice.setText(decimalFormat.format(orderLine.getProduct().getPrice()));

        Glide.with(context)
                .asBitmap()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter())
                .load(orderLine.getProduct().getImage())
                .into(holder.imgCart);
        holder.edtValue.setText(String.valueOf(orderLine.getQuantity()));

        final ViewHolder finalHolder = holder;

        setVisible(finalHolder, Integer.parseInt(finalHolder.edtValue.getText().toString()));
        calculatePrice();
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(finalHolder.edtValue.getText().toString()) + 1;
                int currentQty = orderLine.getQuantity();
                long currentPrice = orderLine.getTotalPrice();

                arrayOrderlines.get(position).setQuantity(newQty);
                long newPrice = (currentPrice * newQty) / currentQty;
                arrayOrderlines.get(position).setTotalPrice(newPrice);
                finalHolder.txtCartPrice.setText(decimalFormat.format(newPrice) + " đ");
                finalHolder.edtValue.setText(String.valueOf(newQty));

                setVisible(finalHolder, newQty);
                calculatePrice();
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(finalHolder.edtValue.getText().toString()) - 1;
                int currentQty = orderLine.getQuantity();
                long currentPrice = orderLine.getTotalPrice();

                arrayOrderlines.get(position).setQuantity(newQty);
                long newPrice = (currentPrice * newQty) / currentQty;
                arrayOrderlines.get(position).setTotalPrice(newPrice);
                finalHolder.txtCartPrice.setText(decimalFormat.format(newPrice) + " đ");
                finalHolder.edtValue.setText(String.valueOf(newQty));

                setVisible(finalHolder, newQty);
                calculatePrice();
            }
        });

        return convertView;
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
        public TextView txtCartName, txtCartPrice;
        public ImageView imgCart;
        public ImageButton btnMinus, btnPlus;
        public EditText edtValue;

        public ViewHolder() {
        }
    }
}
