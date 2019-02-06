package com.demo.savemymoney.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseDialogFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Category;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconView;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static cn.pedant.SweetAlert.SweetAlertDialog.SUCCESS_TYPE;
import static com.maltaisn.icondialog.IconDialog.VISIBILITY_ALWAYS;

public class CategoryDialogFragment extends BaseDialogFragment implements SpectrumDialog.OnColorSelectedListener, IconDialog.Callback, CategoryDialogFragmentPresenter.View {

    @BindView(R.id.icon_select_button)
    IconView iconView;

    @BindView(R.id.color_select_button)
    Button colorSelectButton;

    @BindView(R.id.categoryNameEditText)
    EditText categoryNameEditText;

    @BindView(R.id.category_name_til)
    TextInputLayout categoryNameTil;

    private String selectedColor;
    private Icon selectedIcon;
    private CategoryDialogFragmentPresenter presenter;
    private AddCategoryListener listener;

    public static AppCompatDialogFragment newInstance(@NonNull AddCategoryListener listener) {

        CategoryDialogFragment dialog = new CategoryDialogFragment();
        dialog.setAddCategoryListener(listener);
        return dialog;
    }

    private void setAddCategoryListener(AddCategoryListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_dialog, container, false);
        ButterKnife.bind(this, v);
        presenter = new CategoryDialogFragmentPresenter(this, getContext());
        return v;
    }

    @OnClick({R.id.color_select_button, R.id.color_select_label})
    public void onColorButtonPressed() {
        new SpectrumDialog.Builder(getContext())
                .setDismissOnColorSelected(true)
                .setColors(R.array.demo_colors)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(this)
                .build()
                .show(getFragmentManager(), "dialog_color_picker");
    }

    @Override
    public void onColorSelected(boolean positiveResult, int color) {
        if (positiveResult) {
            selectedColor = "#" + Integer.toHexString(color).toUpperCase();
            colorSelectButton.setBackgroundColor(color);
        }
    }

    @OnClick({R.id.icon_select_button, R.id.icon_select_label})
    public void onIconButtonPressed() {
        IconDialog iconDialog = new IconDialog();
        iconDialog.setTargetFragment(this, 0);
        iconDialog.setTitle(VISIBILITY_ALWAYS, getString(R.string.category_select_icon_title));
        iconDialog.show(getActivity().getSupportFragmentManager(), "icon_dialog");

    }

    @Override
    public void onIconDialogIconsSelected(Icon[] icons) {
        Icon icon = icons[0];
        iconView.setIcon(icon);
        selectedIcon = icon;
    }

    @OnClick(R.id.cancel_button)
    public void onCancel() {
        this.dismiss();
    }

    @OnClick(R.id.accept_button)
    public void onAccept() {
        presenter.saveCategory();
    }

    @Override
    public Category getCategory() {
        Category category = new Category();
        category.name = categoryNameEditText.getText().toString();
        category.color = selectedColor;
        if (selectedIcon != null)
            category.icon = selectedIcon.getId();
        category.distributedAmountReference = 0.00;
        category.distributedAmount = 0.00;
        category.isDeletable = true;
        category.isSaving = false;
        return category;
    }

    @Override
    public void showErrorMessages(List<ErrorMessage> errors) {
        for (ErrorMessage error : errors) {
            if (error.getInputId() == null)
                showError(error.getMessage());
            else {
                TextInputLayout input = getView().findViewById(error.getInputId());
                input.setErrorEnabled(true);
                input.setError(error.getMessage());
            }
        }
    }

    @Override
    public void clearErrorMessages() {
        categoryNameTil.setErrorEnabled(false);
        categoryNameTil.setError("");
    }

    @Override
    public void showErrorMessage(String string) {
        showError(string);
    }

    @Override
    public void notifySuccessSaveCategory() {
        dismiss();
        new SweetAlertDialog(getActivity(), SUCCESS_TYPE)
                .setTitleText(getString(R.string.success_title))
                .setContentText(getString(R.string.category_success_save))
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    listener.onCategoryAdded();
                })
                .show();
    }

    public interface AddCategoryListener {
        void onCategoryAdded();
    }
}
