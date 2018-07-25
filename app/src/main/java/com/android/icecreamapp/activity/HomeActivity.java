package com.android.icecreamapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.icecreamapp.R;
import com.android.icecreamapp.fragment.AccountFragment;
import com.android.icecreamapp.fragment.CartFragment;
import com.android.icecreamapp.fragment.HomeFragment;
import com.android.icecreamapp.fragment.SearchFragment;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.util.BottomNavigationViewHelper;
import com.android.icecreamapp.util.TweakUI;
import com.viven.fragmentstatemanager.FragmentStateManager;

import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends AppCompatActivity {

    private static final int HOME_FRAGMENT = 0;
    private static final int SEARCH_FRAGMENT = 1;
    private static final int CART_FRAGMENT = 2;
    private static final int ACCOUNT_FRAGMENT = 3;

    public BottomNavigationView bottomNavigation;
    public static QBadgeView badge;
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
        badge = new QBadgeView(this);
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

                    default:
                        return false;
                }
            }
        });

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2); // number of menu from left
        int qty = Cart.countIcecream();
        if (qty > 0) {
            badge.bindTarget(v)
                    .setBadgeNumber(qty).setGravityOffset(20, 4, true);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).addToBackStack("tag");
        fragmentTransaction.commit();
    }

}
