package com.demo.savemymoney.data.repository;

import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.github.clemp6r.futuroid.Future;

import java.util.ArrayList;
import java.util.List;

import static com.demo.savemymoney.common.util.DateUtils.isMonth;
import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryDetailHistoryRepository {
    private AppDatabase database;

    public CategoryDetailHistoryRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<List<CategoryDetailHistory>> getAll(String userUID, int month) {
        return submit(() -> {
            List<CategoryDetailHistory> filteredHistory = new ArrayList<>();
            List<CategoryDetailHistory> allHistory = database.categoryDetailHistoryDao().findAll(userUID);
            for (CategoryDetailHistory h : allHistory)
                if (isMonth(h.date, month)) {
                    Category category = database.categoryDao().findByUserUIDAndCategoryId(userUID, h.categoryId);
                    h.category = category;
                    filteredHistory.add(h);
                }
            return filteredHistory;
        });
    }
}
