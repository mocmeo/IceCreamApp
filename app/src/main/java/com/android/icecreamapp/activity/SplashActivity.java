package com.android.icecreamapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.icecreamapp.R;

public class SplashActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private ImageView imageViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (checkFirstTime()) {
            startActivity(new Intent(this, IntroActivity.class));
        } else {
            textViewWelcome = findViewById(R.id.textViewWelcome);
            imageViewWelcome = findViewById(R.id.imageViewWelcome);

            Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_login);
            textViewWelcome.setAnimation(anim);
            imageViewWelcome.setAnimation(anim);

            final Intent intent = new Intent(this, LoginActivity.class);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                    }
                }
            };
            timer.start();
        }
    }

    private boolean checkFirstTime() {
        SharedPreferences pref = getSharedPreferences("app-config", Context.MODE_PRIVATE);
        boolean checkFirstTime = pref.getBoolean("firstTime", true);
        return checkFirstTime;
    }
}
