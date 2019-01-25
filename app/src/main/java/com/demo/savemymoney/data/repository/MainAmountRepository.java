package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.MainAmount;
import com.github.clemp6r.futuroid.Async;
import com.github.clemp6r.futuroid.Future;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;

public class MainAmountRepository {

    private AppDatabase database;

    public MainAmountRepository(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public Future<Void> save(MainAmount mainAmount) {
        return Async.submit(() -> {
            database.mainAmountDao().saveMainAmount(mainAmount);
            return null;
        });
    }
}
