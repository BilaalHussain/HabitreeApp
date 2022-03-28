package com.example.habitree.notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habitree.R;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setContentTitle("This is my test notification")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentText("We love Habitree")
                .build();
        notificationManager.notify(1,notification);

    }
}
