package com.demo.savemymoney.common.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class Notifier {


    public static void scheduleMainAmount(Context context, long delay, String userUID) {
        Log.i(Notifier.class.getName(), String.format("Sending main amount scheduled task with delay: %s", delay));
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_TYPE, NotificationPublisher.NOTIFICATION_AMOUNT);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_USER_UID, userUID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


}
