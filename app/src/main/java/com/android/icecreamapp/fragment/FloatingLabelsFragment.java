package com.android.icecreamapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.icecreamapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FloatingLabelsFragment extends Fragment {


    public FloatingLabelsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_floating_labels, container, false);
    }

}
