package com.android.icecreamapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.android.icecreamapp.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add slide 1
        addSlide(AppIntroFragment.newInstance("Slide 1", "This is first slide",
                R.mipmap.ic_launcher, ContextCompat.getColor(getApplicationContext(), R.color.slide1)));
        // Add slide 2
        addSlide(AppIntroFragment.newInstance("Slide 2", "This is second slide",
                R.mipmap.ic_launcher, ContextCompat.getColor(getApplicationContext(), R.color.slide2)));
        // Add slide 3
        addSlide(AppIntroFragment.newInstance("Slide 3", "This is third slide",
                R.mipmap.ic_launcher, ContextCompat.getColor(getApplicationContext(), R.color.slide3)));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        handleIntro();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        handleIntro();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void handleIntro(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        SharedPreferences pref = getSharedPreferences("app-config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("firstTime", false);
        editor.commit();
        finish();
    }
}
