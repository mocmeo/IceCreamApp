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
import com.google.firebase.database.ValueEventListener;

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

    public void checkExistUserInformation(final FirebaseUser user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("users");
        final List<FirebaseUserEntity> connectedUser = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (item.getKey().equals(user.getUid())) {
                        FirebaseUserEntity user = dataSnapshot.getValue(FirebaseUserEntity.class);
                        connectedUser.add(user);
                    }
                }
                if (connectedUser.size() == 0) {
                    String userName = "";
                    if(user.getDisplayName() != null){
                        userName = user.getDisplayName();
                    }else{
                        userName = user.getEmail().substring(0, user.getEmail().indexOf("@"));
                    }
                    databaseReference.child("users").child(user.getUid()).setValue(new FirebaseUserEntity(user.getUid(),userName));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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
                               final ImageView imageViewAvatar,
                               final ImageView imageCover) {
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bindData(user, context, dataSnapshot, textViewNameUser, textViewPhone, textViewEmail, textViewAddress, textViewAccount, imageViewAvatar, imageCover);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                bindData(user, context, dataSnapshot, textViewNameUser, textViewPhone, textViewEmail, textViewAddress, textViewAccount, imageViewAvatar, imageCover);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                bindData(user, context, dataSnapshot, textViewNameUser, textViewPhone, textViewEmail, textViewAddress, textViewAccount, imageViewAvatar, imageCover);
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
                          final ImageView imageViewAvatar,
                          final ImageView imageCover) {
        if (dataSnapshot.getKey().equals(user.getUid())) {
            FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);

            //set phone value
            if (userInformation.getPhone() != null) {
                textViewPhone.setText(userInformation.getPhone());
            } else if (user.getPhoneNumber() != null) {
                textViewPhone.setText(user.getPhoneNumber());
            }else{
                textViewPhone.setText("N/A");
            }
            // set display name value
            if (userInformation.getName() != null) {
                textViewNameUser.setText(userInformation.getName());
            } else if (user.getDisplayName() != null) {
                textViewNameUser.setText(user.getDisplayName());
            }else{
                textViewNameUser.setText("N/A");
            }
            // set email value
            if (userInformation.getEmail() != null) {
                textViewEmail.setText(userInformation.getName());
            } else if (user.getEmail() != null) {
                textViewEmail.setText(user.getEmail());
            }
            // set image value
            if (userInformation.getImageUrl() != null) {
                Glide.with(context).load(Uri.parse(userInformation.getImageUrl())).into(imageViewAvatar);
            } else if (user.getPhotoUrl() != null) {
                Glide.with(context).load(user.getPhotoUrl()).into(imageViewAvatar);
            } else {
                imageViewAvatar.setImageResource(R.drawable.no_image);
            }
            // set cover image value
            if (userInformation.getCoverUrl() != null) {
                Glide.with(context).load(Uri.parse(userInformation.getImageUrl())).into(imageCover);
            } else if (user.getPhotoUrl() != null) {
                Glide.with(context).load(user.getPhotoUrl()).into(imageCover);
            } else {
                imageCover.setImageResource(R.drawable.no_image);
            }

            textViewAccount.setText(user.getEmail());
            if(userInformation.getAddress() != null){
                textViewAddress.setText(userInformation.getAddress());
            }else{
                textViewAddress.setText("N/A");
            }
        }
    }
}
