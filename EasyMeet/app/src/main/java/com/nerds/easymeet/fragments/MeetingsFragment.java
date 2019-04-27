package com.nerds.easymeet.fragments;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.data.MeetingModel;
import com.nerds.easymeet.R;
import com.nerds.easymeet.activities.CreateMeetingActivity;
import com.nerds.easymeet.activities.FinalMeetingResultActivity;
import com.nerds.easymeet.activities.RecordMeetingActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingsFragment extends Fragment {

    private View view;
    private LinearLayout createMeetingButton;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private String USER_EMAIL;
    private Map<String, Object> meetingsIds;
    private ArrayList<MeetingModel> meetings;
    private int count;
    private RecyclerView meetingsRecyclerView;
    private LinearLayout progressBarLayout, noMeetingsFoundLayout;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        USER_EMAIL = sharedPreferences.getString(Constants.USER_EMAIL_ID, "");

        meetings = new ArrayList<>();

        count = 0;

        db = FirebaseFirestore.getInstance();
        db.collection(Constants.USERS_MEETINGS_COLLECTION)
                .document(USER_EMAIL)
                .get()
                .addOnSuccessListener(getMeetingsSuccessListener);
        return view;
    }

    private OnSuccessListener<? super DocumentSnapshot> getMeetingsSuccessListener =
            new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    meetingsIds = documentSnapshot.getData();
                    getMeetingsDetails();
                }
            };

    private void getMeetingsDetails() {
        if (meetingsIds == null) {
            progressBarLayout.setVisibility(View.GONE);
            noMeetingsFoundLayout.setVisibility(View.VISIBLE);
            return;
        }

        for (int i = 0; i < meetingsIds.size(); i++) {
            db.collection(Constants.MEETING_COLLECTION)
                    .document(Objects.requireNonNull(meetingsIds.get(String.valueOf(i))).toString())
                    .get()
                    .addOnSuccessListener(getMeetingDetailsSuccessListener);
        }

    }

    private OnSuccessListener<? super DocumentSnapshot> getMeetingDetailsSuccessListener =
            new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    meetings.add(documentSnapshot.toObject(MeetingModel.class));
                    count++;
                    if (count == meetingsIds.size()) {
                        Collections.sort(meetings, (o1, o2) -> o1.getTimestamp() > o2.getTimestamp() ? 1 : 0);
                        setMeetingsAdapter();
                    }
                }
            };

    private void setMeetingsAdapter() {
        progressBarLayout.setVisibility(View.GONE);
        meetingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        meetingsRecyclerView.setAdapter(new MeetingsAdapter());
    }

    private View.OnClickListener createMeetingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), CreateMeetingActivity.class));
            getActivity().finish();
        }
    };

    private void initializeViews() {
        createMeetingButton = view.findViewById(R.id.create_meeting_button);
        meetingsRecyclerView = view.findViewById(R.id.meeting_rv);
        progressBarLayout = view.findViewById(R.id.progress_bar);
        noMeetingsFoundLayout = view.findViewById(R.id.no_meetings_found_layout);
    }

    private class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.meeting_card_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MeetingModel meeting = meetings.get(position);
            holder.title.setText(meeting.getTitle());
            holder.desc.setText(meeting.getDescription());
            Calendar calendar = Calendar.getInstance(Locale.US);
            calendar.setTimeInMillis(meeting.getTimestamp());
            holder.date.setText(android.text.format.DateFormat.format("dd/MM/yyyy", calendar));
            holder.time.setText(android.text.format.DateFormat.format("HH:mm", calendar));
            holder.number_of_participants.setText(String.valueOf(meeting.getParticipants().size()));

        }

        @Override
        public int getItemCount() {
            return meetings.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, desc, date, time, number_of_participants;
            CardView cardView;

            ViewHolder(@NonNull final View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.meeting_title);
                desc = itemView.findViewById(R.id.meeting_desc);
                date = itemView.findViewById(R.id.meeting_date);
                time = itemView.findViewById(R.id.meeting_time);
                cardView = itemView.findViewById(R.id.meeting_cv);
                number_of_participants = itemView.findViewById(R.id.number_of_participants);

                cardView.setOnClickListener(v -> {
                    MeetingModel clickedMeeting = meetings.get(getAdapterPosition());
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            getActivity(), itemView, getResources().getString(R.string.meeting_card_transition));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.MEETING_INTENT_EXTRA, clickedMeeting);
                    Intent intent = new Intent(getContext(),
                            clickedMeeting.getSpeech_to_text() == null ? RecordMeetingActivity.class : FinalMeetingResultActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent, options.toBundle());
                });
            }
        }
    }

}
