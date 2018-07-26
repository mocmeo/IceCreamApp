package com.android.icecreamapp.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.activity.HomeActivity;
import com.android.icecreamapp.activity.LoginActivity;
import com.android.icecreamapp.firebase.FirebaseApplication;
import com.android.icecreamapp.firebase.FirebaseDatabaseHelper;
import com.android.icecreamapp.firebase.FirebaseStorageHelper;
import com.android.icecreamapp.firebase.FirebaseUserEntity;
import com.android.icecreamapp.model.Cart;
import com.android.icecreamapp.util.UserHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private static final int REQUEST_READ_PERMISSION = 120;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    EditText textViewNameUser;
    EditText textViewPhone;
    EditText textViewEmail;
    EditText textViewAddress;
    EditText textViewAccount;
    ImageView imageViewAvatar;
    ImageView imageViewCover;

    EditText textViewOldPassword;
    EditText textViewPassword;

    ImageView imageViewEditAccount;
    ImageView imageViewSaveAccount;
    ImageView imageViewCancelEditAccount;
    ImageView imageViewEditProfile;
    ImageView imageViewSaveProfile;
    ImageView imageViewCancelEdit;

    Button buttonSignOut;

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

        final View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        mapping(rootView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        bindDataUser(user, rootView);

        imageViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingEditProfile(true);
            }
        });
        imageViewCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingEditProfile(false);
                bindDataUser(mAuth.getCurrentUser(), rootView);
            }
        });
        imageViewSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = textViewNameUser.getText().toString();
                String phone = textViewPhone.getText().toString();
                String email = textViewEmail.getText().toString();
                String address = textViewAddress.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                firebaseDatabaseHelper.updateUserInFirebaseDatabase(user.getUid(), new FirebaseUserEntity(user.getUid(), email, name, phone, address, "",""));
                settingEditProfile(false);
            }
        });

        imageViewEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingEditAccount(true);
            }
        });
        imageViewCancelEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingEditAccount(false);
            }
        });
        imageViewSaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reAuthEmail = textViewAccount.getText().toString();
                String reAuthPassword = textViewOldPassword.getText().toString();
                final String newPassword = textViewPassword.getText().toString();
                if (TextUtils.isEmpty(reAuthPassword)) {
                    UserHelper.displayMessageToast(getContext(), "Old password field must be filled");
                } else if (TextUtils.isEmpty(newPassword)) {
                    UserHelper.displayMessageToast(getContext(), "New password field must be filled");
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(reAuthEmail, reAuthPassword); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        UserHelper.displayMessageToast(getContext(), "Update Password success");
                                                        settingEditAccount(false);
                                                    } else {
                                                        UserHelper.displayMessageToast(getContext(), "Password should be at least 6 characters");
                                                    }
                                                }
                                            });
                                    //----------------------------------------------------------\\
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            UserHelper.displayMessageToast(getContext(), "Old password incorrect!");
                        }
                    });
                }
            }
        });

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, UserHelper.SELECT_PICTURE_AVATAR);
            }
        });

