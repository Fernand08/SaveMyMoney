package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.github.clemp6r.futuroid.Future;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;
import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryDetailRepository {
    private AppDatabase database;

    public CategoryDetailRepository(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public Future<Void> saveDetail(CategoryDetail detail) {
        return submit(() -> {
            Integer newId = database.categoryDetailDao().getNewId(detail.userUID, detail.categoryId);
            detail.detailId = newId;
            database.categoryDao().decreaseAmount(detail.userUID, detail.categoryId, detail.amount);
            database.categoryDetailDao().saveDetail(detail);
            return null;
        });
    }
}
