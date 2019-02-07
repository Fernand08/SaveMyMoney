package com.demo.savemymoney.goal;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.main.MainActivity;
import com.demo.savemymoney.monto.MontoFragment;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GoalFragmentPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoalRepository goalRepository;
    public GoalFragmentPresenter(View view, Context context){
        this.view = view;
        this.context = context;

        goalRepository = new GoalRepository(context);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void getAmountSaving(){
        goalRepository.findAMountByUserUID(firebaseAuth.getCurrentUser().getUid(),Boolean.TRUE)
                .addCallback(new FutureCallback<Category>() {
                    @Override
                    public void onSuccess(Category result) {
                        view.showAmountSaving(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(),"Error load Amount Saving", t);
                    }
                });

    }
    public void loadGoal(){
        view.showProgress();
        goalRepository.getGoal(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<Goal>() {
                    @Override
                    public void onSuccess(Goal result) {
                        view.hideProgress();
                        if(result!= null){
                            view.loadGoal(result);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error loading goal", t);
                    }
                });

    }
    public void saveGoal(){

        Goal goal = view.getGoal();
        List<ErrorMessage> errors = getErrors(goal);
        if(errors.isEmpty()){
            SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(context.getString(R.string.goal_alert_title));
            alert.setConfirmText("Si")
                    .setCancelText("No")
                    .showCancelButton(true)
                    .setConfirmClickListener(sDialog ->{

                        view.showProgress();
                        goalRepository.save(goal).addCallback(new FutureCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                view.hideProgress();
                                view.notifyGoalSaved();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e(getClass().getName(), "Error saving goal", t);
                                view.hideProgress();
                            }
                        });

                        sDialog.dismissWithAnimation();
                    });
            alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    loadGoal();
                    sweetAlertDialog.cancel();
                }

            });

            alert.show();

        }else
            view.showErrorMessages(errors);



    }

    private List<ErrorMessage>getErrors (Goal goal){
        view.clearErrorMessages();
        List<ErrorMessage> errors = new ArrayList<>();
        if(goal.amountGoal <= 0.00 || goal.amountGoal == null){
            errors.add(new ErrorMessage(R.id.amount_goal_til,context.getString(R.string.goal_invalid_amount)));
        }
       if(goal.description.isEmpty() || goal.description == null ){
            errors.add(new ErrorMessage(R.id.description_goal_Til,context.getString(R.string.goal_invalid_description)));
        }

        return  errors;
    }


    public void deleteGoal(){
        Goal goal = view.getGoal();
        SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        alert.setTitleText(context.getString(R.string.goal_alert_delete_title));
        alert.setConfirmText("Si")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog ->{

                    view.showProgress();
                    goalRepository.deleteGoal(goal).addSuccessCallback(res -> {


                        view.showSuccess(context.getString(R.string.goal_alert_delete));
                        view.hideProgress();
                        view.Reload(GoalFragment.newInstance());
                    });


                    loadGoal();
                    sDialog.dismissWithAnimation();



                });
        alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
               loadGoal();
                sweetAlertDialog.cancel();
            }

        });

        alert.show();








    }

    public  interface  View{
        void showProgress();
        void showErrorMessages(List<ErrorMessage> errors);
        void clearErrorMessages();
        void showSuccess(String string);
        void hideProgress();
        void notifyGoalSaved();
        void showAmountSaving(Category result);
        Goal getGoal();
        void Reload (Fragment fragment);
        void loadGoal(Goal goal);

    }



}
