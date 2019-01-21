package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.demo.savemymoney.data.entity.MainAmount;

@Dao
public interface MainAmountDao {
    @Query("Select * from MainAmount where userUID = :userUID")
    MainAmount findByUserUID(String userUID);

    @Insert
    void saveMainAmount(MainAmount mainAmount);

    @Update
    void updateMainAmount(MainAmount mainAmount);
}
