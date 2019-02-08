package com.demo.savemymoney.common;

import android.support.v7.app.AppCompatDialogFragment;

import com.demo.savemymoney.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseDialogFragment extends AppCompatDialogFragment {


    public void showError(String message) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.error_alert_title))
                .setContentText(message)
                .show();
    }

}
