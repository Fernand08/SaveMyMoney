package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void save(List<Category> category);

    @Insert
    void save(Category category);

    @Query("Select * from Category where userUID = :userUID")
    List<Category> getAll(String userUID);

    @Query("Select count(categoryId) from Category where userUID = :userUID")
    Integer countCategories(String userUID);
}
