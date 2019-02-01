package com.demo.savemymoney.monto;

import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.repository.IncomeRepository;
import com.demo.savemymoney.data.repository.MainAmountRepository;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MontoFragmentPresenter {
    private View view;
    private Context context;
    private IncomeRepository incomeRepository;
    private MainAmountRepository mainAmountRepository;
    private FirebaseAuth firebaseAuth;

    public MontoFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        incomeRepository = new IncomeRepository(context);
        mainAmountRepository = new MainAmountRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void saveIncome() {
        Income income = view.getIncome();
        List<ErrorMessage> errors = getErrors(income);
        if (errors.isEmpty()) {
            view.showProgress();
            incomeRepository.save(income)
                    .addCallback(new FutureCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            view.hideProgress();
                            view.notifyIncomeSaved();
                            view.prepareFutureNotification(income);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(getClass().getName(), "Error saving income", t);
                            view.hideProgress();
                        }
                    });
        } else
            view.showErrorMessages(errors);
    }

    private List<ErrorMessage> getErrors(Income model) {
        view.clearErrorMessages();
        List<ErrorMessage> errors = new ArrayList<>();
        if (model.amount <= 0.00)
            errors.add(new ErrorMessage(R.id.monto_textInputLayout, context.getString(R.string.income_invalid)));

        return errors;
    }

    public void loadIncome() {
        view.showProgress();
        incomeRepository.getIncome(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<Income>() {
                    @Override
                    public void onSuccess(Income result) {
                        view.hideProgress();
                        if (result != null) {
                            view.loadValues(result);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        view.hideProgress();
                    }
                });
    }

    public void increaseMainAmount() {
        mainAmountRepository.increaseByIncome(firebaseAuth.getCurrentUser().getUid())
                .addSuccessCallback(result -> Log.i(getClass().getName(), "increase amount same day"));
    }

    public interface View {
        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showProgress();

        void hideProgress();

        Income getIncome();

        void notifyIncomeSaved();

        void loadValues(Income result);

        void prepareFutureNotification(Income income);
    }
}
