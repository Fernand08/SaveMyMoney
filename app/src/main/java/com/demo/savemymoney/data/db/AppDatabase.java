package com.demo.savemymoney.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.demo.savemymoney.data.dao.CategoryDao;
import com.demo.savemymoney.data.dao.CategoryDetailDao;
import com.demo.savemymoney.data.dao.GoalDao;
import com.demo.savemymoney.data.dao.CategoryDetailHistoryDao;
import com.demo.savemymoney.data.dao.IncomeDao;
import com.demo.savemymoney.data.dao.MainAmountDao;
import com.demo.savemymoney.data.dao.ReportDao;
import com.demo.savemymoney.data.dao.SavingHistoryDao;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.data.entity.SavingHistory;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;

@Database(entities = {
        Income.class,
        MainAmount.class,
        Category.class,
        CategoryDetail.class,
        CategoryDetailHistory.class,
        Goal.class,
        SavingHistory.class
}, version = 6)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract MainAmountDao mainAmountDao();

    public abstract IncomeDao incomeDao();

    public abstract CategoryDao categoryDao();

    public abstract CategoryDetailDao categoryDetailDao();

    public abstract CategoryDetailHistoryDao categoryDetailHistoryDao();

    public abstract ReportDao reportDao();

    public abstract GoalDao goalDao();

    public abstract SavingHistoryDao savingHistoryDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null)
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
