package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.MainAmount;
import com.github.clemp6r.futuroid.Future;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;
import static com.github.clemp6r.futuroid.Async.submit;

public class MainAmountRepository {

    private AppDatabase database;

    public MainAmountRepository(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public Future<Void> save(MainAmount mainAmount) {
        return submit(() -> {
            database.mainAmountDao().saveMainAmount(mainAmount);
            return null;
        });
    }

    public Future<MainAmount> findByUserUID(String userUID) {
        return submit(() -> {
            MainAmount firstSearch = database.mainAmountDao().findByUserUID(userUID);
            if (firstSearch != null)
                return firstSearch;
            else {
                MainAmount defaultAmount = MainAmount.defaultAmount(userUID);
                database.mainAmountDao().saveMainAmount(defaultAmount);
                return defaultAmount;
            }
        });
    }

    public Future<Void> increaseAmount(String userUID, Double amount) {
        return submit(() -> {
            database.mainAmountDao().increaseAmount(userUID, amount);
            return null;
        });
    }

    public Future<Void> decreaseAmount(String userUID, Double amount) {
        return submit(() -> {
            database.mainAmountDao().decreaseAmount(userUID, amount);
            return null;
        });
    }

    public Future<Void> changeAmount(String userUID, Double amount) {
        return submit(() -> {
            database.mainAmountDao().changeAmount(userUID, amount);
            return null;
        });
    }
}
