package com.android.icecreamapp.firebase;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.icecreamapp.util.UserHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageHelper {

    private static final String TAG = FirebaseStorageHelper.class.getCanonicalName();

    private FirebaseStorage firebaseStorage;

    private DatabaseReference databaseReference;

    private StorageReference rootRef;

    private Context context;

    public FirebaseStorageHelper(Context context){
        this.context = context;
        init();
    }

    private void init(){
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        rootRef = firebaseStorage.getReferenceFromUrl("gs://icecreamapp-9de0f.appspot.com");
    }

    public void saveProfileImageToCloud(final String userId, Uri selectedImageUri, final ImageView imageView) {

        StorageReference photoParentRef = rootRef.child("profile_avatar/"+userId);
        final StorageReference photoRef = photoParentRef.child("user_avatar");
        UploadTask uploadTask = photoRef.putFile(selectedImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Glide.with(context).load(downloadUri).into(imageView);
                    databaseReference.child("users").child(userId).child("imageUrl").setValue(downloadUri.toString());
                    UserHelper.displayMessageToast(context, "Update avatar successful!");
                } else {
                    // Handle failures
                    UserHelper.displayMessageToast(context, "Update avatar fail!");
                    // ...
                }
            }
        });

    }
}
