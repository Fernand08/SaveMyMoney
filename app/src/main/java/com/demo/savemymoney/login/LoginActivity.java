package com.demo.savemymoney.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.common.adapters.ViewPagerAdapter;
import com.demo.savemymoney.main.MainActivity;
import com.demo.savemymoney.signup.SignUpFragment;

public class LoginActivity extends BaseActivity {

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

    @Override
    protected void onStart() {
        super.onStart();

        if (isUserSignedIn())
            goTo(MainActivity.class);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(LoginFragment.newInstance(), getString(R.string.login));
        adapter.addFragment(SignUpFragment.newInstance(), getString(R.string.sign_up));
        viewPager.setAdapter(adapter);
    }
}
