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
    public FirebaseAuth mAuth;
    public ClockNotifyService clockNotifyService;

    @Override
    public void onReceive(Context context, Intent intent) {

        CategoryDetailRepository repository = new CategoryDetailRepository(context);
     SharedPreferences   preferences = context.getSharedPreferences("countDetail",Context.MODE_PRIVATE);
        repository.getCountDetails(mAuth.getCurrentUser().getUid(),new Date()).addCallback(new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
              /*  SharedPreferences.Editor editor = preferences.edit();
                editor.remove("count");
                editor.putString("count",String.valueOf(result));
                editor.commit(); */
              clockNotifyService.getCount(result);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }
}
