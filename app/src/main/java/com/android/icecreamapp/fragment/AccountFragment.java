package com.android.icecreamapp.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.Model.IceCream;
import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.IceCreamAdapter;
import com.android.icecreamapp.firebase.FirebaseApplication;
import com.android.icecreamapp.firebase.FirebaseDatabaseHelper;
import com.android.icecreamapp.firebase.FirebaseStorageHelper;
import com.android.icecreamapp.util.Helper;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final int REQUEST_READ_PERMISSION = 120;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    public AccountFragment() {
        // Required empty public constructor
    }

    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();

        imageViewPhoto = rootView.findViewById(R.id.photo);

        constraintLayout = rootView.findViewById(R.id.constraint_layout);
        layout2.clone(rootView.getContext(),R.layout.profile_expanded);
        layout1.clone(constraintLayout);

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }else{
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        bindDataUser(user, rootView);

        TextView textView2 = rootView.findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Helper.SELECT_PICTURE);
            }
        });

        return rootView;
    }

    private void bindDataUser(FirebaseUser user, View view){

//        TextView tvNumber1 = (TextView) view.findViewById(R.id.tvNumber1);
//        TextView tvNumber3= (TextView) view.findViewById(R.id.tvNumber3);
//        TextView tvNumber5 = (TextView) view.findViewById(R.id.tvNumber5);
//
//
//        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
//        firebaseDatabaseHelper.isUserKeyExist(user, tvNumber1, tvNumber3, tvNumber5 );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("user id has entered onActivityResult ");
        if (requestCode == Helper.SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            String id = ((FirebaseApplication)getActivity().getApplication()).getFirebaseUserAuthenticateId();
            storageHelper.saveProfileImageToCloud(id, selectedImageUri, imageViewPhoto);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getActivity(), "Sorry!!!, you can't use this app without granting this permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