//        imageViewCover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, UserHelper.SELECT_PICTURE_COVER);
//            }
//        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Cart.orderLinesList.clear();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        return rootView;
    }

    private void settingEditProfile(boolean isEdit) {
        if (isEdit) {
            textViewNameUser.setEnabled(true);
            textViewPhone.setEnabled(true);
            textViewEmail.setEnabled(true);
            textViewAddress.setEnabled(true);
            textViewNameUser.setBackgroundResource(R.drawable.edit_profile_background);
            textViewPhone.setBackgroundResource(R.drawable.edit_profile_background);
            textViewEmail.setBackgroundResource(R.drawable.edit_profile_background);
            textViewAddress.setBackgroundResource(R.drawable.edit_profile_background);
            textViewNameUser.setTextColor(Color.BLACK);
            textViewPhone.setTextColor(Color.BLACK);
            textViewEmail.setTextColor(Color.BLACK);
            textViewAddress.setTextColor(Color.BLACK);

            imageViewEditProfile.setVisibility(View.GONE);
            imageViewSaveProfile.setVisibility(View.VISIBLE);
            imageViewCancelEdit.setVisibility(View.VISIBLE);
        } else {
            textViewNameUser.setEnabled(false);
            textViewPhone.setEnabled(false);
            textViewEmail.setEnabled(false);
            textViewAddress.setEnabled(false);
            textViewNameUser.setBackgroundColor(Color.TRANSPARENT);
            textViewPhone.setBackgroundColor(Color.TRANSPARENT);
            textViewEmail.setBackgroundColor(Color.TRANSPARENT);
            textViewAddress.setBackgroundColor(Color.TRANSPARENT);
            textViewPhone.setTextColor(getResources().getColor(R.color.information_color));
            textViewEmail.setTextColor(getResources().getColor(R.color.information_color));
            textViewAddress.setTextColor(getResources().getColor(R.color.information_color));

            imageViewEditProfile.setVisibility(View.VISIBLE);
            imageViewSaveProfile.setVisibility(View.GONE);
            imageViewCancelEdit.setVisibility(View.GONE);
        }
    }

    private void settingEditAccount(boolean isEdit) {
        if (isEdit) {
            textViewAccount.setVisibility(View.GONE);
            textViewOldPassword.setVisibility(View.VISIBLE);
            imageViewEditAccount.setVisibility(View.GONE);
            imageViewSaveAccount.setVisibility(View.VISIBLE);
            imageViewCancelEditAccount.setVisibility(View.VISIBLE);

            textViewOldPassword.setBackgroundResource(R.drawable.edit_profile_background);
            textViewPassword.setBackgroundResource(R.drawable.edit_profile_background);

            textViewOldPassword.setTextColor(Color.BLACK);
            textViewPassword.setTextColor(Color.BLACK);
            textViewOldPassword.setText("");
            textViewPassword.setText("");

            textViewOldPassword.setEnabled(true);
            textViewPassword.setEnabled(true);
        } else {
            textViewAccount.setVisibility(View.VISIBLE);
            textViewOldPassword.setVisibility(View.GONE);
            imageViewEditAccount.setVisibility(View.VISIBLE);
            imageViewSaveAccount.setVisibility(View.GONE);
            imageViewCancelEditAccount.setVisibility(View.GONE);

            textViewOldPassword.setBackgroundColor(Color.TRANSPARENT);
            textViewPassword.setBackgroundColor(Color.TRANSPARENT);

            textViewOldPassword.setTextColor(getResources().getColor(R.color.information_color));
            textViewPassword.setTextColor(getResources().getColor(R.color.information_color));
            textViewPassword.setTextColor(getResources().getColor(R.color.information_color));

            textViewPassword.setText("• • • • • • • • • • • •");

            textViewOldPassword.setEnabled(false);
            textViewPassword.setEnabled(false);
        }
    }

    private void mapping(View view) {
        textViewNameUser = view.findViewById(R.id.textViewNameUser);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewAccount = view.findViewById(R.id.textViewAccount);
        imageViewAvatar = view.findViewById(R.id.imageViewAvatar);
        imageViewCover = view.findViewById(R.id.imageViewCover);
        textViewOldPassword = view.findViewById(R.id.textViewOldPassword);
        textViewPassword = view.findViewById(R.id.textViewPassword);

        imageViewEditProfile = view.findViewById(R.id.imageViewEditProfile);
        imageViewSaveProfile = view.findViewById(R.id.imageViewSaveProfile);
        imageViewCancelEdit = view.findViewById(R.id.imageViewCancelEdit);
        imageViewEditAccount = view.findViewById(R.id.imageViewEditAccount);
        imageViewSaveAccount = view.findViewById(R.id.imageViewSaveAccount);
        imageViewCancelEditAccount = view.findViewById(R.id.imageViewCancelEditAccount);

        buttonSignOut = view.findViewById(R.id.buttonSignOut);

    }

    private void bindDataUser(FirebaseUser user, View view) {

        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        firebaseDatabaseHelper.isUserKeyExist(user, getContext(), textViewNameUser, textViewPhone, textViewEmail, textViewAddress, textViewAccount, imageViewAvatar, imageViewCover);
        List<String> providers = user.getProviders();
        switch (providers.get(0)) {
            case "facebook.com":
                imageViewEditAccount.setVisibility(View.GONE);
                break;
            case "google.com":
                imageViewEditAccount.setVisibility(View.GONE);
                break;
            default:
                imageViewEditAccount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("user id has entered onActivityResult ");
        if (requestCode == UserHelper.SELECT_PICTURE_AVATAR) {
            Uri selectedImageUri = data.getData();
            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            String id = ((FirebaseApplication) getActivity().getApplication()).getFirebaseUserAuthenticateId();
            storageHelper.saveProfileImageToCloud(id, selectedImageUri, imageViewAvatar, UserHelper.SELECT_PICTURE_AVATAR);
        }else if (requestCode == UserHelper.SELECT_PICTURE_COVER){
            Uri selectedImageUri = data.getData();
            FirebaseStorageHelper storageHelper = new FirebaseStorageHelper(getActivity());

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
            String id = ((FirebaseApplication) getActivity().getApplication()).getFirebaseUserAuthenticateId();
            storageHelper.saveProfileImageToCloud(id, selectedImageUri, imageViewCover, UserHelper.SELECT_PICTURE_COVER);
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
