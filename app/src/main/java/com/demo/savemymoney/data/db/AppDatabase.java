package com.demo.savemymoney.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.demo.savemymoney.data.dao.CategoryDao;
import com.demo.savemymoney.data.dao.MainAmountDao;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.entity.MainAmount;

@Database(entities = {
        Income.class,
        MainAmount.class,
        Category.class,
        CategoryDetail.class,
        CategoryDetailHistory.class
}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainAmountDao mainAmountDao();

    public abstract CategoryDao categoryDao();
}
