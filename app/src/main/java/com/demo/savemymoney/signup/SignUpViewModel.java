package com.demo.savemymoney.signup;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.demo.savemymoney.BR;

import java.util.HashMap;
import java.util.Map;

public class SignUpViewModel extends BaseObservable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;
    private boolean acceptTerms;

    @Bindable
    public boolean getAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
        notifyPropertyChanged(BR.acceptTerms);
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
        notifyPropertyChanged(BR.passwordConfirm);
    }

    Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        return data;
    }
}
