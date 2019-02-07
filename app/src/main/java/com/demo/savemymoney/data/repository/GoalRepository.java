package com.demo.savemymoney.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.github.clemp6r.futuroid.Future;

import static com.demo.savemymoney.common.AppConstants.DATABASE_NAME;
import static com.github.clemp6r.futuroid.Async.submit;

public class GoalRepository {

    private AppDatabase database;

    public GoalRepository(Context context){
        database = Room.databaseBuilder(context, AppDatabase.class , DATABASE_NAME).build();
    }
    public Future<Void> save (Goal goal){
        return submit(() -> {
            database.goalDao().saveGoal(goal);
            return null;
        });
    }
    public Future<Category> findAMountByUserUID(String userUID, Boolean b) {
        return submit(() -> {
        return database.goalDao().findAmountByUserUID(userUID,b);

        });
    }
    public Future<Goal> getGoal(String userUID){
        return submit(() -> {
            return database.goalDao().findByUserUID(userUID);
        });
    }

    public Future<Object> deleteGoal(Goal goal){

        return  submit(() -> {
            database.goalDao().deleteGoal(goal);

           return  null;
        });
    }





}
