package com.nerds.easymeet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class CreateMeetingActivity extends AppCompatActivity {

    TextView timeTextView, dateTextView;
    SharedPreferences sharedPreferences;
    DatePicker datePicker;
    TimePicker timePicker;
    View datePickerAlertLayout, timePickerAlertLayout;
    AlertDialog dateDialog, timeDialog;
    String USER_ID, USER_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        initializeViews();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER_ID = sharedPreferences.getString(Constants.USER_ID, "");
        USER_EMAIL = USER_ID.replace(',', '.');

        createDatePickerAlert();
        createTimePickerAlert();

        timePicker.setIs24HourView(true);

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);

        timePicker.setOnTimeChangedListener(timeChangedListener);
    }

    private DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTextView.setText(String.format(Locale.ENGLISH, "%d-%d-%d", dayOfMonth, monthOfYear, year));
        }
    };

    private TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            timeTextView.setText(String.format(Locale.ENGLISH, "%d:%d", hourOfDay, minute));
        }
    };

    private void createDatePickerAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(datePickerAlertLayout);
        dateDialog = builder.create();
        dateDialog.setCanceledOnTouchOutside(true);
    }

    private void createTimePickerAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(timePickerAlertLayout);
        timeDialog = builder.create();
        timeDialog.setCanceledOnTouchOutside(true);
    }

    private void initializeViews() {
        timeTextView = findViewById(R.id.time);
        dateTextView = findViewById(R.id.date);
        datePickerAlertLayout = LayoutInflater.from(this).inflate(R.layout.date_picker_alert_layout, null);
        timePickerAlertLayout = LayoutInflater.from(this).inflate(R.layout.time_picker_alert_layout, null);
        datePicker = datePickerAlertLayout.findViewById(R.id.date_picker);
        timePicker = timePickerAlertLayout.findViewById(R.id.time_picker);
    }

    private void showDatePickerDialog() {
        dateDialog.show();
    }

    private void showTimePickerDialog() {
        timeDialog.show();
    }
}
