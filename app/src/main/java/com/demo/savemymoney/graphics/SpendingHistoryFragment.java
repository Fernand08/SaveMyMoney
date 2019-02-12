package com.demo.savemymoney.graphics;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpendingHistoryFragment extends BaseFragment implements SpendingHistoryFragmentPresenter.View {

    @BindView(R.id.history_rv)
    RecyclerView historyRecycler;
    private SpendingHistoryAdapter historyAdapter;
    private SpendingHistoryFragmentPresenter presenter;

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
        presenter.findByMonth(2);
    }

    @Override
    public void updateHistoryList(List<CategoryDetailHistory> result) {
        historyAdapter.setData(result);
    }
}
