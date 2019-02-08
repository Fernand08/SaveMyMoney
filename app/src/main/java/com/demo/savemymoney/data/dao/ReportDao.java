package com.demo.savemymoney.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.demo.savemymoney.data.tuples.CategoryPieTuple;

import java.util.List;

@Dao
public interface ReportDao {

    @Query("Select ca.name,ca.color,SUM(de.amount) as amount from Category ca join CategoryDetail de on ca.categoryId = de.categoryId and ca.userUID =  de.userUID where ca.userUID = :userUID group by ca.name order by amount desc")
    List<CategoryPieTuple> categoryPieList(String userUID);
}
