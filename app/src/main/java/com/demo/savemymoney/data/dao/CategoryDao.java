package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void save(List<Category> categories);

    @Insert
    void save(Category category);

    @Query("Select * from Category where userUID = :userUID")
    List<Category> getAll(String userUID);

    @Query("Select * from Category where userUID = :userUID and categoryId = :categoryId")
    Category findByUserUIDAndCategoryId(String userUID, Integer categoryId);

    @Query("Select count(categoryId) from Category where userUID = :userUID")
    Integer countCategories(String userUID);

    @Query("Update Category set distributedAmount = distributedAmount + :amount where userUID = :userUID and categoryId = :categoryId")
    void increaseAmount(String userUID, Integer categoryId, Double amount);

    @Query("Update Category set distributedAmount = distributedAmount - :amount where userUID = :userUID and categoryId = :categoryId")
    void decreaseAmount(String userUID, Integer categoryId, Double amount);

    @Query("Update Category set distributedAmount = :amount where userUID = :userUID and categoryId = :categoryId")
    void changeAmount(String userUID, Integer categoryId, Double amount);

    @Query("Update Category set distributedAmountReference = IFNULL(distributedAmountReference,0) + :amount where userUID = :userUID and categoryId = :categoryId")
    void addDistributedAmountReference(String userUID, Integer categoryId, Double amount);

    @Query("Select ifnull(max(categoryId),0)+1 from Category where userUID = :userUID")
    Integer newCategoryId(String userUID);

    @Query("Select count(categoryId) from Category where userUID = :userUID and upper(name) = trim(upper(:name))")
    Integer countByName(String userUID, String name);
}
