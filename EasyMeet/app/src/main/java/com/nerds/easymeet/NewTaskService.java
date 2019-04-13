package com.nerds.easymeet;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerds.easymeet.activities.TaskDetailActivity;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.data.Task;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class NewTaskService extends Service {

    private static final String CHANNEL_ID = "112";
    private String USER_EMAIL;
    private int notificationId = 100;

    @Override
    public void onCreate() {
        USER_EMAIL = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USER_EMAIL_ID, "");
        int lastTaskId = PreferenceManager.getDefaultSharedPreferences(this).getInt(Constants.LAST_TASK_ID, 0);

        if (!USER_EMAIL.equals("")) {
            FirebaseFirestore.getInstance().collection(Constants.TASKS_COLLECTION).document(USER_EMAIL)
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Map<String, Object> data = documentSnapshot.getData();

                            Log.i("data", data.toString());
                            for (Map.Entry<String, Object> s : data.entrySet()) {
                                if (Integer.parseInt(s.getKey()) > lastTaskId) {
                                    createNotification((Task) s.getValue());
                                }
                            }

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                            editor.putInt(Constants.LAST_TASK_ID, data.size() - 1);
                            editor.apply();
                        }
                    });
        }
    }

    private void createNotification(Task newTask) {
        FirebaseFirestore.getInstance().collection(Constants.USERS_COLLECTION)
                .document(newTask.getAssigner_id())
                .get()
                .addOnSuccessListener(documentSnapshot1 -> notifyUser(newTask, documentSnapshot1));
    }

    private void notifyUser(Task task, DocumentSnapshot documentSnapshot1) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TASK_INTENT_EXTRA, task);
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_list)
                .setColor(ContextCompat.getColor(this, R.color.c))
                .setContentTitle("New Task")
                .setContentText(String.format("New task assigned by %s",
                        documentSnapshot1.getData().get("name")))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(++notificationId, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d(TAG, "TASK REMOVED");

        PendingIntent service = PendingIntent.getService(
                this,
                1001,
                new Intent(this, com.nerds.easymeet.NewMeetingService.class),
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
    }


}
