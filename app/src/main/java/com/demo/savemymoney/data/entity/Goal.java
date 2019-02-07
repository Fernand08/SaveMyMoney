package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(primaryKeys = {"userUID","goalId"})
public class Goal implements Serializable {
    @NonNull
    public String userUID;
    @NonNull
    public Integer goalId;
    public Double amountGoal;
    public String description;

}
