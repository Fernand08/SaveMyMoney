package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface GoalDao {


    @Query("Select * from Category where userUID = :userUID and isSaving = :bolean  ")
    Category findAmountByUserUID(String userUID, Boolean bolean);

    @Query("Update Category set distributedAmount = distributedAmount- :amount  where userUID = :userUID and isSaving = :bolean ")
    void decreaseAmountSaving(String userUID,Boolean bolean,Double amount);



    @Query("Select * from Goal where userUID = :userUID")
    Goal findByUserUID(String userUID);

    @Insert(onConflict = REPLACE)
    void saveGoal(Goal goal);

    @Delete
    void deleteGoal(Goal goal);

    @Query("DELETE FROM Goal WHERE userUID = :userUID")
    void deleteGoalSaving(String userUID);
}
