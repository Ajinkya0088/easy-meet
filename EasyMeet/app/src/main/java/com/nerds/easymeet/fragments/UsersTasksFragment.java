package com.nerds.easymeet.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nerds.easymeet.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTasksFragment extends Fragment {


    public UsersTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_tasks, container, false);
    }

}
