package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Income {
    @PrimaryKey
    @NonNull
    public String userUID;
    public Double amount;
    public Date startDate;
    public String period;
    public Date payDate;
}
