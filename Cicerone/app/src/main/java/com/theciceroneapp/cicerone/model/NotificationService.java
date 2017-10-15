package com.theciceroneapp.cicerone.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.theciceroneapp.cicerone.R;
import com.theciceroneapp.cicerone.controller.TripHomeActivity;

/**
 * Created by crsch on 10/15/2017.
 */

public class NotificationService extends Service {

    public static int NOTIFICATION_ID = 324;
    public static NotificationService singleton;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start(String title, String text) {

        System.out.println("HEEEEERRRREEE");

        Intent i = new Intent(this, TripHomeActivity.class);

        TaskStackBuilder tsb = TaskStackBuilder.create(this);
        tsb.addParentStack(TripHomeActivity.class);
        tsb.addNextIntent(i);

        PendingIntent pt = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification nb = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentTitle(title).setContentText(text)
                .setContentIntent(pt).build();

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        nm.notify(NOTIFICATION_ID, nb);
    }

    @Override
    public void onCreate() {
        System.out.println("Now bounded");
        singleton = this;
    }
}
