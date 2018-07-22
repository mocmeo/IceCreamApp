package com.android.icecreamapp.fragment;


import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.adapter.IcecreamHomeAdapter;
import com.android.icecreamapp.adapter.MilkshakeHomeAdapter;
import com.android.icecreamapp.model.Product;
import com.android.icecreamapp.util.TweakUI;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int NUM_COLUMNS = 2;

//    private Toolbar toolbar;
    private AppCompatActivity currentActivity;
    private SliderLayout sliderShow;
    private int[] slideImages = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3, R.drawable.slider4};
    private MilkshakeHomeAdapter milkAdapter;
    private IcecreamHomeAdapter iceAdapter;

    // Firebase section
    private DatabaseReference mData;

    //vars
    private ArrayList<Product> mMilks;
    private ArrayList<Product> mIces;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        toolbar = rootView.findViewById(R.id.toolbarHome);
        currentActivity = (AppCompatActivity) getActivity();
        mData = FirebaseDatabase.getInstance().getReference();

        if (currentActivity != null) {
//            currentActivity.setSupportActionBar(toolbar);
            setupSlider(rootView);
            initRecyclerView(rootView);
            initImageBitmaps();
        }
        return rootView;
    }

    private void setupSlider(View rootView) {
        sliderShow = rootView.findViewById(R.id.sliderHome);

        for (int i = 0; i < slideImages.length; ++i) {
            TextSliderView newSlider = new TextSliderView(getActivity());
            newSlider.description("Ice Cream " + (i + 1))
                    .image(slideImages[i]);

            sliderShow.addSlider(newSlider);
        }
    }

    private void initRecyclerView(View rootView) {
        mMilks = new ArrayList<>();
        mIces = new ArrayList<>();

        // Milkshake recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rvMilk = rootView.findViewById(R.id.rvMilkshakeHome);
        rvMilk.setLayoutManager(layoutManager);
        rvMilk.setNestedScrollingEnabled(false);
        milkAdapter = new MilkshakeHomeAdapter(getActivity(), mMilks);
        rvMilk.setAdapter(milkAdapter);


        // Icecream recycler view
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), NUM_COLUMNS);
        RecyclerView rvIce = rootView.findViewById(R.id.rvIcecreamHome);
        rvIce.setLayoutManager(mLayoutManager);
        rvIce.setNestedScrollingEnabled(false);
        iceAdapter = new IcecreamHomeAdapter(getActivity(), mIces);
        rvIce.setAdapter(iceAdapter);

    }

    private void initImageBitmaps(){
        mData.child("Milkshake").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                mMilks.add(product);
                milkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mData.child("IceCream").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                mIces.add(product);
                iceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
