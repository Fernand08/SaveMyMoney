package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(primaryKeys = {"userUID", "categoryId"})
public class Category implements Serializable {
    @NonNull
    public String userUID;
    @NonNull
    public Integer categoryId;
    public String name;
    public String color;
    public Integer icon;
    public Double distributedAmount;
    public Double distributedAmountReference;
    public Boolean isSaving;
    public Boolean isDeletable;
    @Ignore
    public boolean isAddOption;

    @Ignore
    public Category(@NonNull Integer categoryId, String name, String color, Integer icon, Boolean isSaving, Boolean isDeletable) {
        this.categoryId = categoryId;
        this.name = name;
        this.color = color;
        this.isSaving = isSaving;
        this.isDeletable = isDeletable;
        this.icon = icon;
        this.distributedAmount = 0.00;
        this.distributedAmountReference = 0.00;
    }

    public Category() {
    }
}
