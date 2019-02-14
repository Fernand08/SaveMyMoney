package com.demo.savemymoney.goal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.CategoryRepository;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.main.MainActivity;
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
    private  SharedPreferences preferences;
    public GoalFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;

        goalRepository = new GoalRepository(context);

        preferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void getAmountSaving() {
        goalRepository.findAMountByUserUID(firebaseAuth.getCurrentUser().getUid(), Boolean.TRUE)
                .addCallback(new FutureCallback<Category>() {
                    @Override
                    public void onSuccess(Category result) {
                        view.showAmountSaving(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error load Amount Saving", t);
                    }
                });

    }

    public void loadGoal() {
        view.showProgress();
        goalRepository.getGoal(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<Goal>() {
                    @Override
                    public void onSuccess(Goal result) {
                        view.hideProgress();
                        if (result != null) {
                            view.loadGoal(result);

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error loading goal", t);
                    }
                });

    }







    public void saveGoal() {

        String montoAhorrado =  preferences.getString("montoSaving","");
        Double montoAhorrado_Double = Double.parseDouble(montoAhorrado);
        Goal goal = view.getGoal();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("memorySaving",String.valueOf(montoAhorrado_Double));
        editor.putString("memoryGoaldes",goal.description);
        editor.putString("memoryGoal",String.valueOf(goal.amountGoal));
        editor.commit();

        List<ErrorMessage> errors = getErrors(goal);




        if (errors.isEmpty()) {

            if(goal.amountGoal > montoAhorrado_Double){
                SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context.getString(R.string.goal_alert_confirm_title));
                alert.setContentText(context.getString(R.string.goal_alert_confirm_context))
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {
                            goalRepository.save(goal).addCallback(new FutureCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    view.notifyGoalSaved();
                                    view.hideProgress();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Log.e(getClass().getName(), "Error saving goal", t);
                                    view.hideProgress();
                                }
                            });

                            sDialog.dismissWithAnimation();

                        });
                alert.setCancelable(false);
                alert.show();
            }else{
                SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(context.getString(R.string.goal_alert_goal_achieved_title));
                alert.setContentText(context.getString(R.string.goal_alert_goal_achieved_des))
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {

                        loadGoal();

                            alert.cancel();
                        });
                alert.setCancelable(false);
                alert.show();



            }




        } else
            view.showErrorMessages(errors);


    }

    private List<ErrorMessage> getErrors(Goal goal) {
        view.clearErrorMessages();
        List<ErrorMessage> errors = new ArrayList<>();
        if (goal.amountGoal <= 0.00 || goal.amountGoal == null) {
            errors.add(new ErrorMessage(R.id.amount_goal_til, context.getString(R.string.goal_invalid_amount)));
        }
        if (goal.description.isEmpty() || goal.description == null) {
            errors.add(new ErrorMessage(R.id.description_goal_Til, context.getString(R.string.goal_invalid_description)));
        }

        return errors;
    }


    public void deleteGoal() {
        Goal goal = view.getGoal();
        SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);

        alert.setTitleText(context.getString(R.string.goal_alert_delete_title));
        alert.setConfirmText("Si")
                .setCancelText("No")
                .showCancelButton(true)
                .setConfirmClickListener(sDialog -> {

                    view.showProgress();
                    goalRepository.deleteGoal(goal).addSuccessCallback(res -> {


                        view.showSuccess(context.getString(R.string.goal_alert_delete));
                        view.hideProgress();
                        view.Reload(GoalFragment.newInstance());
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


    }

    public void deleteVerifyGoal(){



        goalRepository.deleteGoalSaving(firebaseAuth.getCurrentUser().getUid()).addSuccessCallback(res -> {

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("memoryGoal");
            editor.remove("memoryGoaldes");
            editor.remove("inspectGoal");
            editor.remove("memorySaving");
            editor.remove("montoSaving");
            editor.commit();
            view.Reload(GoalFragment.newInstance());

            view.hideProgress();

        });








    }


    public void verifyGoal(){
        String montoSaving_String = preferences.getString("inspectSaving","");
        String montoGoal_String = preferences.getString("inspectGoal","");
        String montoGoalDes = preferences.getString("memoryGoaldes","");

        Double montoSaving_Double = Double.valueOf(montoSaving_String) ;
        Double montoGoal_Double = Double.valueOf(montoGoal_String);
        if(montoSaving_Double != null && montoGoal_Double != null){

            if(montoGoal_Double >= montoSaving_Double ){



                SpannableStringBuilder builder = new SpannableStringBuilder();


                SpannableString str1= new SpannableString(context.getString(R.string.goal_alert_goal_congratulations_des  ));
                str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), 0);
                builder.append(str1);

                SpannableString str2= new SpannableString(  montoGoal_String );
                str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
                builder.append(str2);

                SpannableString str3= new SpannableString(" para tu ");
                str3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str3.length(), 0);
                builder.append(str3);
                SpannableString str4= new SpannableString(String.valueOf( montoGoalDes ));
                str4.setSpan(new ForegroundColorSpan(Color.RED), 0, str4.length(), 0);
                builder.append(str4);




                SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(context.getString(R.string.goal_alert_goal_congratulations_title));
                alert.setContentText(builder.toString())
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {
                            goalRepository.deleteGoalSaving(firebaseAuth.getCurrentUser().getUid()).addSuccessCallback(res -> {

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove("memoryGoal");
                                editor.remove("memoryGoaldes");
                                editor.remove("inspectGoal");
                                editor.remove("memorySaving");
                                editor.commit();
                                view.hideProgress();

                            });
                            sDialog.dismissWithAnimation();
                        });
                alert.setCancelable(false);
                alert.show();
            }

        }





    }

    public interface View {
        void showProgress();

        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showSuccess(String string);

        void hideProgress();

        void notifyGoalSaved();

        void showAmountSaving(Category result);

        Goal getGoal();

        void Reload(Fragment fragment);

        void loadGoal(Goal goal);
       void setIncreaseProgress(Integer integer);
    }


}
