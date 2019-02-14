package com.demo.savemymoney.graphics;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.entity.SavingHistory;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.savemymoney.common.util.NumberFormatUtils.formatAsCurrency;
import static com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM;

public class SavingFragment extends BaseFragment implements SavingFragmentPresenter.View {

    @BindView(R.id.bar_chart)
    BarChart savingBarChart;
    private SavingFragmentPresenter presenter;

    public static SavingFragment newInstance() {
        SavingFragment fragment = new SavingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saving, container, false);
        ButterKnife.bind(this, view);
        presenter = new SavingFragmentPresenter(this, getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initChart();
        presenter.loadBarData();
    }

    private void initChart() {
        savingBarChart.getLegend().setEnabled(false);
        savingBarChart.getDescription().setEnabled(false);
        savingBarChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        savingBarChart.setNoDataText(getString(R.string.graphic_no_data_text));
        savingBarChart.setFitBars(true);
        savingBarChart.setPinchZoom(false);
        XAxis xAxis = savingBarChart.getXAxis();
        xAxis.setPosition(BOTTOM);
        xAxis.setGranularity(1);
        savingBarChart.getAxisLeft().setValueFormatter((value, axis) -> formatAsCurrency(new Float(value).doubleValue()));
        savingBarChart.getAxisRight().setEnabled(false);
    }

    @Override
    public void showBarData(List<SavingHistory> result) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM");

        for (long i = 0; i < result.size(); i++) {
            SavingHistory history = result.get((int) i);
            entries.add(new BarEntry(i, history.amount.floatValue()));
            xLabels.add(dateFormatter.format(history.lastUpdate));
        }

        BarDataSet set = new BarDataSet(entries, "");
        set.setColor(getResources().getColor(R.color.md_light_blue_500));
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        data.setValueTextSize(15);
        data.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> formatAsCurrency(new Float(value).doubleValue()));
        savingBarChart.setData(data);
        savingBarChart.getXAxis().setValueFormatter((value, axis) -> xLabels.get((int) value));
        savingBarChart.invalidate();
    }
}
