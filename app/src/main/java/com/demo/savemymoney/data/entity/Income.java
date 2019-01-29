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

    @NonNull
    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(@NonNull String userUID) {
        this.userUID = userUID;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }


}
