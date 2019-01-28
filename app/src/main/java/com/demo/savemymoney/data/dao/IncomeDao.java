package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.Income;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IncomeDao {
    @Insert(onConflict = REPLACE)
    void saveIncome(Income income);

    @Query("Select * from Income where userUID=:userUID")
    Income findByUserUID(String userUID);
}
