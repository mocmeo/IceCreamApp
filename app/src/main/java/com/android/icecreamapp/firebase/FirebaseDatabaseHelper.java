package com.android.icecreamapp.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.icecreamapp.R;
import com.android.icecreamapp.fragment.AccountFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import demo.loginmaster.Adapters.RecyclerViewAdapter;
//import demo.loginmaster.Helper.Helper;
//import demo.loginmaster.UserProfile;

public class FirebaseDatabaseHelper {

    private static final String TAG = FirebaseDatabaseHelper.class.getSimpleName();

    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity) {
        Map<String, FirebaseUserEntity> user = new HashMap<String, FirebaseUserEntity>();
        user.put(userId, firebaseUserEntity);
        databaseReference.child("users").setValue(user);
    }

    public void updateUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity) {
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("name", firebaseUserEntity.getName());
        user.put("email", firebaseUserEntity.getEmail());
        user.put("phone", firebaseUserEntity.getPhone());
        user.put("address", firebaseUserEntity.getAddress());
        databaseReference.child("users").child(userId).updateChildren(user);
    }

    public void isUserKeyExist(final FirebaseUser user,
                               final Context context,
                               final EditText textViewNameUser,
                               final EditText textViewPhone,
                               final EditText textViewEmail,
                               final EditText textViewAddress,
                               final EditText textViewAccount,
                               final ImageView imageViewAvatar) {
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bindData(user,context,dataSnapshot,textViewNameUser,textViewPhone,textViewEmail,textViewAddress,textViewAccount,imageViewAvatar);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                bindData(user,context,dataSnapshot,textViewNameUser,textViewPhone,textViewEmail,textViewAddress,textViewAccount,imageViewAvatar);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                bindData(user,context,dataSnapshot,textViewNameUser,textViewPhone,textViewEmail,textViewAddress,textViewAccount,imageViewAvatar);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void bindData(final FirebaseUser user,
                          final Context context,
                          final DataSnapshot dataSnapshot,
                          final EditText textViewNameUser,
                          final EditText textViewPhone,
                          final EditText textViewEmail,
                          final EditText textViewAddress,
                          final EditText textViewAccount,
                          final ImageView imageViewAvatar) {
        if (!dataSnapshot.getKey().equals(user.getUid())) {
            textViewNameUser.setText(user.getDisplayName());
            textViewAccount.setText(user.getEmail());
            textViewAddress.setText("");
            textViewPhone.setText(user.getPhoneNumber());
            textViewEmail.setText(user.getEmail());
            Glide.with(context).load(user.getPhotoUrl()).into(imageViewAvatar);
        } else {
            if (dataSnapshot.getKey().equals(user.getUid())) {
                FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);

                if (userInformation.getPhone() != null) {
                    textViewPhone.setText(userInformation.getPhone());
                } else if (user.getPhoneNumber() != null) {
                    textViewPhone.setText(user.getPhoneNumber());
                }

                if (userInformation.getName() != null) {
                    textViewNameUser.setText(userInformation.getName());
                } else if (user.getDisplayName() != null) {
                    textViewAddress.setText(user.getDisplayName());
                }

                if (userInformation.getImageUrl() != null) {
                    Glide.with(context).load(Uri.parse(userInformation.getImageUrl())).into(imageViewAvatar);
                } else if (user.getPhotoUrl() != null) {
                    Glide.with(context).load(user.getPhotoUrl()).into(imageViewAvatar);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.no_image);
                }

                textViewEmail.setText(userInformation.getEmail());
                textViewAccount.setText(user.getEmail());

                textViewAddress.setText(userInformation.getAddress());
            }
        }
    }
}
