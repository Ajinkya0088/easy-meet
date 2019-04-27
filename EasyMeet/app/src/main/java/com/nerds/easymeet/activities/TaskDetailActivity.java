package com.nerds.easymeet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerds.easymeet.R;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.data.MeetingModel;
import com.nerds.easymeet.data.Task;

public class TaskDetailActivity extends AppCompatActivity {

    private Task task;
    private TextView date, time, title, desc;
    private MeetingModel meeting;
    private CardView meetingCard;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        task = (Task) getIntent().getExtras().getSerializable(Constants.TASK_INTENT_EXTRA);
        initializeViews();
        db = FirebaseFirestore.getInstance();
        db.collection(Constants.MEETING_COLLECTION).document(task.getMeetingId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                });

    }

    private void initializeViews() {
        date = findViewById(R.id.meeting_date);
        time = findViewById(R.id.meeting_time);
        title = findViewById(R.id.meeting_title);
        desc = findViewById(R.id.meeting_desc);
    }
}
