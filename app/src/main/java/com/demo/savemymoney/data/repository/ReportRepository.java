package com.demo.savemymoney.data.repository;

import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.tuples.CategoryPieTuple;
import com.github.clemp6r.futuroid.Future;

import java.util.List;

import static com.github.clemp6r.futuroid.Async.submit;

public class ReportRepository {

    private AppDatabase db;

    public ReportRepository(Context context) {
        this.db = AppDatabase.getAppDatabase(context);
    }

    public Future<List<CategoryPieTuple>> categoryPieList(String userUID) {
        return submit(() -> db.reportDao().categoryPieList(userUID));
    }
}
