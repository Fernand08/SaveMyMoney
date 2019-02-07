package com.demo.savemymoney.goal;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.databinding.FragmentGoalBinding;
import com.demo.savemymoney.main.MainActivity;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.kolotnev.formattedittext.CurrencyEditText;


public class GoalFragment extends BaseFragment implements GoalFragmentPresenter.View {


    @BindView(R.id.cash_saving)
    TextView montoahorrado;
    @BindView(R.id.amount_goal_til)
    TextInputLayout montodeseadoTil;



    @BindView(R.id.amount_goal)
    CurrencyEditText montodeseado;

    @BindView(R.id.description_goal_Til)
    TextInputLayout descripcionTil;

    @BindView(R.id.description_goal)
    EditText descripcion;

    @BindView(R.id.delete_goal_button)
    ImageButton delete;
    @BindView(R.id.register_goal_button)
    Button registrar ;


    GoalFragmentPresenter presenter;


public static GoalFragment newInstance(){return new GoalFragment();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentGoalBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_goal,container,false);
        presenter = new GoalFragmentPresenter(this,getContext());
        binding.setPresenter(presenter);

        View view = binding.getRoot();

        ButterKnife.bind(this,view);

        getActivity().setTitle(R.string.goal_title);

        delete.setVisibility(View.INVISIBLE);
        registrar.setText(getString(R.string.string_goals_register));
        // Inflate the layout for this fragment
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getAmountSaving();
        presenter.loadGoal();

    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.goal_alert_loading);
    }





    @Override
    public Goal getGoal() {
        String usuarioId = mAuth.getCurrentUser().getUid();
        BigDecimal montoGoal = montodeseado.getValue();
        String descripGoal = descripcion.getText().toString();
        Goal goal = new Goal();
        goal.userUID = usuarioId;
        goal.goalId = 1;
        goal.amountGoal = montoGoal.doubleValue();
        goal.description = descripGoal;


        return goal;
    }

    @Override
    public void Reload(Fragment fragment) {
        goTo(fragment,R.id.content_frame);
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
        montodeseadoTil.setErrorEnabled(false);
        montodeseadoTil.setError("");
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void notifyGoalSaved() {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText(getString(R.string.goal_alert_confirm_title));
        alert.setContentText(getString(R.string.goal_alert_confirm_context))
                .setConfirmText("OK")
        .setConfirmClickListener(sDialog ->{
            sDialog.dismissWithAnimation();
           presenter.loadGoal();
        });
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void showAmountSaving(Category result) {
       Double amauntSaving = result.distributedAmount;
       String amauntSaving_string = String.valueOf(amauntSaving);
       montoahorrado.setText("S/. " +amauntSaving_string);



    }





    @Override
    public void loadGoal(Goal goal) {
    if(goal.amountGoal != null && goal.description != null){
        delete.setVisibility(View.VISIBLE);
        montodeseado.setValue(BigDecimal.valueOf(goal.amountGoal));
        descripcion.setText(goal.description);
        registrar.setText(getString(R.string.string_goals_update));

    }



    }

}
