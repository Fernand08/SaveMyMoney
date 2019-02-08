package com.demo.savemymoney.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.demo.savemymoney.data.dao.CategoryDao;
import com.demo.savemymoney.data.dao.CategoryDetailDao;
import com.demo.savemymoney.data.dao.GoalDao;
import com.demo.savemymoney.data.dao.IncomeDao;
import com.demo.savemymoney.data.dao.MainAmountDao;
import com.demo.savemymoney.data.dao.ReportDao;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.entity.MainAmount;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;

@Database(entities = {
        Income.class,
        MainAmount.class,
        Category.class,
        CategoryDetail.class,
        CategoryDetailHistory.class,
        Goal.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract MainAmountDao mainAmountDao();

    public abstract IncomeDao incomeDao();

    public abstract CategoryDao categoryDao();

    public abstract CategoryDetailDao categoryDetailDao();

    public abstract ReportDao reportDao();

    public abstract GoalDao goalDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null)
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("Alter table Category add distributedAmountReference REAL");
        }
    };
}
