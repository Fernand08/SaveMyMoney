package com.demo.savemymoney.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.login.LoginActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isUserSignedIn())
            goTo(LoginActivity.class);
        else
            Toast.makeText(this, "Hello " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

    }

    public void signOut(View view) {
        mAuth.signOut();
        goTo(LoginActivity.class);
    }
}
