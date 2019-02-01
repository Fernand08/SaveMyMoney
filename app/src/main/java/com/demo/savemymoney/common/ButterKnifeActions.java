package com.demo.savemymoney.common;

import android.support.design.widget.TextInputLayout;

import butterknife.ButterKnife;

public class ButterKnifeActions {
    public static final ButterKnife.Action<TextInputLayout> CLEAR_ERROR = (view, index) -> {
        view.setError("");
        view.setErrorEnabled(false);
    };
}
