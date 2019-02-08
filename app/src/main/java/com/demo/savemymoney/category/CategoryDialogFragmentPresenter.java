package com.demo.savemymoney.category;

import android.content.Context;
import android.util.Log;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.common.exceptions.CategoryNameAlreadyExistsException;
import com.demo.savemymoney.data.entity.Category;
import com.demo.savemymoney.data.repository.CategoryRepository;
import com.github.clemp6r.futuroid.FutureCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogFragmentPresenter {
    private View view;
    private Context context;
    private CategoryRepository categoryRepository;
    private FirebaseAuth firebaseAuth;

    public CategoryDialogFragmentPresenter(View view, Context context) {
        this.view = view;
        this.context = context;
        this.categoryRepository = new CategoryRepository(context);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void saveCategory() {
        Category category = view.getCategory();
        List<ErrorMessage> errors = getErrors(category);
        if (errors.isEmpty()) {
            categoryRepository.saveCategory(firebaseAuth.getCurrentUser().getUid(), category)
                    .addCallback(new FutureCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            view.notifySuccessSaveCategory();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(getClass().getName(), "Error saving new category", t);
                            if (t instanceof CategoryNameAlreadyExistsException)
                                view.showErrorMessage(context.getString(R.string.category_name_exists_error));
                        }
                    });
        } else
            view.showErrorMessages(errors);
    }

    private List<ErrorMessage> getErrors(Category category) {
        List<ErrorMessage> errors = new ArrayList<>();
        view.clearErrorMessages();
        if (category.name == null || category.name.isEmpty())
            errors.add(new ErrorMessage(R.id.category_name_til, "Debes ingresar un nombre"));
        else if (category.name.length() > 10)
            errors.add(new ErrorMessage(R.id.category_name_til, "No puede tener más de 10 caracteres"));
        else if (category.color == null || category.color.isEmpty())
            errors.add(new ErrorMessage(null, "Debes elegir un color para la categoría"));
        else if (category.icon == null)
            errors.add(new ErrorMessage(null, "Debes elegir un icono para la categoría"));
        return errors;

    }

    public interface View {

        Category getCategory();

        void showErrorMessages(List<ErrorMessage> errors);

        void clearErrorMessages();

        void showErrorMessage(String string);

        void notifySuccessSaveCategory();
    }
}
