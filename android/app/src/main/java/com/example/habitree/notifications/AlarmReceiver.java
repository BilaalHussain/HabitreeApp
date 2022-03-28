package com.example.habitree.notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habitree.R;
import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    private HabitApi habitApi;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (habitApi == null) {
            habitApi = new HabitApi(context);
        }

        List<HabitModel> allHabits = habitApi.getAllHabits();

        for (HabitModel habit : allHabits) {
            if (habit.isToDoToday()) {
                notificationManager = NotificationManagerCompat.from(context);
                Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                        .setContentTitle("Don't forget to finish " + habit.name)
                        .setSmallIcon(R.drawable.logo)
                        .setContentText("Make sure to get in your daily habits. You'll be sure to thank yourself later!")
                        .build();
                notificationManager.notify(1,notification);
                return;
            }
        }

        // All habits are completed
        notificationManager = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_2_ID)
                .setContentTitle("Pat yourself on the back!")
                .setSmallIcon(R.drawable.logo)
                .setContentText("You've finished all your habits for the day. Great work!")
                .build();
        notificationManager.notify(2,notification);
        return;


    }
}
