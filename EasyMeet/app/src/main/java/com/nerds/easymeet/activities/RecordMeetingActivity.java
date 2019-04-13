package com.nerds.easymeet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeakerLabelsResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechTimestamp;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.data.MeetingModel;
import com.nerds.easymeet.R;
import com.nerds.easymeet.data.Speaker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class RecordMeetingActivity extends AppCompatActivity {

    private TextView date, time, title, desc;
    private MeetingModel meeting;
    private CardView meetingCard;
    private CardView recordButton;
    private boolean isRecording;
    private SpeechToText speechToText;
    private MicrophoneInputStream microphoneInputStream;
    private TextView recordButtonTV;
    private List<SpeechTimestamp> speechTimestamps;
    private List<Speaker> speakers;
    private Map<String, String> speakersTranscripts;
    private GifImageView recordingGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_meeting);
        getWindow().setStatusBarColor(
                ContextCompat.getColor(this, R.color.record_meeting_activity_status_bar_color));
        initializeViews();

        meeting = (MeetingModel) getIntent().getExtras().getSerializable(Constants.MEETING_INTENT_EXTRA);

        title.setText(meeting.getTitle());
        desc.setText(meeting.getDescription());
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(meeting.getTimestamp());
        date.setText(android.text.format.DateFormat.format("dd/MM/yyyy", calendar));
        time.setText(android.text.format.DateFormat.format("HH:mm", calendar));

        meetingCard.setStateListAnimator(null);
        meetingCard.setClickable(false);
        meetingCard.setFocusable(false);
        meetingCard.setCardElevation(16.0f);

        isRecording = false;

        recordButton.setOnClickListener(recordButtonClickListener);

        speechTimestamps = new ArrayList<>();
        speakers = new ArrayList<>();
        speakersTranscripts = new HashMap<>();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private View.OnClickListener recordButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isRecording) {
                isRecording = false;
                try {
                    microphoneInputStream.close();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recordingGif.setVisibility(View.GONE);
                recordButtonTV.setText(getResources().getString(R.string.start_recording));
                processResults();
            } else {
                recordingGif.setVisibility(View.VISIBLE);
                recordButtonTV.setText(getResources().getString(R.string.stop_recording));
                isRecording = true;
                IamOptions iamOptions = new IamOptions.Builder()
                        .apiKey(Constants.STT_API_KEY)
                        .build();
                speechToText = new SpeechToText(iamOptions);
                speechToText.setEndPoint(Constants.STT_API_URL);
                microphoneInputStream = new MicrophoneInputStream(true);
                RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
                        .audio(microphoneInputStream)
                        .speakerLabels(true)
                        .contentType(ContentType.OPUS.toString())
                        .interimResults(true)
                        .inactivityTimeout(2000)
                        .build();

                new Thread(() -> {
                    speechToText.recognizeUsingWebSocket(recognizeOptions, new STTCallBack());
                }).start();

            }
        }
    };

    //                .model("en-GB_BroadbandModel")
    private void initializeViews() {
        recordButton = findViewById(R.id.record_button);
        date = findViewById(R.id.meeting_date);
        time = findViewById(R.id.meeting_time);
        title = findViewById(R.id.meeting_title);
        desc = findViewById(R.id.meeting_desc);
        recordButtonTV = findViewById(R.id.record_button_tv);
        meetingCard = findViewById(R.id.meeting_cv);
        recordingGif = findViewById(R.id.recording_gif);
    }

    private StringBuilder transcriptBuilder = new StringBuilder();

    public class STTCallBack implements RecognizeCallback {

        @Override
        public void onTranscription(SpeechRecognitionResults speechResults) {
            Log.i("Speech Results:", speechResults.toString());
            if (speechResults.getResults() != null) {
                if (!speechResults.getResults().isEmpty()) {
                    if (speechResults.getResults().get(0).isFinalResults()) {
                        transcriptBuilder.append(speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
                        speechTimestamps.addAll(speechResults.getResults().get(0).getAlternatives().get(0).getTimestamps());

                    }
                }
            }
            if (speechResults.getSpeakerLabels() != null) {
                List<SpeakerLabelsResult> speakerLabelsResults = speechResults.getSpeakerLabels();
                for (SpeakerLabelsResult speakerLabel : speakerLabelsResults) {
                    speakers.add(new Speaker(
                            speakerLabel.getSpeaker(),
                            speakerLabel.getFrom(),
                            speakerLabel.getTo()
                    ));
                }
            }
        }

        @Override
        public void onConnected() {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onInactivityTimeout(RuntimeException runtimeException) {

        }

        @Override
        public void onListening() {

        }

        @Override
        public void onTranscriptionComplete() {

        }

    }

    private void processResults() {
        Collections.sort(speakers, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
        Collections.sort(speechTimestamps, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));

        int currentTimestamp = 0;

        for (Speaker speaker : speakers) {
            Long speakerLabel = speaker.getId();
            Float endTime = speaker.getEndTime();
            Float startTime = speaker.getStartTime();
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = currentTimestamp; i < speechTimestamps.size(); i++) {
                Float temp = Float.parseFloat(speechTimestamps.get(i).getEndTime().toString());
                if (temp.compareTo(endTime) <= 0) {
                    stringBuilder.append(speechTimestamps.get(i).getWord()).append(" ");
                    currentTimestamp++;
                } else break;
            }
            speakersTranscripts.put(String.valueOf(speakerLabel),
                    speakersTranscripts.get(String.valueOf(speakerLabel)) == null ?
                            stringBuilder.toString().replace("  ", " ") :
                            (speakersTranscripts.get(String.valueOf(speakerLabel)) + " " + stringBuilder.toString())
                                    .replace("  ", " "));

        }

        meeting.setSpeaker_labels(speakersTranscripts);
        meeting.setSpeech_to_text(transcriptBuilder.toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MEETING_INTENT_EXTRA, meeting);
        Intent intent = new Intent(this, FinalMeetingResultActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
      /*Log.i("speaker", speakers.toString());
        Log.i("speechTimeStamps", speechTimestamps.toString());
        Log.i("final transcipt", transcriptBuilder.toString());
        Log.i("speaker transcripts", speakersTranscripts.toString());
      */
    }
}
