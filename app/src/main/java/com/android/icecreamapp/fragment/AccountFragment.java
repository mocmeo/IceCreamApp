package com.android.icecreamapp.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private TextView toolbarTitle;
    private Toolbar toolbar;
    private CircleImageView imgUser;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
//        toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        imgUser = rootView.findViewById(R.id.img_user);

        Picasso.get()
                .load(R.drawable.my_user)
                .placeholder(R.drawable.no_image)
                .into(imgUser);

//        configToolbar();
        return rootView;
    }

    private void configToolbar() {
        AppCompatActivity currentActivity = ((AppCompatActivity)getActivity());
        currentActivity.setSupportActionBar(toolbar);

        //remove title from toolbar
        if (currentActivity.getSupportActionBar() != null) {
            currentActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            currentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
