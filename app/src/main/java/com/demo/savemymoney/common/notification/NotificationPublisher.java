package com.demo.savemymoney.common.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.repository.MainAmountRepository;
import com.demo.savemymoney.main.MainActivity;
import com.github.clemp6r.futuroid.FutureCallback;

import java.util.Calendar;

import static com.demo.savemymoney.common.util.DateUtils.getMillisUntil;

public class NotificationPublisher extends BroadcastReceiver {
    private static final String MAIN_AMOUNT_NOTIFICATION_ID = "MAN";
    private static final CharSequence CHANNEL_TITLE = "SMM Notifications";

    public static String NOTIFICATION_USER_UID = "notification-user-uid";
    public static String NOTIFICATION_TYPE = "notification-type";
    public static String NOTIFICATION_AMOUNT = "notification-amount";

    private static final String CHANNEL_ID = "SMM_NOTIFY_CHANNEL";


    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(NOTIFICATION_TYPE);
        if (NOTIFICATION_AMOUNT.equals(type)) {
            amountNotification(context, intent);
        }
    }

    private void amountNotification(Context context, Intent intent) {
        Log.i(getClass().getName(), "amount Notification");
        String userUID = intent.getStringExtra(NOTIFICATION_USER_UID);
        MainAmountRepository repository = new MainAmountRepository(context);
        repository.increaseByIncome(userUID)
                .addCallback(new FutureCallback<Income>() {
                    @Override
                    public void onSuccess(Income result) {
                        Log.i(getClass().getName(), "Success income increasing " + userUID);
                        Calendar c = Calendar.getInstance();
                        c.setTime(result.payDate);
                        if (result.period.equals("MENSUAL"))
                            c.add(Calendar.MONTH, 1);
                        else
                            c.add(Calendar.DAY_OF_MONTH, 15);
                        Notifier.scheduleMainAmount(context, getMillisUntil(c.getTime()), userUID);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error increasing by main amount", t);
                    }
                });

        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent action = PendingIntent.getActivity(context, 0, mainIntent, 0);
        sendNotification(context,
                intent,
                "Hoy es el día de pago :)",
                "Se limpiarán todos tus gastos. No te preocupes los podrás ver en el reporte de gastos",
                action);
    }

    private void sendNotification(Context context, Intent intent, String title, String content, PendingIntent action) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = getNotification(context, title, content, action);
        int id = intent.getIntExtra(MAIN_AMOUNT_NOTIFICATION_ID, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_TITLE, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id, notification);
    }

    private static Notification getNotification(Context context, String title, String content, PendingIntent action) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(action)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return mBuilder.build();
    }
}
