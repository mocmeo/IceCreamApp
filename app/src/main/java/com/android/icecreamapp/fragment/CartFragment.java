package com.android.icecreamapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.CartAdapter;
import com.android.icecreamapp.model.Cart;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private ListView lvCart;
    private Toolbar toolbar;
    private TextView txtMessage;
    private Button btnConfirm;
    private TextView txtFinalPrice;

    private CartAdapter cartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        mapping(rootView);
        actionToolbar();
        checkData();
        calculatePrice();
        return rootView;
    }

    private void calculatePrice() {
        long finalPrice = 0;
        for (int i = 0; i < Cart.orderLinesList.size(); ++i) {
            finalPrice += Cart.orderLinesList.get(i).getTotalPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtFinalPrice.setText(decimalFormat.format(finalPrice) + " đ");
    }

    private void checkData() {
        if (Cart.orderLinesList.size() <= 0) {
            txtMessage.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
        } else {
            lvCart.setVisibility(View.VISIBLE);
            txtMessage.setVisibility(View.INVISIBLE);
        }
        cartAdapter.notifyDataSetChanged();
    }

    private void actionToolbar() {
        AppCompatActivity currentActivity = (AppCompatActivity) getActivity();

        if (currentActivity != null) {
            currentActivity.setSupportActionBar(toolbar);
            if (currentActivity.getSupportActionBar() != null)
                currentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void setTextPrice(long price) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtFinalPrice.setText(decimalFormat.format(price) + " đ");
    }

    private void mapping(View rootView) {
        lvCart = rootView.findViewById(R.id.lvCart);
        txtMessage = rootView.findViewById(R.id.txt_message_cart);
        btnConfirm = rootView.findViewById(R.id.btn_confirm_cart);
        txtFinalPrice = rootView.findViewById(R.id.total_price_cart);

        cartAdapter = new CartAdapter(getActivity(), Cart.orderLinesList, this);
        lvCart.setAdapter(cartAdapter);
    }

}
