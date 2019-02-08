package com.demo.savemymoney.graphics;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.tuples.CategoryPieTuple;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT;
import static com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL;
import static com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP;

public class CategoryPieFragment extends BaseFragment implements CategoryPieFragmentPresenter.View {

    @BindView(R.id.pie_chart)
    PieChart pieChart;

    private CategoryPieFragmentPresenter presenter;

    public static CategoryPieFragment newInstance() {
        CategoryPieFragment fragment = new CategoryPieFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_pie, container, false);
        ButterKnife.bind(this, view);
        presenter = new CategoryPieFragmentPresenter(this, getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initChart();
        presenter.loadPieData();
    }

    private void fillData(List<CategoryPieTuple> result) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        if (result == null || result.isEmpty()) return;

        for (CategoryPieTuple tuple : result) {
            entries.add(new PieEntry(tuple.amount.floatValue(), tuple.name));
            colors.add(Color.parseColor(tuple.color));
        }

        PieDataSet set = new PieDataSet(entries, "Categorías");
        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new PercentFormatter());

        data.setValueTextSize(12f);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void initChart() {
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(TOP);
        legend.setHorizontalAlignment(RIGHT);
        legend.setOrientation(VERTICAL);
        legend.setDrawInside(false);

        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(15f);
        pieChart.setNoDataText(getString(R.string.graphic_no_data_text));
        pieChart.invalidate();
    }

    @Override
    public void showPieData(List<CategoryPieTuple> result) {
        fillData(result);
    }
}
