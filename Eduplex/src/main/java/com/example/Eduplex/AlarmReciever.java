package com.example.Eduplex;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent examinationsPage=new Intent(context, Examinations.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,examinationsPage,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"ExaminationAlert")
                .setSmallIcon(R.drawable.ic_baseline_assignment_24)
                .setContentTitle("Exam Approaching")
                .setContentText("Tommorrow you have an exam . Click to check ")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
        MediaPlayer mediaPlayer=MediaPlayer.create(context,R.raw.alarm);
        mediaPlayer.start();

    }
}

