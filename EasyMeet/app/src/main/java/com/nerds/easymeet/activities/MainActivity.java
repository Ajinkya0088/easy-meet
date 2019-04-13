package com.nerds.easymeet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.nerds.easymeet.NewMeetingService;
import com.nerds.easymeet.R;
import com.nerds.easymeet.fragments.MeetingsFragment;
import com.nerds.easymeet.fragments.TasksFragment;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MeetingsFragment meetingsFragment;
    private TasksFragment tasksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_activity_status_bar_color));

        meetingsFragment = new MeetingsFragment();

        tasksFragment = new TasksFragment();

        Spinner spinner = findViewById(R.id.main_activity_options_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_options, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        startService(new Intent(this, NewMeetingService.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.main_activity_fl, position == 0 ? meetingsFragment : tasksFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.main_activity_fl, meetingsFragment);
        transaction.commitAllowingStateLoss();
        parent.setSelection(0);
    }
}
