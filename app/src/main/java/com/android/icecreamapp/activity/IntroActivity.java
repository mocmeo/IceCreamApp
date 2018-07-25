package com.android.icecreamapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.SliderAdaper;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mSliderViewPage;
    private LinearLayout mDotLayout;
    private Button buttonBack, buttonNext;
    private TextView[] mDots;

    private SliderAdaper sliderAdaper;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mSliderViewPage = findViewById(R.id.mSliderViewPage);
        mDotLayout = findViewById(R.id.mDotLayout);

        buttonBack = findViewById(R.id.buttonBack);
        buttonNext = findViewById(R.id.buttonNext);

        sliderAdaper = new SliderAdaper(this);
        mSliderViewPage.setAdapter(sliderAdaper);

        addDotIndicator(0);

        mSliderViewPage.addOnPageChangeListener(viewListener);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonNext.getText().equals("End")){
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                    SharedPreferences pref = getSharedPreferences("app-config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    finish();
                }else{
                    mSliderViewPage.setCurrentItem(mCurrentPage + 1);
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonBack.getText().equals("Skip")){
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                    SharedPreferences pref = getSharedPreferences("app-config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    finish();
                }else{
                    mSliderViewPage.setCurrentItem(mCurrentPage - 1);
                }

            }
        });
    }

    public void addDotIndicator(int position) {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorWhiteTransparent));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            mCurrentPage = position;

            if (position == 0) {
                buttonNext.setText("Next");
                buttonBack.setText("Skip");
            } else if (position + 1 == mDots.length) {
                buttonNext.setText("End");
                buttonBack.setText("Back");
            } else {
                buttonNext.setText("Next");
                buttonBack.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
