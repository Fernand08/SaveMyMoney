package com.demo.savemymoney.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MainAmount {
    @PrimaryKey
    @NonNull
    public String userUID;
    public Double amount;

    public static MainAmount defaultAmount(String userUID) {
        MainAmount mainAmount = new MainAmount();
        mainAmount.userUID = userUID;
        mainAmount.amount = 0.00;
        return mainAmount;
    }
}
