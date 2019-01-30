package com.demo.savemymoney.category;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.components.AmountEditor;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryFragment extends BaseFragment implements CategoryFragmentPresenter.View, AmountEditor.OnAmountChangeListener, CategoryDetailDialogFragment.OnDetailAcceptedListener {
    private static final String ARG_CATEGORY_ID = "category";
    private Category category;

    @BindView(R.id.category_amount_editor)
    AmountEditor categoryAmountEditor;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private CategoryFragmentPresenter presenter;

    public static CategoryFragment newInstance(@NonNull Category category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY_ID, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            category = (Category) getArguments().getSerializable(ARG_CATEGORY_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(category.name);
        categoryAmountEditor.setAmount(category.distributedAmount);
        categoryAmountEditor.setOnAmountChangeListener(this);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(category.color)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        presenter = new CategoryFragmentPresenter(this, getContext());
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.fab)
    public void onAddClick() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);
        AppCompatDialogFragment dialogFragment = CategoryDetailDialogFragment.newInstance(category, this);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void onIncreaseAmount(BigDecimal amount) {
        presenter.increaseDistributedAmount(category.categoryId, amount);
    }

    @Override
    public void onDecreaseAmount(BigDecimal amount) {
        presenter.decreaseDistributedAmount(category.categoryId, amount);
    }

    @Override
    public void onChangeAmount(BigDecimal amount) {
        presenter.changeDistributedAmount(category.categoryId, amount);
    }

    @Override
    public void updateCategory(Category result) {
        this.category = result;
        categoryAmountEditor.setAmount(result.distributedAmount);
    }

    @Override
    public void refreshDetailList() {

    }

    @Override
    public void onDetailAccepted(CategoryDetail detail) {
        presenter.saveDetail(detail);
    }
}
