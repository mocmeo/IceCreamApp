package com.android.icecreamapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.icecreamapp.R;
import com.android.icecreamapp.activity.ProductDetailActivity;
import com.android.icecreamapp.adapter.SearchProductAdapter;
import com.android.icecreamapp.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private EditText editTextSearch;
    private TextView textViewNumberResult;
    private ListView listViewSearch;
    private ArrayList<Product> arrProducts;
    private ArrayList<Product> products;
    private SearchProductAdapter searchProductAdapter;

    private DatabaseReference databaseReference;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment

        databaseReference = FirebaseDatabase.getInstance().getReference();

        textViewNumberResult = rootView.findViewById(R.id.textViewNumberResult);
        editTextSearch = rootView.findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                products.clear();
                if(!s.toString().equals("")){
                    for(Product p : arrProducts){
                        if(p.getName().toUpperCase().contains(s.toString().toUpperCase())){
                            products.add(p);
                        }
                    }
                }
                if(products.isEmpty()){
                    textViewNumberResult.setText("");
                }else{
                    textViewNumberResult.setText(products.size() + " results");
                }
                searchProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listViewSearch = rootView.findViewById(R.id.listViewSearch);
        arrProducts = new ArrayList<>();
        products = new ArrayList<>();
        searchProductAdapter = new SearchProductAdapter(getContext(), R.layout.layout_search_item,products);
        listViewSearch.setAdapter(searchProductAdapter);
        listViewSearch.setEmptyView(rootView.findViewById(R.id.layoutEmpty));

        listViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product_detail", products.get(position));
                getContext().startActivity(intent);
            }
        });
        initDataSearch();
        return rootView;
    }
    private void initDataSearch(){
        arrProducts.clear();
        databaseReference.child("Milkshake").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                arrProducts.add(product);
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
        databaseReference.child("IceCream").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Product product = dataSnapshot.getValue(Product.class);
                arrProducts.add(product);
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
