package com.demo.savemymoney.category;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.components.AmountEditor;
import com.demo.savemymoney.data.entity.Category;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends BaseFragment implements CategoryFragmentPresenter.View, AmountEditor.OnAmountChangeListener {
    private static final String ARG_CATEGORY_ID = "category";
    private Category category;

    @BindView(R.id.category_amount_editor)
    AmountEditor categoryAmountEditor;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        presenter = new CategoryFragmentPresenter(this, getContext());
        ButterKnife.bind(this, view);
        return view;
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
}
