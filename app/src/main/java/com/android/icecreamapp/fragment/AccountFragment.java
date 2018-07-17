package com.android.icecreamapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.activity.LoginActivity;
import com.android.icecreamapp.activity.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";

    private TextView toolbarTitle;
    private Toolbar toolbar;
    private CircleImageView imgUser;
    private Button btnSignOut;

    private GoogleSignInClient mGoogleSignInClient;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
//        toolbarTitle = rootView.findViewById(R.id.toolbar_title);
        imgUser = rootView.findViewById(R.id.img_user);
        btnSignOut = rootView.findViewById(R.id.btnSignOut);
//
//        Picasso.get()
//                .load(R.drawable.my_user)
//                .placeholder(R.drawable.no_image)
//                .into(imgUser);

//        configToolbar();
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
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
