package com.demo.savemymoney.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.CategoryDetail;

import java.util.Date;
import java.util.List;

@Dao
public interface CategoryDetailDao {
    @Insert
    void saveDetail(CategoryDetail detail);

    @Query("Select ifnull(max(detailId),0)+1 from CategoryDetail where userUID = :userUID and categoryId = :categoryId")
    Integer getNewId(String userUID, Integer categoryId);

    @Query("Select * from CategoryDetail where userUID = :userUID and categoryId = :categoryId order by date desc")
    LiveData<List<CategoryDetail>> findAll(String userUID, Integer categoryId);


    @Query("Select  count(detailId) from CategoryDetail where userUID = :userUID and date = :date")
    Integer getCountDetails(String userUID, Date date);


    @Delete
    void deleteDetail(CategoryDetail detail);
}
