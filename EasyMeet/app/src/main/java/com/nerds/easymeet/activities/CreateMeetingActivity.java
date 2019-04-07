package com.nerds.easymeet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateMeetingActivity extends AppCompatActivity {

    private TextView timeTextView, dateTextView;
    private SharedPreferences sharedPreferences;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private View datePickerAlertLayout, timePickerAlertLayout;
    private AlertDialog dateDialog, timeDialog;
    private String USER_ID, USER_EMAIL;
    private RecyclerView participantsRecyclerView;
    private ArrayList<String> participants;
    private LinearLayout addParticipantButton;
    private ParticipantsAdapter adapter;
    private FloatingActionButton submitButton;
    private FirebaseFirestore firestore;
    private TextView titleTextView, descriptionTextView;
    private String meetingId;
    private ArrayList<String> finalParticipants;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        initializeViews();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.create_activity_status_bar_color));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER_EMAIL = sharedPreferences.getString(Constants.USER_EMAIL_ID, "");

        firestore = FirebaseFirestore.getInstance();

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

        count = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);

        timePicker.setOnTimeChangedListener(timeChangedListener);

        addParticipantButton.setOnClickListener(addParticipantButtonClickListener);

        submitButton.setOnClickListener(submitButtonClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        participants = new ArrayList<>();
        participants.add(USER_EMAIL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ParticipantsAdapter();
        participantsRecyclerView.setLayoutManager(layoutManager);
        participantsRecyclerView.setAdapter(adapter);
    }

    private View.OnClickListener submitButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dateTimeStr = dateTextView.getText().toString() + " " + timeTextView.getText().toString();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            long timestamp = 0;
            try {
                Date date = dateFormat.parse(dateTimeStr);
                timestamp = date.getTime();
                /*System.out.println(date.getTime());
                Calendar calendar = Calendar.getInstance(Locale.US);
                calendar.setTimeInMillis(date.getTime());
                System.out.println(android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", calendar));
                */
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finalParticipants = new ArrayList<>();
            View temp;
            EditText emailEditText;
            String currentParticipantEmail;
            for (int i = 0; i < participants.size(); i++) {
                temp = participantsRecyclerView.getChildAt(i);
                emailEditText = temp.findViewById(R.id.email_id);
                currentParticipantEmail = emailEditText.getText().toString();
                if (!currentParticipantEmail.equals("")) {
                    finalParticipants.add(currentParticipantEmail);
                }
            }
            MeetingModel newMeeting = new MeetingModel();
            newMeeting.setTimestamp(timestamp);
            newMeeting.setTitle(titleTextView.getText().toString());
            newMeeting.setDescription(descriptionTextView.getText().toString());
            newMeeting.setParticipants(finalParticipants);

            firestore.collection(Constants.MEETING_COLLECTION)
                    .add(newMeeting)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            meetingId = documentReference.getId();
                            addMeetingToParticipants();
                            Toast.makeText(CreateMeetingActivity.this, "Meeting Created Successfully!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreateMeetingActivity.this, RecordMeetingActivity.class));
                            finish();
                        }
                    });
        }
    };

    private void addMeetingToParticipants() {
        for (final String participant : finalParticipants) {
            final CollectionReference reference = firestore.collection(Constants.USERS_COLLECTION);
            reference.document(participant)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> update = new HashMap<>();
                            if (documentSnapshot.getData() == null) {
                                update.put("0", meetingId);
                                reference.document(participant).set(update)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                count++;
                                                if (count == finalParticipants.size()) {
                                                    Toast.makeText(CreateMeetingActivity.this, "Meeting Created Successfully!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(CreateMeetingActivity.this, RecordMeetingActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                update.put(String.valueOf(documentSnapshot.getData().size()), meetingId);
                                reference.document(participant).update(update)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                count++;
                                                if (count == finalParticipants.size()) {
                                                    Toast.makeText(CreateMeetingActivity.this, "Meeting Created Successfully!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(CreateMeetingActivity.this, RecordMeetingActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                            }

                        }
                    });
        }
    }

    private View.OnClickListener addParticipantButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View temp;
            EditText emailEditText;
            String currentParticipantEmail;
            for (int i = 0; i < participants.size(); i++) {
                temp = participantsRecyclerView.getChildAt(i);
                if (temp != null) {
                    emailEditText = temp.findViewById(R.id.email_id);
                    currentParticipantEmail = emailEditText.getText().toString();
                    participants.set(i, currentParticipantEmail);
                }
            }
            participants.add("");
            adapter = new ParticipantsAdapter();
            participantsRecyclerView.setAdapter(adapter);
        }
    };

    private DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTextView.setText(String.format(Locale.ENGLISH, "%02d/%02d/%04d", dayOfMonth, monthOfYear, year));
        }
    };

    private TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            timeTextView.setText(String.format(Locale.ENGLISH, "%02d:%02d", hourOfDay, minute));
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
        participantsRecyclerView = findViewById(R.id.participants_list_rv);
        addParticipantButton = findViewById(R.id.add_participant_button);
        submitButton = findViewById(R.id.submit_meeting_button);
        titleTextView = findViewById(R.id.meeting_title);
        descriptionTextView = findViewById(R.id.meeting_desc);
    }

    private void showDatePickerDialog() {
        dateDialog.show();
    }

    private void showTimePickerDialog() {
        timeDialog.show();
    }

    private class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(CreateMeetingActivity.this)
                    .inflate(R.layout.participants_list_card_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ParticipantsAdapter.ViewHolder holder, int position) {
            holder.SrNo.setText(String.format("%s.", String.valueOf(position + 1)));
            if (!participants.get(position).equals("")) {
                holder.participantEmailEditText.setText(participants.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return participants.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView SrNo;
            EditText participantEmailEditText;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                SrNo = itemView.findViewById(R.id.sr_no);
                participantEmailEditText = itemView.findViewById(R.id.email_id);
            }
        }
    }
}
