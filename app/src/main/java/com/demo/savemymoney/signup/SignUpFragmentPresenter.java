package com.demo.savemymoney.signup;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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
        List<ErrorMessage> errorMessages = getErrors(model);
        if (errorMessages.isEmpty()) {
            view.showProgress();
            firebaseAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            saveToCloudDb(task.getResult().getUser().getUid(), model);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(model.getFirstName())
                                    .build();

                            task.getResult().getUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            view.hideProgress();
                                            activity.startActivity(new Intent(activity, MainActivity.class));
                                        } else {
                                            view.hideProgress();
                                            TabLayout tabLayout = activity.findViewById(R.id.login_tab_layout);
                                            tabLayout.getTabAt(0).select();
                                            view.showMessageSuccess(activity.getString(R.string.sign_up_success));
                                            view.reset();
                                        }
                                    });
                        } else {
                            view.hideProgress();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                view.showErrorMessages(Collections.singletonList(new ErrorMessage(null, activity.getString(R.string.sign_up_user_collision))));
                            else {
                                Log.e(getClass().getName(), "Error creating user", task.getException());
                                view.showErrorMessages(Collections.singletonList(new ErrorMessage(null, activity.getString(R.string.sign_up_failed))));
                            }
                        }
                    });
        } else
            view.showErrorMessages(errorMessages);
    }

    private void saveToCloudDb(String uid, SignUpViewModel model) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(uid)
                .set(model.toMap())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Log.i("SignUp", "user saved");
                    else
                        Log.e("SignUp", "Error saving user");
                });
    }

    private List<ErrorMessage> getErrors(SignUpViewModel model) {
        List<ErrorMessage> errors = new ArrayList<>();
        view.clearErrorMessages();
        if (model == null)
            errors.add(new ErrorMessage(null, activity.getString(R.string.sign_up_error_model_null)));
        else {
            if (model.getFirstName() == null || model.getFirstName().isEmpty())
                errors.add(new ErrorMessage(R.id.firstNameInputLayout, activity.getString(R.string.login_error_first_name_empty)));

            if (model.getLastName() == null || model.getLastName().isEmpty())
                errors.add(new ErrorMessage(R.id.lastNameInputLayout, activity.getString(R.string.login_error_last_name_empty)));

            if (model.getEmail() == null || model.getEmail().isEmpty())
                errors.add(new ErrorMessage(R.id.emailSignUpInputLayout, activity.getString(R.string.login_error_email_empty)));
            else if (!EMAIL_ADDRESS.matcher(model.getEmail()).matches())
                errors.add(new ErrorMessage(R.id.emailSignUpInputLayout, activity.getString(R.string.login_error_email_invalid)));

            if (model.getPassword() == null || model.getPassword().isEmpty())
                errors.add(new ErrorMessage(R.id.passwordSignUpInputLayout, activity.getString(R.string.login_password_empty)));
            else if (model.getPassword().length() < 6)
                errors.add(new ErrorMessage(R.id.passwordSignUpInputLayout, activity.getString(R.string.sign_up_password_invalid)));
            else if (model.getPasswordConfirm() == null || model.getPasswordConfirm().isEmpty())
                errors.add(new ErrorMessage(R.id.passwordConfirmInputLayout, activity.getString(R.string.login_password_empty)));
            else if (model.getPasswordConfirm().length() < 6)
                errors.add(new ErrorMessage(R.id.passwordConfirmInputLayout, activity.getString(R.string.sign_up_password_invalid)));
            else if (!model.getPassword().equals(model.getPasswordConfirm()))
                errors.add(new ErrorMessage(R.id.passwordConfirmInputLayout, activity.getString(R.string.sign_up_password_not_match)));

            if (errors.isEmpty() && !model.getAcceptTerms())
                errors.add(new ErrorMessage(null, activity.getString(R.string.sign_up_not_accepted_terms)));
        }

        return errors;
    }

    public interface View {
        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showMessageSuccess(String message);

        void reset();

        void showProgress();

        void hideProgress();
    }
}
