package com.demo.savemymoney.data.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.github.clemp6r.futuroid.Future;

import java.util.List;

import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryDetailHistoryRepository {
    private AppDatabase database;

    public CategoryDetailHistoryRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<Void> saveDetail(CategoryDetail detail) {
        return submit(() -> {
            database.categoryDetailHistoryDao().saveDetail(detail);
            return null;
        });
    }

    public LiveData<List<CategoryDetail>> getAll(String userUID, Integer categoryId, int month) {
        return database.categoryDetailHistoryDao().findAll(userUID, categoryId, month);
    }

    public Future<Object> deleteDetail(CategoryDetail detail) {
        return submit(() -> {
            database.categoryDetailHistoryDao().deleteDetail(detail);
            return null;
        });
    }
}
