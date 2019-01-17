package com.demo.savemymoney.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUserSignedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void goTo(Class activity) {
        startActivity(new Intent(this, activity));
    }
}
