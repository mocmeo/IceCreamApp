package com.android.icecreamapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.TransitionRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.util.TweakUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shaishavgandhi.loginbuttons.GoogleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private GoogleButton btnSignIn;
    private EditText edtEmail, edtPassword;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;

    // For toolbar
    private Toolbar toolbar;

    //Component
    ImageView imageLogo;
    LinearLayout linearLayout;
    TextView textViewTitle;

    //Run Splash Screen
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            linearLayout.setVisibility(View.VISIBLE);
            textViewTitle.setVisibility(View.VISIBLE);
            imageLogo.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_login));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setLogoApp();
        if(checkFirstTime()){
            startActivity(new Intent(this, IntroActivity.class));
        }else{

            //setting Splash screen
            linearLayout = (LinearLayout)findViewById(R.id.linear);
            textViewTitle = (TextView)findViewById(R.id.textViewTitle);
            imageLogo = (ImageView)findViewById(R.id.imgViewLogo);



            handler.postDelayed(runnable, 2000);

            mapping();
            configToolbar();
            configGoogleLogin();
            TweakUI.makeTransparent(this);
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }

    }

    private void setLogoApp(){
        String[] arr = getResources().getStringArray(R.array.list_logo_name);
        ArrayList<String> arrLogo = new ArrayList<>(Arrays.asList(arr));

        Collections.shuffle(arrLogo);

        int idImage = getResources().getIdentifier(arrLogo.get(0),"drawable", getPackageName());

        imageLogo = (ImageView)findViewById(R.id.imgViewLogo);
        imageLogo.setImageResource(idImage);

    }


    private boolean checkFirstTime(){
        SharedPreferences pref = getSharedPreferences("app-config", Context.MODE_PRIVATE);
        boolean checkFirstTime = pref.getBoolean("firstTime", true);
        return checkFirstTime;
    }

    private void mapping() {
        toolbar = findViewById(R.id.toolbar);
        btnSignIn = findViewById(R.id.btnSignIn);
        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPassword = findViewById(R.id.edtLoginPassword);
    }

    private void configToolbar() {
        setSupportActionBar(toolbar);

        //remove title from toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configGoogleLogin() {
        mAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user_profile's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user_profile's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user_profile.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

}
