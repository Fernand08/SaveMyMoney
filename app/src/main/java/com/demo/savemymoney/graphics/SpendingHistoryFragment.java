package com.demo.savemymoney.graphics;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpendingHistoryFragment extends BaseFragment implements SpendingHistoryFragmentPresenter.View {

    @BindView(R.id.history_rv)
    RecyclerView historyRecycler;
    @BindView(R.id.button_month)
    Button monthPickerButton;
    private SpendingHistoryAdapter historyAdapter;
    private SpendingHistoryFragmentPresenter presenter;

    private Calendar calendar = Calendar.getInstance();

    public static SpendingHistoryFragment newInstance() {
        SpendingHistoryFragment fragment = new SpendingHistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_history, container, false);
        ButterKnife.bind(this, view);
        presenter = new SpendingHistoryFragmentPresenter(this, getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        historyAdapter = new SpendingHistoryAdapter(getContext());
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecycler.setHasFixedSize(true);
        historyRecycler.setItemAnimator(new DefaultItemAnimator());
        historyRecycler.setAdapter(historyAdapter);
        setMonthButtonLabel();
        findHistory();
    }

    private void setMonthButtonLabel() {
        monthPickerButton.setText(
                String.format("Mes: %02d-%d", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
        );
    }

    private void findHistory() {
        presenter.findByMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    @Override
    public void updateHistoryList(List<CategoryDetailHistory> result) {
        historyAdapter.setData(result);
        if (result.isEmpty())
            Snackbar.make(getView(), R.string.history_empty_result, Snackbar.LENGTH_LONG)
                    .show();
    }

    @OnClick(R.id.button_month)
    public void onButtonMonthPressed() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                (selectedMonth, selectedYear) -> onMonthYearSelected(selectedMonth, selectedYear), 1, 1);
        builder.setMinYear(1990)
                .setActivatedYear(calendar.get(Calendar.YEAR))
                .setTitle(getString(R.string.year_month_picker_dialogo_title))
                .build()
                .show();
    }

    private void onMonthYearSelected(int selectedMonth, int selectedYear) {
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        setMonthButtonLabel();
        findHistory();
    }
}
