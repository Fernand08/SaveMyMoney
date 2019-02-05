package com.demo.savemymoney.category;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.components.AmountEditor;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.main.MainActivity;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.demo.savemymoney.common.util.NumberFormatUtils.formatAsCurrency;

public class CategoryFragment extends BaseFragment implements CategoryFragmentPresenter.View, AmountEditor.OnAmountChangeListener, CategoryDetailDialogFragment.OnDetailAcceptedListener, CategoryDetailAdapter.OnDeleteButtonClickListener {
    private static final String ARG_CATEGORY_ID = "category";
    private Category category;

    @BindView(R.id.category_amount_editor)
    AmountEditor categoryAmountEditor;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.category_detail_rv)
    RecyclerView recyclerView;

    private CategoryFragmentPresenter presenter;
    private CategoryDetailAdapter categoryDetailAdapter;

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
        configureActivityBar();
        categoryAmountEditor.setAmount(category.distributedAmount);
        categoryAmountEditor.setOnAmountChangeListener(this);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(category.color)));

        categoryDetailAdapter = new CategoryDetailAdapter(getContext(), this);

        presenter.getDetail(category.categoryId).observe(this, details -> categoryDetailAdapter.setData(details));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryDetailAdapter);
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
        if (category.distributedAmount <= 0.00) {
            showChihuanDialog();
            return;
        }

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            ft.remove(prev);

        ft.addToBackStack(null);
        AppCompatDialogFragment dialogFragment = CategoryDetailDialogFragment.newInstance(category, this);
        dialogFragment.show(ft, "dialog");
    }

    private void showChihuanDialog() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Huy!")
                .setContentText("Te quedaste Chihuán \uD83D\uDE22")
                .show();
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
        configureActivityBar();
    }

    @Override
    public void onDetailAccepted(CategoryDetail detail) {
        presenter.saveDetail(detail);
    }

    @Override
    public void onDeleteButtonClicked(CategoryDetail categoryDetail) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.app_caution))
                .setContentText(getString(R.string.category_detail_delete_item))
                .setConfirmText(getString(R.string.app_yes))
                .setCancelText(getString(R.string.app_no))
                .setConfirmClickListener(dialog -> {
                    presenter.deleteDetail(categoryDetail);
                    dialog.dismissWithAnimation();
                })
                .setCancelClickListener(dialog -> dialog.cancel())
                .show();

    }

    public void configureActivityBar() {
        FragmentActivity parentActivity = getActivity();
        if (parentActivity instanceof MainActivity) {
            MainActivity main = (MainActivity) parentActivity;
            main.getSupportActionBar().setTitle(category.name);
            main.getSupportActionBar().setHomeButtonEnabled(true);
            main.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            main.getSupportActionBar().setHomeAsUpIndicator(changeDrawableColor(getContext(), category.icon, Color.WHITE));
            main.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(category.color)));
            categoryAmountEditor.setButtonsColor(Color.parseColor(category.color));
            main.changeDistributed(String.format(getString(R.string.category_assigned_reference_format),
                    formatAsCurrency(category.distributedAmountReference)
            ));
        }
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }
}
