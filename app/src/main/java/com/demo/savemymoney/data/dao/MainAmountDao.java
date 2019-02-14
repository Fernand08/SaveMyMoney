package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.MainAmount;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainAmountDao {
    @Query("Select * from MainAmount where userUID = :userUID")
    MainAmount findByUserUID(String userUID);

    @Insert(onConflict = REPLACE)
    void saveMainAmount(MainAmount mainAmount);

    @Query("Update MainAmount set amount = amount + :amount where userUID = :userUID")
    void increaseAmount(String userUID, Double amount);

    @Query("Update MainAmount set amount = amount - :amount where userUID = :userUID")
    void decreaseAmount(String userUID, Double amount);

    @Query("Update MainAmount set amount = :amount where userUID = :userUID")
    void changeAmount(String userUID, Double amount);

    @Query("Update MainAmount set periodNumber = periodNumber + 1 where userUID = :userUID")
    void increasePeriod(String userUID);
}
