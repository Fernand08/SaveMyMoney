package com.demo.savemymoney.data.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.demo.savemymoney.data.entity.MainAmount;
import com.github.clemp6r.futuroid.Future;

import java.util.List;

import static com.demo.savemymoney.data.entity.CategoryDetailHistory.fromDetail;
import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryDetailRepository {
    private AppDatabase database;

    public CategoryDetailRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<Void> saveDetail(CategoryDetail detail) {
        return submit(() -> {
            Integer newId = database.categoryDetailDao().getNewId(detail.userUID, detail.categoryId);
            detail.detailId = newId;
            database.categoryDao().decreaseAmount(detail.userUID, detail.categoryId, detail.amount);
            database.categoryDetailDao().saveDetail(detail);
            CategoryDetailHistory history = fromDetail(detail);
            MainAmount main = database.mainAmountDao().findByUserUID(detail.userUID);
            history.periodNumber = main.periodNumber;
            database.categoryDetailHistoryDao().saveDetail(history);
            return null;
        });
    }

    public LiveData<List<CategoryDetail>> getAll(String userUID, Integer categoryId) {
        return database.categoryDetailDao().findAll(userUID, categoryId);
    }

    public Future<Object> deleteDetail(CategoryDetail detail) {
        return submit(() -> {
            database.categoryDao().increaseAmount(detail.userUID, detail.categoryId, detail.amount);
            database.categoryDetailDao().deleteDetail(detail);
            CategoryDetailHistory history = fromDetail(detail);
            MainAmount main = database.mainAmountDao().findByUserUID(detail.userUID);
            history.periodNumber = main.periodNumber;
            database.categoryDetailHistoryDao().deleteDetail(history);
            return null;
        });
    }
}
