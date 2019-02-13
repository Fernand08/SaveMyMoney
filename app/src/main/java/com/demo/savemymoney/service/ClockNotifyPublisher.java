package com.demo.savemymoney.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.demo.savemymoney.data.repository.CategoryDetailRepository;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class ClockNotifyPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_USER_UID = "notification-user-uid";
    public FirebaseAuth firebaseAuth;
    @Override
    public void onReceive(Context context, Intent intent) {
        String userUID = intent.getStringExtra(NOTIFICATION_USER_UID);
        CategoryDetailRepository repository = new CategoryDetailRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
        repository.getCountDetails(firebaseAuth.getCurrentUser().getUid() ,new Date()).addCallback(new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
            ClockNotifier.CountNotifier(context,result,userUID);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}
