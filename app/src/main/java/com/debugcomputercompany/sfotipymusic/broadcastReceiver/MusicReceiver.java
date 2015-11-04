package com.debugcomputercompany.sfotipymusic.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.debugcomputercompany.sfotipymusic.MusicActivity;
import com.debugcomputercompany.sfotipymusic.R;

public class MusicReceiver extends BroadcastReceiver {
    public MusicReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Reproduciendo: " + intent.getExtras().getString("song_name"), Toast.LENGTH_LONG).show();


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MusicActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.header_bg)
                        .setContentTitle("Sfotipy")
                        .setContentText("Reproduciendo: " + intent.getExtras().getString("song_name"));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}
