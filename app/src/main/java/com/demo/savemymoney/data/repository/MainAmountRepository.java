package com.demo.savemymoney.data.repository;

import android.content.Context;

import com.demo.savemymoney.common.exceptions.IncomeNotFoundException;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.data.entity.SavingHistory;
import com.github.clemp6r.futuroid.Future;

import java.util.Date;

import static com.demo.savemymoney.common.util.DateUtils.isSameDay;
import static com.github.clemp6r.futuroid.Async.submit;

public class MainAmountRepository {

    private AppDatabase database;

    public MainAmountRepository(Context context) {
        database = AppDatabase.getAppDatabase(context);
    }

    public Future<Void> save(MainAmount mainAmount) {
        return submit(() -> {
            database.mainAmountDao().saveMainAmount(mainAmount);
            return null;
        });
    }

    public Future<MainAmount> findByUserUID(String userUID) {
        return submit(() -> {
            MainAmount firstSearch = database.mainAmountDao().findByUserUID(userUID);
            if (firstSearch != null)
                return firstSearch;
            else {
                MainAmount defaultAmount = MainAmount.defaultAmount(userUID);
                database.mainAmountDao().saveMainAmount(defaultAmount);
                SavingHistory savingHistory = new SavingHistory();
                savingHistory.userUID = userUID;
                savingHistory.amount = 0.00;
                savingHistory.lastUpdate = new Date();
                savingHistory.periodNumber = defaultAmount.periodNumber;
                database.savingHistoryDao().save(savingHistory);
                return defaultAmount;
            }
        });
    }

    public Future<Void> increaseAmount(String userUID, Double amount) {
        return submit(() -> {
            checkIfIncomeExists(userUID);
            database.mainAmountDao().increaseAmount(userUID, amount);
            return null;
        });
    }

    private void checkIfIncomeExists(String userUID) {
        Income income = database.incomeDao().findByUserUID(userUID);
        if (income == null)
            throw new IncomeNotFoundException();
    }

    public Future<Void> decreaseAmount(String userUID, Double amount) {
        return submit(() -> {
            database.mainAmountDao().decreaseAmount(userUID, amount);
            return null;
        });
    }

    public Future<Void> changeAmount(String userUID, Double amount) {
        return submit(() -> {
            checkIfIncomeExists(userUID);
            database.mainAmountDao().changeAmount(userUID, amount);
            return null;
        });
    }

    public Future<Income> increaseByIncome(String userUID) {
        return submit(() -> {
            Income income = database.incomeDao().findByUserUID(userUID);
            if (income.payDate != null && isSameDay(income.payDate, new Date()))
                return income;
            income.payDate = new Date();
            database.incomeDao().updateIncome(income);
            database.mainAmountDao().increaseAmount(userUID, income.amount);
            return income;
        });
    }
}
