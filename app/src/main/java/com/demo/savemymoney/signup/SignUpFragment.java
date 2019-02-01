package com.demo.savemymoney.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.databinding.SignUpFragmentBinding;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.demo.savemymoney.common.ButterKnifeActions.CLEAR_ERROR;

public class SignUpFragment extends BaseFragment implements SignUpFragmentPresenter.View {

    @BindViews({R.id.firstNameInputLayout, R.id.lastNameInputLayout, R.id.emailSignUpInputLayout, R.id.passwordSignUpInputLayout, R.id.passwordConfirmInputLayout})
    List<TextInputLayout> inputLayouts;
    private SignUpFragmentBinding binding;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false);
        SignUpFragmentPresenter presenter = new SignUpFragmentPresenter(this, getActivity());
        SignUpViewModel user = new SignUpViewModel();

        binding.setUser(user);
        binding.setPresenter(presenter);
        View view = binding.getRoot();

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void showErrorMessages(List<ErrorMessage> errors) {
        for (ErrorMessage error : errors) {
            if (error.getInputId() == null)
                showError(error.getMessage());
            else {
                TextInputLayout input = getActivity().findViewById(error.getInputId());
                input.setErrorEnabled(true);
                input.setError(error.getMessage());
            }
        }
    }

    @Override
    public void clearErrorMessages() {
        ButterKnife.apply(inputLayouts, CLEAR_ERROR);
    }

    @Override
    public void showMessageSuccess(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void reset() {
        binding.setUser(new SignUpViewModel());
    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.loading_signup);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }
}
