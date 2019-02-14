package com.demo.savemymoney.data.repository;

import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.SavingHistory;
import com.github.clemp6r.futuroid.Future;

import java.util.List;

import static com.github.clemp6r.futuroid.Async.submit;

public class SavingHistoryRepository {

    private AppDatabase database;

    public SavingHistoryRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<List<SavingHistory>> getAll(String userUID) {
        return submit(() -> database.savingHistoryDao().getAll(userUID));
    }
}
