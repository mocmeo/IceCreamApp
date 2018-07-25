package com.android.icecreamapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.activity.HomeActivity;
import com.android.icecreamapp.adapter.CartAdapter;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.util.UserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private ListView lvCart;
    private Toolbar toolbar;
    private LinearLayout messageCart;

    private LinearLayout bottom_navigation_cart;

    private Button btnConfirm;
    private TextView txtFinalPrice;

    private CartAdapter newcartAdapter;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("cart");
        mAuth = FirebaseAuth.getInstance();

        mapping(rootView);
        actionToolbar();
        checkData();
        calculatePrice();

        if(Cart.orderLinesList.size() <= 0){
            bottom_navigation_cart.setVisibility(View.GONE);
        }else{
            bottom_navigation_cart.setVisibility(View.VISIBLE);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
                UserHelper.displayMessageToast(getContext(), "order success");
            }
        });

        return rootView;
    }

    private void saveOrder(){
        FirebaseUser user = mAuth.getCurrentUser();

        Map<String, Object> carts = new HashMap<String, Object>();
        String cartId = databaseReference.push().getKey();
        carts.put("id", cartId);
        carts.put("orderline", Cart.orderLinesList);
        carts.put("uId", user.getUid());

        databaseReference.child(cartId).setValue(carts);
        Cart.orderLinesList.clear();
        newcartAdapter.notifyDataSetChanged();
        if(Cart.orderLinesList.size() <= 0){
            bottom_navigation_cart.setVisibility(View.GONE);
        }else{
            bottom_navigation_cart.setVisibility(View.VISIBLE);
        }
        txtFinalPrice.setText("0đ");

        HomeActivity.badge.hide(true);

    }

    private void calculatePrice() {
        long finalPrice = 0;
        for (int i = 0; i < Cart.orderLinesList.size(); ++i) {
            finalPrice += Cart.orderLinesList.get(i).getTotalPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtFinalPrice.setText(decimalFormat.format(finalPrice) + "đ");
    }

    private void checkData() {
        if (Cart.orderLinesList.size() <= 0) {
            messageCart.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
        } else {
            lvCart.setVisibility(View.VISIBLE);
            messageCart.setVisibility(View.INVISIBLE);
        }
        newcartAdapter.notifyDataSetChanged();
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
        txtFinalPrice.setText(decimalFormat.format(price) + "đ");
    }

    private void mapping(View rootView) {
        lvCart = rootView.findViewById(R.id.lvCart);
        messageCart = rootView.findViewById(R.id.message_cart);
        btnConfirm = rootView.findViewById(R.id.btn_confirm_cart);
        txtFinalPrice = rootView.findViewById(R.id.total_price_cart);
        bottom_navigation_cart = rootView.findViewById(R.id.bottom_navigation_cart);

        newcartAdapter = new CartAdapter(getContext(), R.layout.layout_cart_item, Cart.orderLinesList,this);
        lvCart.setAdapter(newcartAdapter);
        lvCart.setEmptyView(messageCart);

    }

}
