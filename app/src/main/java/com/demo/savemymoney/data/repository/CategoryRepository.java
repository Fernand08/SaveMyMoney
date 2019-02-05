package com.demo.savemymoney.data.repository;

import android.content.Context;

import com.demo.savemymoney.common.exceptions.CategoryInvalidAmountException;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.MainAmount;
import com.github.clemp6r.futuroid.Future;

import java.util.Arrays;
import java.util.List;

import static com.github.clemp6r.futuroid.Async.submit;
import static java.lang.Math.abs;

public class CategoryRepository {

    private List<Category> DEFAULT_CATEGORY_LIST = Arrays.asList(
            new Category(1, "Ahorros", "#4286f4", 296, true, false),
            new Category(2, "Educación", "#70b72a", 64, false, false),
            new Category(3, "Diversión", "#9e1f8d", 419, false, false),
            new Category(4, "Casa", "#d86800", 470, false, false),
            new Category(5, "Alimentos", "#fcbd14", 430, false, false),
            new Category(6, "Otros", "#6a707a", 935, false, false)
    );

    private AppDatabase database;

    public CategoryRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<Integer> countCategories(String userUID) {
        return submit(() -> database.categoryDao().countCategories(userUID));
    }

    public Future<Category> findByUserUIDAndCategoryId(String userUID, Integer categoryId) {
        return submit(() -> database.categoryDao().findByUserUIDAndCategoryId(userUID, categoryId));
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

    public Future<Void> increaseDistributedAmount(String userUID, Integer categoryId, Double amount) {
        return submit(() -> {
            increaseAmount(userUID, categoryId, amount);
            return null;
        });
    }

    private void increaseAmount(String userUID, Integer categoryId, Double amount) {
        MainAmount mainAmount = database.mainAmountDao().findByUserUID(userUID);
        if (amount > mainAmount.amount)
            throw new CategoryInvalidAmountException();
        else {
            database.mainAmountDao().decreaseAmount(userUID, amount);
            database.categoryDao().increaseAmount(userUID, categoryId, amount);
            database.categoryDao().addDistributedAmountReference(userUID, categoryId, amount);
        }
    }

    public Future<Void> decreaseDistributedAmount(String userUID, Integer categoryId, Double amount) {
        return submit(() -> {
            decreaseAmount(userUID, categoryId, amount);
            return null;
        });
    }

    private void decreaseAmount(String userUID, Integer categoryId, Double amount) {
        database.mainAmountDao().increaseAmount(userUID, amount);
        database.categoryDao().decreaseAmount(userUID, categoryId, amount);
    }

    public Future<Void> changeDistributedAmount(String userUID, Integer categoryId, Double amount) {
        return submit(() -> {
            Category category = database.categoryDao().findByUserUIDAndCategoryId(userUID, categoryId);
            if ((category.distributedAmount - amount) >= 1)
                decreaseAmount(userUID, categoryId, amount);
            else
                increaseAmount(userUID, categoryId, abs(category.distributedAmount - amount));
            return null;
        });
    }
}
