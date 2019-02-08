package com.demo.savemymoney.graphics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;

public class SpendingHistoryFragment extends BaseFragment {
    public static SpendingHistoryFragment newInstance() {
        SpendingHistoryFragment fragment = new SpendingHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spending_history, container, false);
    }

}
