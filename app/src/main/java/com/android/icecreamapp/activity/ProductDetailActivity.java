package com.android.icecreamapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imgDetail;
    private String imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mapping();
        configToolbar();
        getInformation();
    }

    private void getInformation() {
        Product product = (Product)getIntent().getSerializableExtra("product_detail");
        imgUrl = product.getImage();

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
    }

    private void configToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setIcon(R.drawable.btn_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
