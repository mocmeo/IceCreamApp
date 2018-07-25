package com.android.icecreamapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.icecreamapp.R;
import com.android.icecreamapp.fragment.AccountFragment;
import com.android.icecreamapp.fragment.CartFragment;
import com.android.icecreamapp.fragment.HomeFragment;
import com.android.icecreamapp.fragment.SearchFragment;
import com.android.icecreamapp.util.BottomNavigationViewHelper;
import com.android.icecreamapp.util.TweakUI;
import com.viven.fragmentstatemanager.FragmentStateManager;

public class HomeActivity extends AppCompatActivity {

    private static final int HOME_FRAGMENT = 0;
    private static final int SEARCH_FRAGMENT = 1;
    private static final int CART_FRAGMENT = 2;
    private static final int ACCOUNT_FRAGMENT = 3;

    private BottomNavigationView bottomNavigation;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private AccountFragment accountFragment;
    private SearchFragment searchFragment;
    private FragmentStateManager fragmentStateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mapping();
        initFragment();
        fragmentHandler();
        TweakUI.makeTransparent(this);
        setFragment(homeFragment);
        redirectFragment();
    }

    private void redirectFragment() {
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentName = intent.getStringExtra("info");
            if (fragmentName != null) {
                if (fragmentName.equals("cart")) {
                    setFragment(cartFragment);
                    bottomNavigation.setSelectedItemId(R.id.nav_cart);
                }
            }
        }
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

//        fragmentStateManager = new FragmentStateManager(mMainFrame, getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case HOME_FRAGMENT:
//                        return homeFragment;
//                    case SEARCH_FRAGMENT:
//                        return searchFragment;
//                    case CART_FRAGMENT:
//                        return cartFragment;
//                    case ACCOUNT_FRAGMENT:
//                        return accountFragment;
//                }
//                return homeFragment;
//            }
//        };
    }

//    private int getNavPositionFromMenuItem(int id) {
//        switch (id) {
//            case R.id.nav_home:
//                return HOME_FRAGMENT;
//            case R.id.nav_search:
//                return SEARCH_FRAGMENT;
//            case R.id.nav_cart:
//                return CART_FRAGMENT;
//            case R.id.nav_account:
//                return ACCOUNT_FRAGMENT;
//        }
//        return -1;
//    }

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

                    default:
                        return false;
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
