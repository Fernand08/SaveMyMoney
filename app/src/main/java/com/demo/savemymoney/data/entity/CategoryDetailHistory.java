package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

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
}
