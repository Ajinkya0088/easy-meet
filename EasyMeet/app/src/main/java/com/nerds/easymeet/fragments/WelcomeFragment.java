package com.nerds.easymeet.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerds.easymeet.R;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {


    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setStatusBarColor(
                ContextCompat.getColor(Objects.requireNonNull(getContext()), android.R.color.holo_purple));
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

}
