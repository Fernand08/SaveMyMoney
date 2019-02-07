package com.demo.savemymoney.goal;


import android.databinding.DataBindingUtil;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.databinding.FragmentGoalProgressBinding;
import com.emredavarci.circleprogressbar.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GoalProgressFragment extends BaseFragment implements GoalProgressFragmentPresenter.View {


    @BindView(R.id.progressBar)
    CircleProgressBar progressBar;

    GoalProgressFragmentPresenter presenter;

    public static  GoalProgressFragment newInstance(){return new GoalProgressFragment();}
    private Handler handler = new Handler();
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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    loadingProgress();

    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.goal_alert_loading);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }



public  void loadingProgress(){

       Void goal = presenter.loadGoal();

    new Thread(new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (status < 100) {
                status += 1;

                handler. post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        progressBar.setProgress(status);
                        progressBar.setText(String.valueOf(status));

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
    }).start();

}


    @Override
    public void loadGoal(Goal goal) {



    }
}
