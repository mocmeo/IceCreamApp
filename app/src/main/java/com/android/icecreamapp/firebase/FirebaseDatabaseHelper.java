package com.android.icecreamapp.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.icecreamapp.fragment.AccountFragment;
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

    public FirebaseDatabaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity){
        Map<String, FirebaseUserEntity> user = new HashMap<String, FirebaseUserEntity>();
        user.put(userId, firebaseUserEntity);
        databaseReference.child("users").setValue(user);
    }

    public void isUserKeyExist(final FirebaseUser user, final TextView textViewPhone, final TextView textViewEmail, final TextView textViewAddress){
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())) {
                    FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);
                    if(!userInformation.getPhone().isEmpty()){
                        textViewPhone.setText(userInformation.getPhone());
                    }else{
                        textViewPhone.setText(user.getPhoneNumber());
                    }
                    if(!userInformation.getPhone().isEmpty()){
                        textViewEmail.setText(userInformation.getPhone());
                    }else{
                        textViewEmail.setText(user.getPhoneNumber());
                    }

                    textViewAddress.setText(userInformation.getAddress());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())) {
                    FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);
                    if(!userInformation.getPhone().isEmpty()){
                        textViewPhone.setText(userInformation.getPhone());
                    }else{
                        textViewPhone.setText(user.getPhoneNumber());
                    }
                    if(!userInformation.getPhone().isEmpty()){
                        textViewEmail.setText(userInformation.getPhone());
                    }else{
                        textViewEmail.setText(user.getPhoneNumber());
                    }

                    textViewAddress.setText(userInformation.getAddress());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private List<UserProfile> adapterSourceData(DataSnapshot dataSnapshot, String uId){
//        List<UserProfile> allUserData = new ArrayList<UserProfile>();
//        if(dataSnapshot.getKey().equals(uId)){
//            FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);
//            allUserData.add(new UserProfile(Helper.NAME, userInformation.getName()));
//            allUserData.add(new UserProfile(Helper.EMAIL, userInformation.getEmail()));
//            allUserData.add(new UserProfile(Helper.BIRTHDAY, userInformation.getBirthday()));
//            allUserData.add(new UserProfile(Helper.PHONE_NUMBER, userInformation.getPhone()));
//            allUserData.add(new UserProfile(Helper.HOBBY_INTEREST, userInformation.getHobby()));
//        }
//        return allUserData;
//    }
}
