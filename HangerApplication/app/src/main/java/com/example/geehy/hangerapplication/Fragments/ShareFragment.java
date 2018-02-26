package com.example.geehy.hangerapplication.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geehy.hangerapplication.R;

/**
 * Created by MIN on 2018-02-23.
 */

public class ShareFragment extends Fragment {
    private View view;

    public ShareFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_dress, container, false);
       // init();
        return view;
    }

}
