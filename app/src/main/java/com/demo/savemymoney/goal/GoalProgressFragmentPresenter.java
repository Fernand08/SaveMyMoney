package com.demo.savemymoney.goal;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.os.Handler;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.main.MainActivity;
import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GoalProgressFragmentPresenter {

    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoalRepository goalRepository;

    private CircleProgressBar progressBar;
    int status= 0;
   private Handler handler = new Handler();

    public  GoalProgressFragmentPresenter(View view, Context context){
        this.view = view;
        this.context = context;
        goalRepository = new GoalRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
    }






public  void loadGoal(){

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

    public void loadCategorySaving(){
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

public void goDecreaseAmountSaving(){
    SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(context.getString(R.string.goal_goals_decrease_quest));
    alert.setConfirmText("Si")
            .setCancelText("No")
            .showCancelButton(true)
            .setConfirmClickListener(sDialog ->{

                view.showProgress();
                goalRepository.findAMountByUserUID(firebaseAuth.getCurrentUser().getUid(),Boolean.TRUE)
                        .addCallback(new FutureCallback<Category>() {
                            @Override
                            public void onSuccess(Category result) {

                                view.onDecreaseAmount(result.distributedAmount);
                                deleteGoalSaving(result.userUID);
                                view.goToo(MainActivity.class);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.e(getClass().getName(),"Error  Decrease Amount Saving", t);
                            }
                        });



                sDialog.dismissWithAnimation();
            });
    alert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog sweetAlertDialog) {

            sweetAlertDialog.cancel();
        }

    });

    alert.show();


}


    public  void decreaseAmountSaving(String uid , Boolean b, Double amount){
        goalRepository.decreaseAmountSaving(uid, b, amount);
    }

    public void showGoal(){
        view. Reload(GoalFragment.newInstance());
        view.hideProgress();

    }


    public void deleteGoalSaving(String uid){



        goalRepository.deleteGoalSaving(uid).addSuccessCallback(res -> {
            view.hideProgress();
            view.Reload(GoalFragment.newInstance());
        });


    }


    public interface View {
        void showProgress();
        void hideProgress();
        void setIncreaseProgress(Integer integer );
        void setIncreaseProgressFull(Integer integer);
        void onDecreaseAmount(Double amount);
        void loadGoal(Goal goal);
        void Reload (Fragment fragment);
        void showAmountSaving(Category result);
        void goToo(Class c);
    }
}
