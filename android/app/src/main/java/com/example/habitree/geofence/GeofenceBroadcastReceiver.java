package com.example.habitree.geofence;


import static androidx.core.content.ContextCompat.getSystemService;
import static java.security.AccessController.getContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.habitree.R;
import com.example.habitree.api.HabitApi;
import com.example.habitree.model.HabitModel;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiver";
    private static final String CHANNEL_ID = "HABITREE_COMPLETED";

    private HabitApi habitApi;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        if (habitApi == null) {
            habitApi = new HabitApi(context);
        }
        Log.d(TAG, "Geofence triggered");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();

        for (Geofence geofence : geofenceList) {
            String uuid = geofence.getRequestId();
            Log.d(TAG, "onReceive: " + uuid);
            int transitionType = geofencingEvent.getGeofenceTransition();

            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                    HabitModel model = habitApi.getHabitById(UUID.fromString(uuid));
                    if (model == null) {
                        Log.d(TAG, "Received enter, model is null. Ignoring: " + uuid);
                        // probably currently creating geofence. Don't do anything
//                        new GeofencingClient(context).removeGeofences(Arrays.asList(uuid))
//                                .addOnSuccessListener(x -> Log.d(TAG, "Removed geofence with ID " + uuid))
//                                .addOnFailureListener(e -> Log.e(TAG, "Couldnt remove geofence with ID " + uuid + " msg: " + e.getMessage()));
                        continue;
                    }
                    if (!model.isToDoToday()) {
                        Log.d(TAG, "Habit already completed " + uuid);
                        continue;
                    }
                    createNotificationChannel(context);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("Habit completed")
                            .setContentText("Your habit named " + model.name + " was completed by the geofence!")
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(ThreadLocalRandom.current().nextInt(0,  Integer.MAX_VALUE), builder.build());

                    model.complete();
                    habitApi.updateHabitDatesCompleted(model.id, model.daysHabitCompleted);
                    break;
                case Geofence.GEOFENCE_TRANSITION_DWELL:
                    Log.d(TAG, "Received dwell: " + uuid);
                    Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    Log.d(TAG, "Received exit: " + uuid);
                    Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                    break;
            }

        }


    }

    private void createNotificationChannel(Context ctx) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ctx.getString(R.string.channel_name);
            String description = ctx.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}