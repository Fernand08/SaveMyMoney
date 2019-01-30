package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;
import com.github.clemp6r.futuroid.Future;

import java.util.Arrays;
import java.util.List;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;
import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryRepository {

    private List<Category> DEFAULT_CATEGORY_LIST = Arrays.asList(
            new Category(1, "Ahorros", "#ffffff", true, false),
            new Category(2, "Educación", "#ffffff", false, false),
            new Category(3, "Diversión", "#ffffff", false, false),
            new Category(4, "Casa", "#ffffff", false, false)
    );

    private AppDatabase database;

    public CategoryRepository(Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public Future<Integer> countCategories(String userUID) {
        return submit(() -> database.categoryDao().countCategories(userUID));
    }

    public Future<List<Category>> saveDefaultsAndGetCategories(String uid) {
        return submit(() -> {
            for (Category c : DEFAULT_CATEGORY_LIST) {
                c.userUID = uid;
            }
            database.categoryDao().save(DEFAULT_CATEGORY_LIST);
            return database.categoryDao().getAll(uid);
        });
    }

    public Future<List<Category>> getAll(String uid) {
        return submit(() -> database.categoryDao().getAll(uid));
    }
}
