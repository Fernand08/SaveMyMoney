package com.demo.savemymoney.common.components;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.kolotnev.formattedittext.CurrencyEditText;

import static cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE;

public class AmountEditor extends LinearLayout {

    @BindView(R.id.amount_editor_mount_tv)
    public TextView amountTextView;
    private OnAmountChangeListener onAmountChangeListener;
    private Double currentAmount;

    public AmountEditor(Context context) {
        super(context, null);
        init(context);
    }

    public AmountEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public AmountEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AmountEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.amount_editor_layout, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.amount_editor_plus, R.id.amount_editor_minus, R.id.amount_editor_mount_tv})
    public void onActionPressed(View view) {
        String action;
        if (view.getId() == R.id.amount_editor_plus)
            action = getContext().getString(R.string.amount_editor_accion_add);
        else if (view.getId() == R.id.amount_editor_minus)
            action = getContext().getString(R.string.amount_editor_accion_minus);
        else
            action = getContext().getString(R.string.amount_editor_accion_change);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_amount_editor, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(promptView);
        builder.setTitle(String.format("%s %s", getContext().getString(R.string.amount_editor_title), action));
        builder.setCancelable(false);

        final CurrencyEditText input = promptView.findViewById(R.id.dialog_amount_editor_currency);
        if (view.getId() == R.id.amount_editor_mount_tv)
            input.setValue(BigDecimal.valueOf(currentAmount));

        builder.setPositiveButton(getContext().getString(R.string.amount_editor_dialog_positive), (dialog, which) -> {
            BigDecimal amount = input.getValue();
            if (view.getId() == R.id.amount_editor_plus)
                onIncrease(amount);
            else if (view.getId() == R.id.amount_editor_minus)
                onDecrease(amount);
            else
                onChange(amount);
        });
        builder.setNegativeButton(getContext().getString(R.string.amount_editor_dialog_negative), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void setAmount(Double amount) {
        this.currentAmount = amount;
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(Currency.getInstance("PEN"));
        String result = format.format(amount);
        amountTextView.setText(result);
    }

    public void setOnAmountChangeListener(OnAmountChangeListener listener) {
        this.onAmountChangeListener = listener;
    }

    private void onIncrease(BigDecimal amount) {
        if (onAmountChangeListener != null)
            onAmountChangeListener.onIncreaseAmount(amount);
    }

    private void onDecrease(BigDecimal amount) {

        if (currentAmount < amount.doubleValue()) {
            new SweetAlertDialog(getContext(), ERROR_TYPE)
                    .setTitleText(getContext().getString(R.string.error_alert_title))
                    .setContentText(getContext().getString(R.string.amount_editor_error_decrease))
                    .show();
            return;
        }

        if (onAmountChangeListener != null)
            onAmountChangeListener.onDecreaseAmount(amount);
    }

    private void onChange(BigDecimal amount) {
        if (onAmountChangeListener != null)
            onAmountChangeListener.onChangeAmount(amount);
    }

    public interface OnAmountChangeListener {
        void onIncreaseAmount(BigDecimal amount);

        void onDecreaseAmount(BigDecimal amount);

        void onChangeAmount(BigDecimal amount);
    }
}
