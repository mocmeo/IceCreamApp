package com.android.icecreamapp.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.icecreamapp.R;
import com.android.icecreamapp.fragment.AccountFragment;
import com.android.icecreamapp.fragment.CartFragment;
import com.android.icecreamapp.fragment.HomeFragment;
import com.android.icecreamapp.fragment.SearchFragment;
import com.android.icecreamapp.util.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private AccountFragment accountFragment;
    private SearchFragment searchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        initFragment();
        fragmentHandler();
        setFragment(homeFragment);
    }

    private void mapping() {
        mMainFrame = findViewById(R.id.main_frame);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        accountFragment = new AccountFragment();
        searchFragment = new SearchFragment();
    }

    private void fragmentHandler() {
        BottomNavigationViewHelper.removeShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;
                    case R.id.nav_cart:
                        setFragment(cartFragment);
                        return true;
                    case R.id.nav_account:
                        setFragment(accountFragment);
                        return true;

                    default: return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).addToBackStack("tag");
        fragmentTransaction.commit();
    }

}
