package com.demo.savemymoney.goal;


import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.emredavarci.circleprogressbar.CircleProgressBar;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.logging.Handler;

public class GoalProgressFragmentPresenter {

    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private GoalRepository goalRepository;

    private CircleProgressBar progressBar;
    int status= 0;
    private Handler handler ;

    public  GoalProgressFragmentPresenter(View view, Context context){
        this.view = view;
        this.context = context;
        goalRepository = new GoalRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loadingProgress(){

    }



    public Void loadGoal(){
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

        return null;
    }




    public interface View {
        void showProgress();
        void hideProgress();

        void loadGoal(Goal goal);
    }
}
