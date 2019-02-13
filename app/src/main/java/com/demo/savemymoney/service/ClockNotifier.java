package com.demo.savemymoney.service;

import android.content.Context;
import android.content.Intent;

public class ClockNotifier {
    public static void CountNotifier(Context context,Integer count,String userUID){

        Intent notificationIntent = new Intent(context,ClockNotifyService.class);
        notificationIntent.putExtra(ClockNotifyService.COUNT ,count);

        Intent notifi = new Intent(context,ClockNotifyPublisher.class);
        notifi.putExtra(ClockNotifyPublisher.NOTIFICATION_USER_UID,userUID);
    }

}
