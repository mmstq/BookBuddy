package com.mmstq.bookbuddy;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MessagingService extends FirebaseMessagingService {
    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }


}
