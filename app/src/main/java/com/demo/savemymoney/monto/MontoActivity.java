package com.demo.savemymoney.monto;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;


import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.data.dao.IncomeDao;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Income;

import com.demo.savemymoney.data.repository.IncomeRepository;
import com.demo.savemymoney.login.LoginActivity;
import com.github.clemp6r.futuroid.FutureCallback;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MontoActivity extends BaseActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monto);

        MontoFragment montoFragment = new MontoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.id_fragmente_layout,montoFragment,null);
        fragmentTransaction.commit();





    }
  @Override
    protected  void  onStart() {


        super.onStart();


    }




    public void  signOut(View view){
        mAuth.signOut();
        goTo(LoginActivity.class);
    }


}
