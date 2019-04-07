package com.nerds.easymeet;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingsFragment extends Fragment {

    private View view;
    private FloatingActionButton createMeetingButton;

    public MeetingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meetings, container, false);
        initializeViews();
        createMeetingButton.setOnClickListener(createMeetingButtonListener);
        return view;
    }

    private View.OnClickListener createMeetingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), CreateMeetingActivity.class));
        }
    };

    private void initializeViews() {
        createMeetingButton = view.findViewById(R.id.create_meeting_button);
    }

}
