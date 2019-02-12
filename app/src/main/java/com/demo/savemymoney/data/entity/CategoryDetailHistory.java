package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Date;

@Entity(primaryKeys = {"userUID", "categoryId", "detailId"})
public class CategoryDetailHistory {
    @NonNull
    public String userUID;
    @NonNull
    public Integer categoryId;
    @NonNull
    public Integer detailId;
    public String description;
    public Double amount;
    public Date date;
    @Ignore
    public Category category;

    public static CategoryDetailHistory fromDetail(CategoryDetail detail) {
        Gson gson = new Gson();
        String jsonDetail = gson.toJson(detail);
        return gson.fromJson(jsonDetail, CategoryDetailHistory.class);
    }
}
