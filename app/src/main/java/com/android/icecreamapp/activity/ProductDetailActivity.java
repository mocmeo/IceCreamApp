package com.android.icecreamapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.IcecreamHomeAdapter;
import com.android.icecreamapp.adapter.MilkshakeHomeAdapter;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.model.OrderLine;
import com.android.icecreamapp.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

import q.rorbin.badgeview.QBadgeView;

public class ProductDetailActivity extends AppCompatActivity {

    private static final int NUM_COLUMNS = 2;
    private static final int ICE_CREAM = 1;
    private static final int MILKSHAKE = 2;

    private final int MAX_PRODUCT_QTY = 10;
    private final int MIN_PRODUCT_QTY = 1;

    private Toolbar toolbar;
    private ImageView imgDetail;
    private Button btnAddToCart;
    private String imgUrl;
    private TextView productName, productPrice, productDesc;
    private RecyclerView rvProductRelated;
    private ArrayList<Product> mProducts;
    private RecyclerView.Adapter productAdapter;
    private LinearLayout btnHome, btnCart;

    private DatabaseReference mData;
    private Product product;
    private QBadgeView badge;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mData = FirebaseDatabase.getInstance().getReference();
        product = (Product)getIntent().getSerializableExtra("product_detail");
        mapping();
        configToolbar();
        getInformation();
        initRecyclerView();
        initImageBitmaps();
        activityHandler();

        int qty = Cart.countIcecream();
        if (qty > 0) {
            badge.bindTarget(btnCart)
                    .setBadgeNumber(qty)
                    .setGravityOffset(2, true);
        }
    }

    private void activityHandler() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                intent.putExtra("info", "cart");
                startActivity(intent);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDialog();
            }
        });


    }

    private void cartDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.cart_dialog_custom, null);

        // mapping
        ImageView imageCart = view.findViewById(R.id.image_cart_dialog);
        TextView nameCart = view.findViewById(R.id.name_cart_dialog);
        TextView descCart = view.findViewById(R.id.desc_cart_dialog);
        final TextView priceCart = view.findViewById(R.id.price_cart_dialog);
        ImageButton btnMinus = view.findViewById(R.id.btn_minus_cart_dialog);
        ImageButton btnPlus = view.findViewById(R.id.btn_plus_cart_dialog);
        final EditText txtQuantity = view.findViewById(R.id.quantity_cart_dialog);
        Button btnAddToCart = view.findViewById(R.id.btn_add_to_cart_dialog);

        // set values
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        Glide.with(this)
                .asBitmap()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter())
                .load(imgUrl)
                .into(imageCart);
        nameCart.setText(product.getName());
        if(product.getDescription().length() > 100){
            String des = product.getDescription().substring(0, 99) + "...";
            descCart.setText(des);
        } else {
            descCart.setText(product.getDescription());
        }
        priceCart.setText(decimalFormat.format(product.getPrice()) + " đ̲");

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQty = txtQuantity.getText().toString();
                int quantity = Integer.parseInt(strQty) + 1;
                if (quantity > MAX_PRODUCT_QTY)
                    quantity = MAX_PRODUCT_QTY;
                txtQuantity.setText(String.valueOf(quantity));
                priceCart.setText(decimalFormat.format(product.getPrice() * quantity) + " đ̲");
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQty = txtQuantity.getText().toString();
                int quantity = Integer.parseInt(strQty) - 1;
                if (quantity < MIN_PRODUCT_QTY)
                    quantity = MIN_PRODUCT_QTY;
                txtQuantity.setText(String.valueOf(quantity));
                priceCart.setText(decimalFormat.format(product.getPrice() * quantity) + " đ̲");
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                quantity = refineQuantity(quantity);

                if (Cart.orderLinesList.size() > 0) {
                    boolean exist = false;
                    for (int i = 0; i < Cart.orderLinesList.size(); ++i) {
                        if (Cart.orderLinesList.get(i).getProduct().getId() == product.getId()) {
                            Cart.orderLinesList.get(i).setQuantity(Cart.orderLinesList.get(i).getQuantity() + quantity);
                            if (Cart.orderLinesList.get(i).getQuantity() > MAX_PRODUCT_QTY)
                                Cart.orderLinesList.get(i).setQuantity(MAX_PRODUCT_QTY);

                            Cart.orderLinesList.get(i).setTotalPrice(product.getPrice() * Cart.orderLinesList.get(i).getQuantity());
                            exist = true;
                        }
                    }

                    if (!exist) {
                        long price = quantity * product.getPrice();
                        Cart.orderLinesList.add(OrderLine.generateOrderLine(product, quantity, price));
                    }
                } else {
                    long price = quantity * product.getPrice();
                    Cart.orderLinesList.add(OrderLine.generateOrderLine(product, quantity, price));
                }

                int qty = Cart.countIcecream();
                if (qty > 0) {
                    new QBadgeView(getApplicationContext())
                            .bindTarget(btnCart)
                            .setBadgeNumber(qty)
                            .setGravityOffset(2, true);
                }
                dialog.dismiss();
            }
        });
    }

    private int refineQuantity(int quantity) {
        if (quantity > MAX_PRODUCT_QTY)
            return MAX_PRODUCT_QTY;
        else if (quantity < MIN_PRODUCT_QTY)
            return MIN_PRODUCT_QTY;
        return quantity;
    }

    private void initImageBitmaps() {
        if (product.getIdType() == ICE_CREAM) {
            mData.child("IceCream").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    mProducts.add(product);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (product.getIdType() == MILKSHAKE) {
            mData.child("Milkshake").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Product product = dataSnapshot.getValue(Product.class);
                    mProducts.add(product);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initRecyclerView() {
        mProducts = new ArrayList<>();

        if (product.getIdType() == ICE_CREAM) {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, NUM_COLUMNS);
            rvProductRelated.setLayoutManager(mLayoutManager);
            rvProductRelated.setNestedScrollingEnabled(false);
            productAdapter = new IcecreamHomeAdapter(this, mProducts);
            rvProductRelated.setAdapter(productAdapter);

        } else if (product.getIdType() == MILKSHAKE) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            RecyclerView rvMilk = findViewById(R.id.rvMilkshakeHome);
            rvProductRelated.setLayoutManager(layoutManager);
            rvProductRelated.setNestedScrollingEnabled(false);
            productAdapter = new MilkshakeHomeAdapter(this, mProducts);
            rvProductRelated.setAdapter(productAdapter);
        }


    }

    private void getInformation() {
        imgUrl = product.getImage();

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        productPrice.setText(decimalFormat.format(product.getPrice()) + " đ̲");
        productName.setText(product.getName());
        productDesc.setText(product.getDescription());

        Glide.with(this)
                .asBitmap()
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .fitCenter())
                .load(imgUrl)
                .into(imgDetail);
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar_detail);
        imgDetail = findViewById(R.id.img_detail);
        productName = findViewById(R.id.name_product_detail);
        productPrice = findViewById(R.id.price_product_detail);
        productDesc = findViewById(R.id.desc_product_detail);
        rvProductRelated = findViewById(R.id.rvProductRelated);
        btnHome = findViewById(R.id.btn_home_detail);
        btnCart = findViewById(R.id.btn_cart_detail);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        badge = new QBadgeView(this);
    }

    private void configToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(product.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
