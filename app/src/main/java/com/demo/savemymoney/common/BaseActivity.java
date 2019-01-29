package com.demo.savemymoney.common;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BaseActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private SweetAlertDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.setCancelable(false);
    }

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void showProgressDialog(int resIdMessage) {
        progressDialog.setTitle(resIdMessage);
        progressDialog.show();
    }

    public void showErrorMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    public void goTo(Class activity) {
        startActivity(new Intent(this, activity));
    }
}
