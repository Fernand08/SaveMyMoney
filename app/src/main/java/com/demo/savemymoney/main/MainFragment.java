package com.demo.savemymoney.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.entity.Category;

import java.util.List;

public class MainFragment extends BaseFragment implements MainFragmentPresenter.View {


    MainFragmentPresenter presenter;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new MainFragmentPresenter(this, getContext());
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getCategoryList();

    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.main_category_loading);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showCategoryList(List<Category> result) {

        LinearLayout parentLayout = getActivity().findViewById(R.id.categories_container);
        LayoutInflater layoutInflater = getLayoutInflater();

        for (Category category : result) {
            layoutInflater.inflate(R.layout.categories_layout, parentLayout, false);
            Button button = new Button(getContext());
            button.setText(category.name);
            parentLayout.addView(button);
        }

    }
}
