package com.demo.savemymoney.graphics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.common.adapters.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphicsActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        ButterKnife.bind(this);
        setupViewPager();
        setTitle(R.string.menu_reports);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CategoryPieFragment.newInstance(), getString(R.string.graphic_category_pie_title));
        adapter.addFragment(SpendingHistoryFragment.newInstance(), getString(R.string.graphic_spending_history_title));
        adapter.addFragment(SavingFragment.newInstance(), getString(R.string.graphic_saving_title));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
