package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;
import com.github.clemp6r.futuroid.Future;

import java.util.Arrays;
import java.util.List;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;
import static com.github.clemp6r.futuroid.Async.submit;

public class CategoryRepository {

    private List<Category> DEFAULT_CATEGORY_LIST = Arrays.asList(
            new Category(1, "Ahorros", "#4286f4", R.drawable.ic_monetization_on_black_24dp, true, false),
            new Category(2, "Educación", "#70b72a", R.drawable.ic_school_black_24dp, false, false),
            new Category(3, "Diversión", "#9e1f8d", R.drawable.ic_local_bar_black_24dp, false, false),
            new Category(4, "Casa", "#d86800", R.drawable.ic_home_black_24dp, false, false)
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
