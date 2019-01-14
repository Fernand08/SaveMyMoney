package com.demo.savemymoney.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.databinding.SignUpFragmentBinding;

import java.util.List;

public class SignUpFragment extends Fragment implements SignUpFragmentPresenter.View {

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

        return binding.getRoot();
    }

    @Override
    public void showErrorMessages(List<String> messages) {
        for (String message : messages)
            Toast.makeText(getActivity(),
                    message,
                    Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reset() {
        binding.setUser(new SignUpViewModel());
    }
}
