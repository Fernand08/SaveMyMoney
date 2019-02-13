package com.demo.savemymoney.goal;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.databinding.FragmentGoalBinding;
import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.emredavarci.circleprogressbar.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
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
    TextView delete;
    @BindView(R.id.register_goal_button)
    Button registrar;

    @BindView(R.id.group2)
    LinearLayout group;

    @BindView(R.id.progress)
    LinearLayout progress;

    @BindView(R.id.progressBar)
    CircleProgressBar progressBar;
    @BindView(R.id.goal_progress_mensaje)
    TextView message;

    GoalFragmentPresenter presenter;

    private Handler handler = new Handler();
    private Thread thread;

    double montoAhorrado;
    int  status = 0;
    private SharedPreferences preferences;
    private GoalRepository goalRepository;
     FirebaseAuth firebaseAuth;
    DisplayMetrics displaymetrics;


    public static GoalFragment newInstance() {
        return new GoalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentGoalBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goal, container, false);
        presenter = new GoalFragmentPresenter(this, getContext());

        binding.setPresenter(presenter);

        View view = binding.getRoot();

        ButterKnife.bind(this, view);

       displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        if(480 <width && width<=768 && 800< height &&  height <= 1280){
            progressBar.getLayoutParams().width = 500;
            progressBar.getLayoutParams().height=300;

        } else if (width<= 480 && height <= 800){
            progressBar.getLayoutParams().width =220;
            progressBar.getLayoutParams().height=180;
        }



        getActivity().setTitle(R.string.goal_title);

        delete.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        registrar.setText(getString(R.string.string_goals_register));


       preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        // Inflate the layout for this fragment
        return view;
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
        goTo(fragment, R.id.content_frame);
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

             Reload(GoalFragment.newInstance());


    }

    @Override
    public void showAmountSaving(Category result) {

        Double amauntSaving = result.distributedAmount;
            amauntSaving = Math.round(amauntSaving*100.0)/100.0;

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("montoSaving");
        editor.putString("montoSaving",String.valueOf(amauntSaving));
        editor.commit();

        montoAhorrado = result.distributedAmount;
        String amauntSaving_string = String.valueOf(amauntSaving);
        montoahorrado.setText("S/. " +amauntSaving_string);


    }




    @Override
    public void loadGoal(Goal goal) {
        String  montosave = preferences.getString("montoSaving","");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("inspectGoal",String.valueOf(goal.amountGoal));
        editor.putString("inspectSaving",montosave);
        editor.commit();
        Double guardado = Double.parseDouble(montosave);




        if (goal.amountGoal != null && goal.description != null) {
            progress.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);

            montodeseado.setValue(BigDecimal.valueOf(goal.amountGoal));
            descripcion.setText(goal.description);


            BigDecimal montogoal = BigDecimal.valueOf( goal.amountGoal);

            BigDecimal montosaving =BigDecimal.valueOf(guardado )  ;
            BigDecimal diferencia = montosaving.subtract(montogoal);
            Double diferencia_double = Double.parseDouble(String.valueOf(diferencia));
                   diferencia_double = Math.round(diferencia_double*100.0)/100.0;
            BigDecimal diferenciaMonto = montogoal.subtract(montosaving);
            Double diferenciaMonto_Double = Double.parseDouble(String.valueOf( diferenciaMonto));
            diferenciaMonto_Double = Math.round(diferenciaMonto_Double*100.0)/100.0;
            BigDecimal division = montosaving.divide(montogoal, MathContext.DECIMAL128);
            Double result  = Double.parseDouble(String.valueOf(division));


            Integer   porcentaje = (int)  (result * 100) ;




            if(diferencia_double<0){
                if(porcentaje <= 0){
                    setIncreaseProgress(0);
                }else{
                    thread = new Thread() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            while (status < porcentaje) {
                                status += 1;

                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        setIncreaseProgress(status);

                                    }
                                });
                                try {
                                    // Sleep for 200 milliseconds.
                                    // Just to display the progress slowly
                                    Thread.sleep(32); //thread will take approx 3 seconds to finish
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    thread.start();


                }

                SpannableStringBuilder builder = new SpannableStringBuilder();


                SpannableString str1= new SpannableString("Necesitas  S/.");
                str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
                builder.append(str1);

                SpannableString str2= new SpannableString( String.valueOf( diferenciaMonto_Double));
                str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
                builder.append(str2);

                SpannableString str3= new SpannableString(" para obtener tu ");
                str3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str3.length(), 0);
                builder.append(str3);
                SpannableString str4= new SpannableString(String.valueOf(goal.description));
                str4.setSpan(new ForegroundColorSpan(Color.RED), 0, str4.length(), 0);
                builder.append(str4);


                message.setText(builder, TextView.BufferType.SPANNABLE);



            } else{
                setIncreaseProgress(100);
                String monto ="S/. "+ goal.amountGoal;
                SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getString(R.string.goal_alert_goal_congratulations_title));
                alert.setContentText(getContext().getString(R.string.goal_alert_goal_congratulations_des)
                        + monto +" para tu " + goal.description)
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {
                            presenter.deleteVerifyGoal();

                            sDialog.dismissWithAnimation();
                        });
                alert.setCancelable(false);
                alert.show();

            }






        }





    }

    @Override
    public void setIncreaseProgress(Integer integer) {
        progressBar.setProgress(integer);
        progressBar.setText(String.valueOf(integer));
        progressBar.setProgressColor("#00a4ff"); 	// set progress color
    }


}
