package com.nerds.easymeet.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.nerds.easymeet.Constants;
import com.nerds.easymeet.MeetingModel;
import com.nerds.easymeet.R;
import com.nerds.easymeet.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FinalMeetingResultActivity extends AppCompatActivity {

    private MeetingModel meeting;
    private RecyclerView participants_rv, speaker_labels_rv;
    private TextView date, time, title, desc, sentiment, transciption, keywords_tv;
    private StringBuilder keywords;
    private Double sentimentSum, sentimentMean;
    private int keywordsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_meeting_result);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.final_result_activity_status_bar_color));

        meeting = (MeetingModel) getIntent().getExtras().getSerializable(Constants.MEETING_INTENT_EXTRA);

        initializeViews();

        keywordsCount = 0;
        sentimentSum = 0.0;
        keywords = new StringBuilder();

        if (meeting.getSummery() == null) {
            new Thread(this::getSummary).start();
        } else {
            sentimentMean = meeting.getSentiment();
            sentiment.setText(String.format("%s %s", sentimentMean < 0 ? "Negative" : "Positive", String.valueOf(sentimentMean)));
            keywords_tv.setText(meeting.getSummery());
        }
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(meeting.getTimestamp());
        date.setText(android.text.format.DateFormat.format("dd/MM/yyyy", calendar));
        time.setText(android.text.format.DateFormat.format("HH:mm", calendar));
        title.setText(meeting.getTitle());
        desc.setText(meeting.getDescription());
        transciption.setText(meeting.getSpeech_to_text());
        participants_rv.setLayoutManager(new LinearLayoutManager(this));
        participants_rv.setAdapter(new ParticipantsAdapter());
        speaker_labels_rv.setLayoutManager(new LinearLayoutManager(this));
        speaker_labels_rv.setAdapter(new SpeakerLabelsAdapter());
    }

    private void getSummary() {
        IamOptions iamOptions = new IamOptions.Builder()
                .apiKey(Constants.NLU_API_KEY)
                .build();

        NaturalLanguageUnderstanding nlu =
                new NaturalLanguageUnderstanding("2018-11-16", iamOptions);

        nlu.setEndPoint(Constants.NLU_API_URL);

        KeywordsOptions options = new KeywordsOptions.Builder()
                .sentiment(true)
                .build();

        Features features = new Features.Builder()
                .keywords(options)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(meeting.getSpeech_to_text())
                .features(features)
                .build();

        AnalysisResults response = nlu
                .analyze(parameters)
                .execute();

        List<KeywordsResult> keywordsResults = response.getKeywords();

        for (KeywordsResult keyword : keywordsResults) {
            keywords.append(keyword.getText()).append("\n");
            sentimentSum += keyword.getSentiment().getScore();
            keywordsCount++;
        }
        sentimentMean = sentimentSum / keywordsCount;

        runOnUiThread(() -> {
            keywords_tv.setText(keywords.toString());
            sentiment.setText(String.format("%s %s", sentimentMean < 0 ? "Negative" : "Positive", String.valueOf(sentimentMean)));
            updateMeeting();
        });

        //        System.out.println(response);
    }

    private void updateMeeting() {
        meeting.setSentiment(sentimentMean);
        meeting.setSummery(keywords.toString());
        FirebaseFirestore.getInstance().collection(Constants.MEETING_COLLECTION)
                .document(meeting.getId())
                .set(meeting);
    }


    private void initializeViews() {
        date = findViewById(R.id.meeting_date);
        time = findViewById(R.id.meeting_time);
        title = findViewById(R.id.meeting_title);
        desc = findViewById(R.id.meeting_desc);
        sentiment = findViewById(R.id.meeting_sentiment);
        participants_rv = findViewById(R.id.participants_list_rv);
        speaker_labels_rv = findViewById(R.id.speaker_labels_rv);
        transciption = findViewById(R.id.meeting_transciption);
        keywords_tv = findViewById(R.id.keywords);
    }

    private class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> {

        ArrayList<String> participants;

        ParticipantsAdapter() {
            participants = meeting.getParticipants();
        }

        @NonNull
        @Override
        public ParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(FinalMeetingResultActivity.this).inflate(R.layout.assign_task_card_view, parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull ParticipantsAdapter.ViewHolder holder, int position) {
            holder.email.setText(participants.get(position));
            holder.sr_no.setText(String.valueOf(position + 1));
        }

        @Override
        public int getItemCount() {
            return participants.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView email, assignButton, sr_no;
            private View.OnClickListener assignTaskClickListener = v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(FinalMeetingResultActivity.this);
                View view = LayoutInflater.from(FinalMeetingResultActivity.this).inflate(R.layout.assign_task_dialog_layout, null);
                builder.setView(view);
                TextView email = view.findViewById(R.id.email_id);
                EditText task = view.findViewById(R.id.task);
                email.setText(participants.get(getAdapterPosition()));
                builder.setPositiveButton("Assign", (dialog, which) -> {
                    CollectionReference taskColl = FirebaseFirestore.getInstance().collection(Constants.TASKS_COLLECTION);
                    String emailId = email.getText().toString();
                    taskColl.document(emailId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Map<String, Object> tasks = documentSnapshot.getData();
                                if (tasks == null) {
                                    tasks = new HashMap<>();
                                    tasks.put("0", new Task(meeting.getId(), task.getText().toString()));
                                    taskColl.document(emailId).set(tasks);
                                } else {
//                                    tasks.put();
                                    taskColl.document(emailId).update(String.valueOf(tasks.size()), new Task(meeting.getId(), task.getText().toString()));
                                }
                            });
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            };

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                sr_no = itemView.findViewById(R.id.sr_no);
                assignButton = itemView.findViewById(R.id.assign_task_button);
                email = itemView.findViewById(R.id.email_id);
                assignButton.setOnClickListener(assignTaskClickListener);
            }
        }
    }

    private class SpeakerLabelsAdapter extends RecyclerView.Adapter<SpeakerLabelsAdapter.ViewHolder> {


        private Map<String, String> speakerLabels;

        SpeakerLabelsAdapter() {
            speakerLabels = meeting.getSpeaker_labels();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(FinalMeetingResultActivity.this)
                    .inflate(R.layout.speaker_label_card_view, parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.label.setText(String.format(Locale.US, "Speaker %d:", position));
            holder.speakerWords.setText(speakerLabels.get(String.valueOf(position)));
        }

        @Override
        public int getItemCount() {
            return speakerLabels.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView label, speakerWords;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                label = itemView.findViewById(R.id.label);
                speakerWords = itemView.findViewById(R.id.speaker_words);
            }
        }
    }

}
