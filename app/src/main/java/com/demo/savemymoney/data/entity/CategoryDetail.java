package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"userUID", "categoryId", "detailId"},
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = {"userUID", "categoryId"},
                        childColumns = {"userUID", "categoryId"},
                        onDelete = CASCADE)
        })
public class CategoryDetail {
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
