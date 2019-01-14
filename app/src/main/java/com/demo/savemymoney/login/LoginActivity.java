package com.demo.savemymoney.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.ViewPagerAdapter;
import com.demo.savemymoney.signup.SignUpFragment;

public class LoginActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        viewPager = findViewById(R.id.login_view_pager);
        tabLayout = findViewById(R.id.login_tab_layout);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(LoginFragment.newInstance(), getString(R.string.login));
        adapter.addFragment(SignUpFragment.newInstance(), getString(R.string.sign_up));
        viewPager.setAdapter(adapter);
    }
}
