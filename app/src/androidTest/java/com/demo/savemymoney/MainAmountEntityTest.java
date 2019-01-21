package com.demo.savemymoney;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.demo.savemymoney.data.dao.MainAmountDao;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.MainAmount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MainAmountEntityTest {
    private MainAmountDao dao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = db.mainAmountDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void saveMainAmount() {
        MainAmount mainAmount = new MainAmount();
        mainAmount.userUID = "TEST_UID";
        mainAmount.amount = 50000000.00;
        dao.saveMainAmount(mainAmount);
        MainAmount result = dao.findByUserUID(mainAmount.userUID);
        assertThat(result.userUID, equalTo(mainAmount.userUID));
    }
}
