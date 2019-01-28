package com.demo.savemymoney.monto;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;


import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
        setContentView(R.layout.fragment_monto);

       /* MontoFragment montoFragment = new MontoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        */
        FragmentManager fragmentManager = getSupportFragmentManager();
        MontoFragment montoFragment = (MontoFragment) fragmentManager.findFragmentById(R.id.id_montofragment);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if(montoFragment == null || montoFragment.isRemoving()){
            montoFragment = new MontoFragment();
            fragmentTransaction.add(R.id.id_montofragment,montoFragment,null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            Log.e("Abstract","Done");
        }








    }





    public void  signOut(View view){
        mAuth.signOut();
        goTo(LoginActivity.class);
    }


}
