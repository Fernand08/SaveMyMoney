package com.demo.savemymoney.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.demo.savemymoney.R;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconView;
import com.thebluealliance.spectrum.SpectrumDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.maltaisn.icondialog.IconDialog.VISIBILITY_ALWAYS;

public class CategoryDialogFragment extends AppCompatDialogFragment implements SpectrumDialog.OnColorSelectedListener, IconDialog.Callback {

    @BindView(R.id.icon_select_button)
    IconView iconView;

    @BindView(R.id.color_select_button)
    Button colorSelectButton;

    private String selectedColor;
    private Icon selectedIcon;

    public static AppCompatDialogFragment newInstance() {
        return new CategoryDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_dialog, container, false);
        ButterKnife.bind(this, v);
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
}
