package com.demo.savemymoney.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.CategoryDetailHistory;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDetailHistoryDao {
    @Insert(onConflict = REPLACE)
    void saveDetail(CategoryDetailHistory detail);

    @Query("Select * from CategoryDetailHistory where userUID = :userUID and categoryId = :categoryId and month(date) = :month order by date desc")
    LiveData<List<CategoryDetailHistory>> findAll(String userUID, Integer categoryId, Integer month);

    @Delete
    void deleteDetail(CategoryDetailHistory detail);
}
