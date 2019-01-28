package com.demo.savemymoney.common;

import android.support.v4.app.Fragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseFragment extends Fragment {
    private SweetAlertDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setCancelable(false);
    }

    public void showProgressDialog(int resIdMessage) {
        progressDialog.setTitle(resIdMessage);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public void showErrorMessage(String message) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }
}
