package com.android.icecreamapp.firebase;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.icecreamapp.activity.HomeActivity;
import com.android.icecreamapp.util.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseApplication extends Application {

    private static final String TAG = FirebaseApplication.class.getSimpleName();

    public FirebaseAuth firebaseAuth;


    public void getFirebaseAuth(){
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getFirebaseUserAuthenticateId() {
        getFirebaseAuth();
        String userId = null;
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        return userId;
    }


    public void createNewUser(final Context context, String email, String password){
        getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    UserHelper.displayMessageToast(context, "Register fail!");
                }else{
                    UserHelper.displayMessageToast(context, "Register successful!");
                }
            }
        });
    }

    public void loginAUser(final Context context, String email, String password){
        getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            UserHelper.displayMessageToast(context, "Failed to login");
                        }else {
                            UserHelper.displayMessageToast(context, "User has been login");
                            Intent profileIntent = new Intent(context, HomeActivity.class);
                            context.startActivity(profileIntent);
                        }
                    }
                });
    }

}
