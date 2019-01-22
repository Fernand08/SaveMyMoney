package com.demo.savemymoney.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.databinding.LoginFragmentBinding;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.demo.savemymoney.common.ButterKnifeActions.CLEAR_ERROR;

public class LoginFragment extends Fragment implements LoginFragmentPresenter.View {

    @BindViews({R.id.emailInputLayout, R.id.passwordInputLayout})
    List<TextInputLayout> inputLayouts;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        LoginFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false);
        LoginFragmentPresenter presenter = new LoginFragmentPresenter(this, getContext());
        LoginViewModel user = new LoginViewModel();

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
                Toast.makeText(getActivity(),
                        error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            else {
                TextInputLayout input = getActivity().findViewById(error.getInputId());
                input.setErrorEnabled(true);
                input.setError(error.getMessage());
            }
        }

    }

    @Override
    public void clearErrorMessages() {
        ButterKnife.apply(inputLayouts,CLEAR_ERROR);
    }
}
