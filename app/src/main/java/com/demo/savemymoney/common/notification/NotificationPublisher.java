package com.demo.savemymoney.common.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.demo.savemymoney.data.repository.MainAmountRepository;

import java.util.Calendar;

import static com.demo.savemymoney.common.util.DateUtils.getMillisUntil;

public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_USER_UID = "notification-user-uid";

    @Override
    public void onReceive(Context context, Intent intent) {
        String userUID = intent.getStringExtra(NOTIFICATION_USER_UID);
        MainAmountRepository repository = new MainAmountRepository(context);
        repository.increaseByIncome(userUID)
                .addSuccessCallback(result -> {
                    Calendar c = Calendar.getInstance();
                    c.setTime(result.payDate);
                    if (result.period.equals("MENSUAL"))
                        c.add(Calendar.MONTH, 1);
                    else
                        c.add(Calendar.DAY_OF_MONTH, 15);
                    Notifier.scheduleMainAmount(context, getMillisUntil(c.getTime()), userUID);
                });
    }
}
