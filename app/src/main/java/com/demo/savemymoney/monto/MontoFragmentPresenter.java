package com.demo.savemymoney.monto;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.data.repository.IncomeRepository;
import com.demo.savemymoney.main.MainActivity;
import com.github.clemp6r.futuroid.FutureCallback;

import java.util.ArrayList;
import java.util.List;

public class MontoFragmentPresenter {
    private View view;
    private Context context;
    private IncomeRepository repository;

    public MontoFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        repository = new IncomeRepository(context);
    }

    public void saveIncome() {
        Income income = view.getIncome();
        List<ErrorMessage> errors = getErrors(income);
        if (errors.isEmpty()) {
            view.showProgress();
            repository.save(income)
                    .addCallback(new FutureCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            view.hideProgress();
                            Log.i(getClass().getName(), "Income saved");
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            view.finish();
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

    public interface View {
        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showProgress();

        void hideProgress();

        Income getIncome();

        void finish();
    }
}
