package com.demo.savemymoney.data.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;

import java.util.List;

public class CategoryDetailHistoryRepository {
    private AppDatabase database;

    public CategoryDetailHistoryRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public LiveData<List<CategoryDetailHistory>> getAll(String userUID, Integer categoryId, int month) {
        return database.categoryDetailHistoryDao().findAll(userUID, categoryId, month);
    }
}
