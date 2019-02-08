package com.demo.savemymoney.graphics;

import android.content.Context;

import com.demo.savemymoney.data.repository.ReportRepository;
import com.demo.savemymoney.data.tuples.CategoryPieTuple;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class CategoryPieFragmentPresenter {
    private View view;
    private Context context;
    private ReportRepository reportRepository;
    private FirebaseAuth firebaseAuth;

    public CategoryPieFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        reportRepository = new ReportRepository(context);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loadPieData() {
        reportRepository.categoryPieList(firebaseAuth.getCurrentUser().getUid())
                .addSuccessCallback(result -> view.showPieData(result));
    }

    public interface View {

        void showPieData(List<CategoryPieTuple> result);
    }
}
