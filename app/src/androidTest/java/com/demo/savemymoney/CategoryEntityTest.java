package com.demo.savemymoney;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.demo.savemymoney.data.dao.CategoryDao;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class CategoryEntityTest {
    private CategoryDao dao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = db.categoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void saveCategory() {

        Category category = new Category();
        category.userUID = "TEST_UID";
        category.isSaving = true;
        category.categoryId = 1;
        category.color = "ffffff";
        category.distributedAmount = 1900.87;
        dao.save(Collections.singletonList(category));

        dao.getAll(category.userUID);
    }
}
