package com.demo.savemymoney.main;

import android.content.Context;
import android.support.v4.math.MathUtils;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.exceptions.CategoryHasDetailException;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.data.repository.CategoryDetailRepository;
import com.demo.savemymoney.data.repository.CategoryRepository;
import com.demo.savemymoney.data.repository.MainAmountRepository;
import com.demo.savemymoney.service.ClockNotifier;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class MainFragmentPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private CategoryRepository categoryRepository;
    private CategoryDetailRepository categoryDetailRepository;
    private MainAmountRepository mainAmountRepository;

    public MainFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        categoryRepository = new CategoryRepository(context);
        mainAmountRepository = new MainAmountRepository(context);
        categoryDetailRepository = new CategoryDetailRepository(context);
    }

    public void getCategoryList() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        categoryRepository.countCategories(uid)
                .addCallback(new FutureCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        if (result <= 0) {
                            categoryRepository.saveDefaultsAndGetCategories(uid)
                                    .addSuccessCallback(result1 -> view.showCategoryList(result1));
                        } else {
                            categoryRepository.getAll(uid)
                                    .addSuccessCallback(result12 -> view.showCategoryList(result12));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error counting categories", t);
                    }
                });
    }

    public void getMainAmount() {
        mainAmountRepository.findByUserUID(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<MainAmount>() {
                    @Override
                    public void onSuccess(MainAmount result) {
                        view.showMainAmount(result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error obtaining main amount", t);
                    }
                });
    }

    public void increaseAmount(BigDecimal amount) {
        mainAmountRepository.increaseAmount(firebaseAuth.getCurrentUser().getUid(), amount.doubleValue())
                .addCallback(new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        getMainAmount();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error increasing main amount", t);
                        view.notifyIncomeNotFound();
                    }
                });
    }

    public void decreaseAmount(BigDecimal amount) {
        mainAmountRepository.decreaseAmount(firebaseAuth.getCurrentUser().getUid(), amount.doubleValue())
                .addSuccessCallback(result -> getMainAmount());
    }

    public void changeAmount(BigDecimal amount) {
        mainAmountRepository.changeAmount(firebaseAuth.getCurrentUser().getUid(), amount.doubleValue())
                .addCallback(new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        getMainAmount();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error changing main amount", t);
                        view.notifyIncomeNotFound();
                    }
                });
    }

    public void deleteCategory(Category category) {
        categoryRepository.deleteCategory(category)
                .addCallback(new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        getMainAmount();
                        view.notifyCategoryDeleted();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error deleting category", t);
                        if (t instanceof CategoryHasDetailException)
                            view.showError(context.getString(R.string.category_delete_has_detail_error));
                    }
                });
    }

    public void getCountDetail(){

        categoryDetailRepository.getCountDetails(firebaseAuth.getCurrentUser().getUid() , new Date()).addCallback(new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                ClockNotifier.CountNotifier(context,result,firebaseAuth.getCurrentUser().getUid());

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    public interface View {

        void showProgress();

        void hideProgress();

        void showCategoryList(List<Category> result);

        void showMainAmount(MainAmount result);

        void notifyIncomeNotFound();

        void showError(String s);

        void notifyCategoryDeleted();
        void getCountDetails(Context context ,String uid,Integer count);
    }
}
