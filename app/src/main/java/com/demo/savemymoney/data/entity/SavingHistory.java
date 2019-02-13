package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(primaryKeys = {"userUID", "periodNumber"})
public class SavingHistory {
    @NonNull
    public String userUID;
    @NonNull
    public Integer periodNumber;
    public Date lastUpdate;
    public Double amount;
}
