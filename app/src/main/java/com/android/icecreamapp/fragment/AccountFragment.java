package com.android.icecreamapp.fragment;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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

import com.android.icecreamapp.Model.IceCream;
import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.IceCreamAdapter;
import com.android.icecreamapp.firebase.FirebaseDatabaseHelper;
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
    private static final String TAG = "AccountFragment";

    private TextView toolbarTitle;
    private Toolbar toolbar;
    private CircleImageView imgUser;
    private Button btnSignOut;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    public AccountFragment() {
        // Required empty public constructor
    }

    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageViewPhoto;
    private TextView txt_bio;

    ListView listViewIceCream;
    IceCreamAdapter iceCreamAdapter;
    List<IceCream> arrIceCream;
    DatabaseReference dbIceCream;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        dbIceCream.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                arrIceCream.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    IceCream iceCream = artistSnapshot.getValue(IceCream.class);

                    arrIceCream.add(iceCream);
                }
                iceCreamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

//        Window w = rootView.window

        dbIceCream = FirebaseDatabase.getInstance().getReference("IceCream");

        listViewIceCream = (ListView) rootView.findViewById(R.id.listViewDemo);
        arrIceCream = new ArrayList<>();

        iceCreamAdapter = new IceCreamAdapter(rootView.getContext(), R.layout.list_ice_cream, arrIceCream);
        listViewIceCream.setAdapter(iceCreamAdapter);




        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        imageViewPhoto = rootView.findViewById(R.id.photo);
//        txt_bio = rootView.findViewById(R.id.txt_bio);

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


//        imgUser = rootView.findViewById(R.id.img_user);
//        imgUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(getContext(), EditProfileActivity.class), Activity.RESULT_OK);
//            }
//        });
//        btnSignOut = rootView.findViewById(R.id.btnSignOut);
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });
//
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        bindDataUser(user, rootView);

        return rootView;
    }

    private void bindDataUser(FirebaseUser user, View view){

        TextView tvNumber1 = (TextView) view.findViewById(R.id.tvNumber1);
        TextView tvNumber3= (TextView) view.findViewById(R.id.tvNumber3);
        TextView tvNumber5 = (TextView) view.findViewById(R.id.tvNumber5);

        ImageView imageViewUser = (ImageView)view.findViewById(R.id.img_user);
        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        firebaseDatabaseHelper.isUserKeyExist(user, tvNumber1, tvNumber3, tvNumber5 );
    }


}
