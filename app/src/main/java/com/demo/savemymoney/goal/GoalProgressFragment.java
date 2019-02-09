package com.demo.savemymoney.goal;


import android.content.Intent;
import android.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.databinding.FragmentGoalProgressBinding;
import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class GoalProgressFragment extends BaseFragment implements GoalProgressFragmentPresenter.View {


    @BindView(R.id.progressBar)
    CircleProgressBar progressBar;
    @BindView(R.id.goal_progress_mensaje)
    TextView message;
    @BindView(R.id.goal_progress_update)
    Button gastar ;

    FirebaseAuth firebaseAuth;
    GoalProgressFragmentPresenter presenter;


    public static  GoalProgressFragment newInstance(){return new GoalProgressFragment();}
    private Handler handler = new Handler();
    private Thread thread;

    double montoAhorrado= 0;
    int  status = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentGoalProgressBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_goal_progress, container, false);
        presenter = new GoalProgressFragmentPresenter(this,getContext());
        binding.setPresenter(presenter);
        View view = binding.getRoot();
        ButterKnife.bind(this,view);

        getActivity().setTitle(R.string.goal_progress_title);

        gastar.setVisibility(View.INVISIBLE);

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        presenter.loadCategorySaving();

        presenter.loadGoal();


    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.goal_alert_loading);
    }





    @Override
    public void hideProgress() {
        hideProgressDialog();
    }



    @Override
    public void showAmountSaving(Category result) {
        montoAhorrado = result.distributedAmount;
    }

    @Override
    public void goToo(Class activity) {
        startActivity(new Intent(getContext(), activity));
    }

    @Override
    public void setIncreaseProgress(Integer integer) {
        progressBar.setProgress(integer);
        progressBar.setText(String.valueOf(integer));
        progressBar.setProgressColor("#00a4ff"); 	// set progress color

    }

    @Override
    public void setIncreaseProgressFull(Integer integer) {
        progressBar.setProgress(integer);
        progressBar.setText(String.valueOf(integer));
        progressBar.setProgressColor("#00a4ff"); 	// set progress color
        progressBar.setBackgroundColor("#00fbef");
        progressBar.setTextColor("#00a4ff");

    }

    @Override
    public void onDecreaseAmount(Double amount) {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.goal_goals_decrease_title));
        alert
                .setConfirmText("OK")
                .setConfirmClickListener(sDialog ->{
                    sDialog.dismissWithAnimation();
                    presenter.decreaseAmountSaving(firebaseAuth.getCurrentUser().getUid(),Boolean.TRUE,amount);
                });
        alert.setCancelable(false);
        alert.show();



    }


    @Override
    public void loadGoal(Goal goal) {



        Double montogoal = goal.amountGoal;
        Double montosaving  = montoAhorrado;
        Double diferencia = montosaving - montogoal;
        Double diferenciaMonto = -1 * diferencia;
        Integer   porcentaje = (int) ((montosaving/ montogoal)*100);

       if( diferencia<0){

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
                           Thread.sleep(16); //thread will take approx 3 seconds to finish
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           };
          thread.start();

           SpannableStringBuilder builder = new SpannableStringBuilder();


           SpannableString str1= new SpannableString("Necesitas  S/.");
           str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
           builder.append(str1);

           SpannableString str2= new SpannableString( String.valueOf( diferenciaMonto));
           str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
           builder.append(str2);

           SpannableString str3= new SpannableString(" para obtener ");
           str3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str3.length(), 0);
           builder.append(str3);
           SpannableString str4= new SpannableString(String.valueOf(goal.description));
           str4.setSpan(new ForegroundColorSpan(Color.RED), 0, str4.length(), 0);
           builder.append(str4);


          message.setText(builder, TextView.BufferType.SPANNABLE);

       } else {
           gastar.setVisibility(View.VISIBLE);

          thread = new Thread(){

               @Override
               public void run() {
                   // TODO Auto-generated method stub
                   while (status < 100) {
                       status += 1;

                       handler.post(new Runnable() {

                           @Override
                           public void run() {
                               // TODO Auto-generated method stub
                               setIncreaseProgressFull(status);
                           }
                       });
                       try {
                           Thread.sleep(16);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           };
          thread.start();

           SpannableStringBuilder builder = new SpannableStringBuilder();
           SpannableString str1= new SpannableString("Felicidades! Lograste alcanzar tu meta,    Ya puedes obtener: " );
           str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
           builder.append(str1);

           SpannableString str2= new SpannableString( goal.description);
           str2.setSpan(new ForegroundColorSpan(Color.rgb(0, 164, 255)), 0, str2.length(), 0);
           builder.append(str2);


           message.setText(builder, TextView.BufferType.SPANNABLE);








       }




    }




    @Override
    public void Reload(Fragment fragment) {
        goTo(fragment,R.id.content_frame);
    }


}
