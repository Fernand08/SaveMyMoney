package com.demo.savemymoney.graphics;

import android.content.Context;

import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.demo.savemymoney.data.repository.CategoryDetailHistoryRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SpendingHistoryFragmentPresenter {
    private View view;
    private Context context;
    private CategoryDetailHistoryRepository categoryDetailHistoryRepository;
    private FirebaseAuth firebaseAuth;

    public SpendingHistoryFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.categoryDetailHistoryRepository = new CategoryDetailHistoryRepository(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void findByMonth(int year, int month) {
        categoryDetailHistoryRepository.getAll(firebaseAuth.getCurrentUser().getUid(), year,month)
                .addSuccessCallback(result -> view.updateHistoryList(result));

    }

    public interface View {
        void updateHistoryList(List<CategoryDetailHistory> result);
    }
}
