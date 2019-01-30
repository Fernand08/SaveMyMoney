package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(primaryKeys = {"userUID", "categoryId"})
public class Category {
    @NonNull
    public String userUID;
    @NonNull
    public Integer categoryId;
    public String name;
    public String color;
    public Double distributedAmount;
    public Boolean isSaving;
    public Boolean isDeletable;

    public Category(@NonNull Integer categoryId, String name, String color, Boolean isSaving, Boolean isDeletable) {
        this.categoryId = categoryId;
        this.name = name;
        this.color = color;
        this.isSaving = isSaving;
        this.isDeletable = isDeletable;
    }
}
