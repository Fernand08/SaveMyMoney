package com.demo.savemymoney.common;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    private ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity());
    }

    public void showProgressDialog(int resIdMessage) {
        progressDialog.setMessage(getString(resIdMessage));
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
