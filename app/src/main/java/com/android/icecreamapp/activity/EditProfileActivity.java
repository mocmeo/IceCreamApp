package com.android.icecreamapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.icecreamapp.R;
import com.android.icecreamapp.firebase.FirebaseDatabaseHelper;
import com.android.icecreamapp.firebase.FirebaseUserEntity;
import com.android.icecreamapp.fragment.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextAddress;
    private Button buttonEdit, buttonCancel;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mapping();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void editProfile(){
        String profileName = editTextName.getText().toString();
        String profilePhone = editTextPhone.getText().toString();
        String profileEmail = editTextEmail.getText().toString();
        String profileAddress = editTextAddress.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent firebaseUserIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
            startActivity(firebaseUserIntent);

        } else {
            String id = user.getUid();

            FirebaseUserEntity userEntity = new FirebaseUserEntity(id, profileName, profilePhone, profileEmail, profileAddress);
            FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
            firebaseDatabaseHelper.createUserInFirebaseDatabase(id, userEntity);

        }
        finish();
    }

    private void mapping() {
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
}
