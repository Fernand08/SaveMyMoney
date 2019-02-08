package com.demo.savemymoney.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.category.CategoryDialogFragment;
import com.demo.savemymoney.category.CategoryFragment;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.adapters.CategoryAdapter;
import com.demo.savemymoney.common.components.AmountEditor;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.monto.MontoFragment;
import com.maltaisn.icondialog.IconHelper;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static cn.pedant.SweetAlert.SweetAlertDialog.SUCCESS_TYPE;
import static cn.pedant.SweetAlert.SweetAlertDialog.WARNING_TYPE;

public class MainFragment extends BaseFragment implements MainFragmentPresenter.View, AmountEditor.OnAmountChangeListener, CategoryAdapter.CategoryActionsListener, CategoryDialogFragment.AddCategoryListener {

    @BindView(R.id.main_amount_editor)
    AmountEditor amountEditor;

    @BindView(R.id.categories_container)
    GridView categoriesGrid;

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
        presenter.getMainAmount();
        getActivity().setTitle(R.string.main_income_title);
        IconHelper.getInstance(getContext()).addLoadCallback(() -> presenter.getCategoryList());
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
    public void notifyCategoryDeleted() {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SUCCESS_TYPE)
                .setTitleText(getString(R.string.success_title))
                .setContentText(getString(R.string.category_deleted_message))
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    presenter.getCategoryList();
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
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);
        AppCompatDialogFragment dialogFragment = CategoryDialogFragment.newInstance(this);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void onDeleteCategory(Category category) {
        new SweetAlertDialog(getContext(), WARNING_TYPE)
                .setTitleText(getString(R.string.app_caution))
                .setContentText(String.format(getString(R.string.category_delete_confirm_message_fmt), category.name))
                .setCancelText(getString(R.string.app_no))
                .setConfirmText(getString(R.string.category_delete_confirm))
                .setConfirmClickListener(sweetAlertDialog -> {
                    presenter.deleteCategory(category);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .show();
    }

    @Override
    public void onCategoryAdded() {
        presenter.getCategoryList();
    }
}
