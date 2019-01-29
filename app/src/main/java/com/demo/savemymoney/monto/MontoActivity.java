package com.demo.savemymoney.monto;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;

public class MontoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monto);
        Toolbar toolbar = findViewById(R.id.income_toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MontoFragment newFragment = new MontoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frame_container, newFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
