package com.android.icecreamapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.icecreamapp.R;
import com.android.icecreamapp.firebase.FirebaseApplication;
import com.android.icecreamapp.util.UserHelper;
import com.android.icecreamapp.util.TweakUI;

public class SignUpActivity extends AppCompatActivity {

    private Button buttonRegister, buttonCancel;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();
        TweakUI.makeTransparent(this);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = editTextEmail.getText().toString();
                String enteredPassword = editTextPassword.getText().toString();

                if(TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)){
                    UserHelper.displayMessageToast(SignUpActivity.this, "Login fields must be filled");
                    return;
                }
                if(!UserHelper.isValidEmail(enteredEmail)){
                    UserHelper.displayMessageToast(SignUpActivity.this, "Invalidate email entered");
                    return;
                }
                ((FirebaseApplication)getApplication()).createNewUser(SignUpActivity.this, enteredEmail, enteredPassword);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_back, R.anim.anim_exit_back);
            }
        });
    }

    private void mapping() {
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
}
