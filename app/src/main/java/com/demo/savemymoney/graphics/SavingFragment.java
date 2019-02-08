package com.demo.savemymoney.graphics;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;

public class SavingFragment extends BaseFragment {
    public static SavingFragment newInstance() {
        SavingFragment fragment = new SavingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saving, container, false);
    }

}
