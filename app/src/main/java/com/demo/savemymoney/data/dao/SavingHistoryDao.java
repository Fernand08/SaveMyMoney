package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.SavingHistory;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SavingHistoryDao {

    @Insert(onConflict = REPLACE)
    void save(SavingHistory savingHistory);

    @Query("Select * from SavingHistory where userUID = :userUID")
    List<SavingHistory> getAll(String userUID);

    @Query("Select max(periodNumber) from SavingHistory where userUID= :userUID")
    Integer getLastPeriod(String userUID);
}
