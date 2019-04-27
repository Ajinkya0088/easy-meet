package com.nerds.easymeet.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nerds.easymeet.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class TasksFragment extends Fragment {

    private AssignedTasksFragment assignedTasksFragment;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        TextView byMe = view.findViewById(R.id.by_me);
        TextView toMe = view.findViewById(R.id.to_me);
        UsersTasksFragment usersTasksFragment = new UsersTasksFragment();
        assignedTasksFragment = new AssignedTasksFragment();
        byMe.setOnClickListener(v -> changeFragment(usersTasksFragment));
        toMe.setOnClickListener(v -> changeFragment(assignedTasksFragment));
        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.task_fragment_fl, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        changeFragment(assignedTasksFragment);
    }
}
