package com.demo.savemymoney.login;

import android.content.Context;
import android.content.Intent;

import com.demo.savemymoney.R;
import com.demo.savemymoney.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

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
        List<String> errorMessages = getErrors(model);
        if (errorMessages.isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } else
                            view.showErrorMessages(Collections.singletonList(context.getString(R.string.login_failed)));
                    });
        } else
            view.showErrorMessages(errorMessages);
    }

    private List<String> getErrors(LoginViewModel model) {
        List<String> errors = new ArrayList<>();
        if (model == null)
            errors.add(context.getString(R.string.login_error_model_null));
        else {
            if (model.getEmail() == null || model.getEmail().isEmpty())
                errors.add(context.getString(R.string.login_error_email_empty));
            else if (!EMAIL_ADDRESS.matcher(model.getEmail()).matches())
                errors.add(context.getString(R.string.login_error_email_invalid));

            if (model.getPassword() == null || model.getPassword().isEmpty())
                errors.add(context.getString(R.string.login_password_empty));
        }

        return errors;
    }

    public interface View {
        void showErrorMessages(List<String> messages);
    }
}
