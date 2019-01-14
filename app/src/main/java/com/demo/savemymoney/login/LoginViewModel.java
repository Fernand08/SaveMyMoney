package com.demo.savemymoney.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.demo.savemymoney.BR;

public class LoginViewModel extends BaseObservable {
    private String email;
    private String password;

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
       // notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        //notifyPropertyChanged(BR.password);
    }
}
