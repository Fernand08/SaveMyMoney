package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.CategoryDetail;

@Dao
public interface CategoryDetailDao {
    @Insert
    void saveDetail(CategoryDetail detail);

    @Query("Select ifnull(max(detailId),0)+1 from CategoryDetail where userUID = :userUID and categoryId = :categoryId")
    Integer getNewId(String userUID, Integer categoryId);
}
