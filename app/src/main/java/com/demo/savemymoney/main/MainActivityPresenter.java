package com.demo.savemymoney.main;

import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.repository.IncomeRepository;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private IncomeRepository repository;

    public MainActivityPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        repository = new IncomeRepository(context);
    }

    public void checkIfHasIncome() {
        view.showProgress(R.string.main_income_loading);
        repository.hasIncome(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        view.hideProgress();
                        if (!result)
                            view.notifyToRegisterIncome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error get income", t);
                        view.showError(context.getString(R.string.main_income_error));
                        view.hideProgress();
                    }
                });
    }

    public void addDefaultCategoriesIfNotExists(){

    }

    public interface View {
        void showProgress(int resId);

        void hideProgress();

        void showError(String message);

        void notifyToRegisterIncome();
    }
}
