package com.insomniacgks.miniprojectsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.developer_cloud.android.library.audio.utils.ContentType;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.RecognizeCallback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    boolean isRecording = false;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String fileName = "";
    private String[] permissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SpeechToText speechToText;
    private MicrophoneInputStream microphoneInputStream;
    private TextView result;
    private Thread thread;
    private TranscribeService mTranscribeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, 0);
        result = findViewById(R.id.textView);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/voicerecording.mpeg";
        final Button button = findViewById(R.id.record_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_SHORT).show();
                    startRecording();
                    isRecording = true;
                    button.setText("Stop Recording");
                } else {
                    Toast.makeText(MainActivity.this, "Recording stopped", Toast.LENGTH_SHORT).show();
                    isRecording = false;
                    button.setText("Start Recording");
                    stopRecording();
                }
            }
        });
    }

    private void stopRecording() {
        /*mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        */
        try {
            microphoneInputStream.close();
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        /*IamOptions iamOptions = new IamOptions.Builder()
                .apiKey("aJ471j-r6jv1byn-tXhWq9g5wr4FOMZIMUIGwZ5IJpZK")
                .build();
        speechToText = new SpeechToText(iamOptions);
        speechToText.setEndPoint("https://gateway-lon.watsonplatform.net/speech-to-text/api");
//        microphoneInputStream = new MicrophoneInputStream(true);
        final RecognizeOptions options;
        try {
            FileInputStream stream = new FileInputStream(fileName);
            options = new RecognizeOptions.Builder()
                    .audio(stream)
                    .speakerLabels(true)
                    .contentType("audio/mpeg")
                    .interimResults(false)
                    .model("en-US_BroadbandModel")
                    .build();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    speechToText.recognizeUsingWebSocket(options, recognizeCallback);
                }
            }).start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }

    private void startRecording() {
        /*mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(this.fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            mediaRecorder.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();*/


        IamOptions iamOptions = new IamOptions.Builder()
                .apiKey("aJ471j-r6jv1byn-tXhWq9g5wr4FOMZIMUIGwZ5IJpZK")
                .build();
        speechToText = new SpeechToText(iamOptions);
        speechToText.setEndPoint("https://gateway-lon.watsonplatform.net/speech-to-text/api");
        microphoneInputStream = new MicrophoneInputStream(true);
        final RecognizeOptions options = new RecognizeOptions.Builder()
                .audio(microphoneInputStream)
                .speakerLabels(true)
                .contentType(ContentType.OPUS.toString())
                .interimResults(false)
                .inactivityTimeout(2000)
                .model("en-US_BroadbandModel")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SpeechRecognitionResults results = speechToText.recognize(options).execute();
                System.out.println(results);
            }
        }).start();

        //mTranscribeService.execute();*/
    }

    class TranscribeService extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            RecognizeOptions options = new RecognizeOptions.Builder()
                    .audio(microphoneInputStream)
                    .speakerLabels(true)
                    .contentType(ContentType.OPUS.toString())
                    .interimResults(false)
                    .inactivityTimeout(2000)
                    .model("en-US_NarrowbandModel")
                    .build();

            speechToText.recognizeUsingWebSocket(options, recognizeCallback);
            return null;
        }
    }

    BaseRecognizeCallback recognizeCallback = new BaseRecognizeCallback() {
        @Override
        public void onTranscription(SpeechRecognitionResults speechResults) {
            Log.v("result", speechResults.toString());
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.setText(speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
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
    };
}
