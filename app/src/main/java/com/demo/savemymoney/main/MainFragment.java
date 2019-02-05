package com.demo.savemymoney.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.category.CategoryFragment;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.adapters.CategoryAdapter;
import com.demo.savemymoney.common.components.AmountEditor;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.monto.MontoFragment;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainFragment extends BaseFragment implements MainFragmentPresenter.View, AmountEditor.OnAmountChangeListener, CategoryAdapter.CategoryActionsListener {

    @BindView(R.id.main_amount_editor)
    AmountEditor amountEditor;

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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        amountEditor.setOnAmountChangeListener(this);
        presenter.getCategoryList();
        presenter.getMainAmount();
        getActivity().setTitle(R.string.main_income_title);
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
        GridView categoriesGrid = getActivity().findViewById(R.id.categories_container);
        Category categoryForAdd = new Category();
        categoryForAdd.isAddOption = true;
        result.add(categoryForAdd);
        categoriesGrid.setAdapter(new CategoryAdapter(result, getContext(), this));
    }

    @Override
    public void showMainAmount(MainAmount result) {
        amountEditor.setAmount(result.amount);
    }

    @Override
    public void notifyIncomeNotFound() {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.error_alert_title))
                .setContentText(getString(R.string.main_income_not_exist_message))
                .setConfirmText(getString(R.string.main_income_confirm_register))
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    goTo(MontoFragment.newInstance(), R.id.content_frame);
                });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void onIncreaseAmount(BigDecimal amount) {
        presenter.increaseAmount(amount);
    }

    @Override
    public void onDecreaseAmount(BigDecimal amount) {
        presenter.decreaseAmount(amount);
    }

    @Override
    public void onChangeAmount(BigDecimal amount) {
        presenter.changeAmount(amount);
    }

    @Override
    public void onSelectCategory(Category category) {
        goTo(CategoryFragment.newInstance(category), R.id.content_frame);
    }

    @Override
    public void onAdd() {
        Toast.makeText(getContext(), "Add new category!", Toast.LENGTH_SHORT).show();
    }
}
