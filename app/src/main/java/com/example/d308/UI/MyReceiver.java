package com.example.d308.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.d308.R;

public class MyReceiver extends BroadcastReceiver {
    String channel_id = "test";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationType = intent.getStringExtra("Date Alert");
        String vacationOrExcursionTitle = intent.getStringExtra("title");
        String title = "";
        String content = "";
        switch (notificationType) {
            case "V1":
                title = "Vacation Starting";
                content = vacationOrExcursionTitle + " starting";
                break;
            case "V2":
                title = "Vacation Ending";
                content = vacationOrExcursionTitle + " ending";
                break;
            case "E1":
                title = "Excursion Starting";
                content = vacationOrExcursionTitle + " starting";
                break;
        }

        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(content)
                .setContentTitle(title).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, n);
    }


    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence name = "mychannelname";
        String description = "mychanneldescription";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}