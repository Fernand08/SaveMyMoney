package com.demo.savemymoney.main;

import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.repository.CategoryRepository;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainFragmentPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private CategoryRepository repository;

    public MainFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        repository = new CategoryRepository(context);
    }

    public void getCategoryList() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        repository.countCategories(uid)
                .addCallback(new FutureCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        if (result <= 0) {
                            repository.saveDefaultsAndGetCategories(uid)
                                    .addSuccessCallback(result1 -> view.showCategoryList(result1));
                        } else {
                            repository.getAll(uid)
                                    .addSuccessCallback(result12 -> view.showCategoryList(result12));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error counting categories", t);
                    }
                });
    }


    public interface View {

        void showProgress();

        void hideProgress();

        void showCategoryList(List<Category> result);
    }
}
