package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Income;
import com.github.clemp6r.futuroid.Async;
import com.github.clemp6r.futuroid.Future;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;

public class IncomeRepository {

    private AppDatabase database;

    public IncomeRepository(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public Future<Void> save(Income income) {
        return submit(() -> {
            database.incomeDao().saveIncome(income);
            return null;
        });
    }

    public Future<Boolean> hasIncome(String userUID) {
        return submit(() -> {
            Income income = database.incomeDao().findByUserUID(userUID);
            return income != null;
        });
    }
}
