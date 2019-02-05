package com.demo.savemymoney.category;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.maltaisn.icondialog.IconHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.kolotnev.formattedittext.CurrencyEditText;

import static java.math.BigDecimal.ZERO;

public class CategoryDetailDialogFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_CATEGORY = "category";
    private Category category;

    @BindView(R.id.category_detail_dialog_header)
    LinearLayout header;

    @BindView(R.id.category_detail_dialog_header_icon)
    ImageView icon;

    @BindView(R.id.category_detail_dialog_header_title)
    TextView title;

    @BindView(R.id.date_edt)
    EditText date;

    @BindView(R.id.description_edt)
    EditText description;

    @BindView(R.id.amount_edt)
    CurrencyEditText amountEdt;

    @BindView(R.id.amount_til)
    TextInputLayout amountTil;

    private OnDetailAcceptedListener listener;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static CategoryDetailDialogFragment newInstance(@NonNull Category category, OnDetailAcceptedListener listener) {
        CategoryDetailDialogFragment fragment = new CategoryDetailDialogFragment();
        fragment.setOnDetailAcceptedListener(listener);
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            category = (Category) getArguments().getSerializable(ARG_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_detail_dialog, container, false);

        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        header.setBackgroundColor(Color.parseColor(category.color));
        icon.setImageDrawable(IconHelper.getInstance(getContext()).getIcon(category.icon).getDrawable(getContext()));
        title.setText(category.name);
        date.setText(format.format(new Date()));
    }

    @OnClick(R.id.date_edt)
    public void onDateClicked() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date.setText(format.format(c.getTime()));
    }

    @OnClick({R.id.accept_button, R.id.cancel_button})
    public void onActionSelected(View view) {
        if (view.getId() == R.id.cancel_button)
            dismiss();
        else {
            amountTil.setErrorEnabled(false);
            amountTil.setError("");
            if (amountEdt.getValue().compareTo(ZERO) <= 0) {
                amountTil.setErrorEnabled(true);
                amountTil.setError(getString(R.string.category_detail_amount_invalid));
            } else if (amountEdt.getValue().doubleValue() > category.distributedAmount) {
                amountTil.setErrorEnabled(true);
                amountTil.setError(getString(R.string.category_detail_amount_overflow));
            } else {
                dismiss();
                createAndSendDetail();
            }

        }
    }

    private void createAndSendDetail() {
        CategoryDetail detail = new CategoryDetail();
        detail.amount = amountEdt.getValue().doubleValue();
        detail.categoryId = category.categoryId;
        try {
            detail.date = format.parse(date.getText().toString());
        } catch (ParseException e) {
            Log.e(getClass().getName(), "Error parsing date", e);
            detail.date = new Date();
        }

        //Fix get actual time
        Calendar actualCalendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(detail.date);
        c.set(Calendar.HOUR_OF_DAY, actualCalendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, actualCalendar.get(Calendar.MINUTE));

        detail.date = c.getTime();

        detail.description = description.getText().toString();
        if (listener != null)
            listener.onDetailAccepted(detail);
    }

    public void setOnDetailAcceptedListener(OnDetailAcceptedListener listener) {
        this.listener = listener;
    }

    public interface OnDetailAcceptedListener {
        void onDetailAccepted(CategoryDetail detail);
    }


}
