package com.demo.savemymoney.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.common.mail.MailSender;
import com.demo.savemymoney.main.MainActivity;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.util.Patterns.EMAIL_ADDRESS;

public class LoginFragmentPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;

    public LoginFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(LoginViewModel model) {
        List<ErrorMessage> errorMessages = getErrors(model);
        if (errorMessages.isEmpty()) {
            view.showProgress();
            view.clearErrorMessages();
            firebaseAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(task -> {
                        view.hideProgress();
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            Log.e(getClass().getName(), "Authentication error", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                                view.showErrorMessages(Collections.singletonList(new ErrorMessage(null, context.getString(R.string.login_failed))));
                            else if (task.getException() instanceof FirebaseAuthInvalidUserException)
                                view.showErrorMessages(Collections.singletonList(new ErrorMessage(null, context.getString(R.string.login_failed_user_not_exist))));
                            else if (task.getException() instanceof FirebaseNetworkException)
                                view.showErrorMessages(Collections.singletonList(new ErrorMessage(null, context.getString(R.string.login_failed_network_error))));

                        }
                    });
        } else {
            view.clearErrorMessages();
            view.showErrorMessages(errorMessages);
        }
    }

    private List<ErrorMessage> getErrors(LoginViewModel model) {
        List<ErrorMessage> errors = new ArrayList<>();
        if (model == null)
            errors.add(new ErrorMessage(null, context.getString(R.string.login_error_model_null)));
        else {
            if (model.getEmail() == null || model.getEmail().isEmpty())
                errors.add(new ErrorMessage(R.id.emailInputLayout, context.getString(R.string.login_error_email_empty)));
            else if (!EMAIL_ADDRESS.matcher(model.getEmail()).matches())
                errors.add(new ErrorMessage(R.id.emailInputLayout, context.getString(R.string.login_error_email_invalid)));

            if (model.getPassword() == null || model.getPassword().isEmpty())
                errors.add(new ErrorMessage(R.id.passwordInputLayout, context.getString(R.string.login_password_empty)));
        }

        return errors;
    }

    public interface View {
        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showProgress();

        void hideProgress();
    }
}
