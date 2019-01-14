package com.demo.savemymoney.login;

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
import com.demo.savemymoney.databinding.LoginFragmentBinding;

import java.util.List;

public class LoginFragment extends Fragment implements LoginFragmentPresenter.View {

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

        return binding.getRoot();
    }

    @Override
    public void showErrorMessages(List<String> messages) {
        for (String message : messages)
            Toast.makeText(getActivity(),
                    message,
                    Toast.LENGTH_SHORT).show();


    }
}
