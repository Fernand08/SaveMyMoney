package com.demo.savemymoney.graphics;

import android.content.Context;

import com.demo.savemymoney.data.entity.SavingHistory;
import com.demo.savemymoney.data.repository.SavingHistoryRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SavingFragmentPresenter {
    private View view;
    private Context context;
    private SavingHistoryRepository savingHistoryRepository;
    private FirebaseAuth firebaseAuth;

    public SavingFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.savingHistoryRepository = new SavingHistoryRepository(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loadBarData() {
        savingHistoryRepository.getAll(firebaseAuth.getCurrentUser().getUid())
                .addSuccessCallback(result -> view.showBarData(result));
    }

    public interface View {

        void showBarData(List<SavingHistory> result);
    }
}
