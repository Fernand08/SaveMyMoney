package com.demo.savemymoney.signup;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.demo.savemymoney.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.util.Patterns.EMAIL_ADDRESS;

public class SignUpFragmentPresenter {
    private View view;
    private Activity activity;
    private FirebaseAuth firebaseAuth;

    public SignUpFragmentPresenter(View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUp(SignUpViewModel model) {
        List<String> errorMessages = getErrors(model);
        if (errorMessages.isEmpty()) {
            //TODO save first and last name
            firebaseAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            TabLayout tabLayout = activity.findViewById(R.id.login_tab_layout);
                            tabLayout.getTabAt(0).select();
                            view.showMessage(activity.getString(R.string.sign_up_success));
                            view.reset();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                view.showErrorMessages(Collections.singletonList(activity.getString(R.string.sign_up_user_collision)));
                            else {
                                Log.e(getClass().getName(), "Error creating user", task.getException());
                                view.showErrorMessages(Collections.singletonList(activity.getString(R.string.sign_up_failed)));
                            }
                        }
                    });
        } else
            view.showErrorMessages(errorMessages);
    }

    private List<String> getErrors(SignUpViewModel model) {
        List<String> errors = new ArrayList<>();
        if (model == null)
            errors.add(activity.getString(R.string.login_error_model_null));
        else {
            if (model.getEmail() == null || model.getEmail().isEmpty())
                errors.add(activity.getString(R.string.login_error_email_empty));
            else if (!EMAIL_ADDRESS.matcher(model.getEmail()).matches())
                errors.add(activity.getString(R.string.login_error_email_invalid));

            if (model.getPassword() == null || model.getPassword().isEmpty())
                errors.add(activity.getString(R.string.login_password_empty));
            else if (model.getPassword().length() < 6)
                errors.add(activity.getString(R.string.sign_up_password_invalid));
        }

        return errors;
    }

    public interface View {
        void showErrorMessages(List<String> messages);

        void showMessage(String message);

        void reset();
    }
}
