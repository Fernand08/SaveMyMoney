package com.demo.savemymoney.category;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.exceptions.CategoryInvalidAmountException;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.entity.CategoryDetail;
import com.demo.savemymoney.data.entity.Goal;
import com.demo.savemymoney.data.entity.MainAmount;
import com.demo.savemymoney.data.repository.CategoryDetailRepository;
import com.demo.savemymoney.data.repository.CategoryRepository;
import com.demo.savemymoney.data.repository.GoalRepository;
import com.demo.savemymoney.data.repository.MainAmountRepository;
import com.demo.savemymoney.goal.GoalFragment;
import com.demo.savemymoney.main.MainActivity;
import com.github.clemp6r.futuroid.FutureCallback;
import com.github.clemp6r.futuroid.SuccessCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.math.BigDecimal;
import java.util.List;

public class CategoryFragmentPresenter {
    private View view;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private CategoryRepository categoryRepository;
    private CategoryDetailRepository categoryDetailRepository;
    private MainAmountRepository mainAmountRepository;
    private GoalRepository goalRepository;
    private  SharedPreferences preferences;
    public CategoryFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.firebaseAuth = FirebaseAuth.getInstance();
        categoryRepository = new CategoryRepository(context);
        categoryDetailRepository = new CategoryDetailRepository(context);
        mainAmountRepository = new MainAmountRepository(context);
        goalRepository = new GoalRepository(context);
        preferences = context.getSharedPreferences("pref",Context.MODE_PRIVATE);
    }

    public void increaseDistributedAmount(Integer categoryId, BigDecimal amount) {

        categoryRepository.increaseDistributedAmount(firebaseAuth.getCurrentUser().getUid(), categoryId, amount.doubleValue())
                .addCallback(new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        loadCategory(categoryId);

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error increasing category amount", t);
                        if (t instanceof CategoryInvalidAmountException)
                            view.showError(context.getString(R.string.category_increase_error));
                    }
                });
    }

    public void decreaseDistributedAmount(Integer categoryId, BigDecimal amount) {
        categoryRepository.decreaseDistributedAmount(firebaseAuth.getCurrentUser().getUid(), categoryId, amount.doubleValue())
                .addSuccessCallback(result -> loadCategory(categoryId));
    }

    public void changeDistributedAmount(Integer categoryId, BigDecimal amount) {
        categoryRepository.changeDistributedAmount(firebaseAuth.getCurrentUser().getUid(), categoryId, amount.doubleValue())
                .addCallback(new FutureCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        loadCategory(categoryId);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error increasing category amount", t);
                        if (t instanceof CategoryInvalidAmountException)
                            view.showError(context.getString(R.string.category_increase_error));
                    }
                });
    }

    private void loadCategory(Integer categoryId) {
        categoryRepository.findByUserUIDAndCategoryId(firebaseAuth.getCurrentUser().getUid(), categoryId)
                .addSuccessCallback(result -> view.updateCategory(result) );
    }

    public void saveDetail(CategoryDetail detail) {
        detail.userUID = firebaseAuth.getCurrentUser().getUid();
        categoryDetailRepository.saveDetail(detail)
                .addSuccessCallback(result -> {
                    view.showSuccess(context.getString(R.string.category_detail_success_register));
                    loadCategory(detail.categoryId);
                });

    }

    public LiveData<List<CategoryDetail>> getDetail(Integer categoryId) {
        return categoryDetailRepository.getAll(firebaseAuth.getCurrentUser().getUid(), categoryId);
    }

    public void deleteDetail(CategoryDetail detail) {
        categoryDetailRepository.deleteDetail(detail)
                .addSuccessCallback(result -> {
                    view.showSuccess(context.getString(R.string.category_detail_success_deleted));
                    loadCategory(detail.categoryId);
                });
    }

    public void afterAddCategory(Category category) {
        mainAmountRepository.findByUserUID(category.userUID)
                .addSuccessCallback(new SuccessCallback<MainAmount>() {
                    @Override
                    public void onSuccess(MainAmount result) {
                        if (result.amount <= 0.00 && category.distributedAmount <= 0.00)
                            view.showChihuanDialog();
                        else if (result.amount > 0.00 && category.distributedAmount <= 0.00)
                            view.showDistributeMessage();
                        else
                            view.addSpending();
                    }
                });
    }
    public void loadingGoal(){
        goalRepository.getGoal(firebaseAuth.getCurrentUser().getUid())
                .addCallback(new FutureCallback<Goal>() {
                    @Override
                    public void onSuccess(Goal result) {

                        if (result != null) {
                            view.loadSaving(result);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e(getClass().getName(), "Error loading goal", t);
                    }
                });

    }

    public  void deleteGoal(){

        goalRepository.deleteGoalSaving(firebaseAuth.getCurrentUser().getUid()).addSuccessCallback(res -> {

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("memoryGoal");
            editor.remove("memoryGoaldes");
            editor.remove("inspectGoal");
            editor.remove("memorySaving");
            editor.remove("montoSaving");
            editor.remove("montoSavingCat");
            editor.commit();



        });

    }

    public interface View {

        void showError(String string);

        void updateCategory(Category result);

        void showSuccess(String string);

        void showChihuanDialog();

        void showDistributeMessage();

        void addSpending();
        void loadSaving(Goal goal);
        void reload(Fragment fragment);
        void reloadA(Class activity);
    }
}
