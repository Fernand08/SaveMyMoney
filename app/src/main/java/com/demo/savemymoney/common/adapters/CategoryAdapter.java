package com.demo.savemymoney.common.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.Category;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.demo.savemymoney.common.util.NumberFormatUtils.formatAsCurrency;

public class CategoryAdapter extends BaseAdapter {
    private List<Category> categories;
    private Context context;
    private CategoryActionsListener listener;

    public CategoryAdapter(@NonNull List<Category> categories, @NonNull Context context, CategoryActionsListener listener) {
        this.categories = categories;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View categoryView;
        if (convertView == null) {
            Category category = getItem(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (!category.isAddOption) {
                categoryView = inflater.inflate(R.layout.category_item_layout, null);
                TextView categoryNameTv = categoryView.findViewById(R.id.category_name);
                TextView categoryAmountTv = categoryView.findViewById(R.id.category_amount);
                ImageView categoryIcon = categoryView.findViewById(R.id.category_icon);
                categoryIcon.setImageResource(category.icon);
                categoryNameTv.setText(category.name);
                categoryAmountTv.setText(formatAsCurrency(category.distributedAmount));
                categoryView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                categoryView.setBackgroundColor(Color.parseColor(category.color));
                categoryView.setOnClickListener(v -> listener.onSelectCategory(category));
            } else {
                categoryView = inflater.inflate(R.layout.category_add_layout, null);
                FloatingActionButton button = categoryView.findViewById(R.id.add_button);
                button.setOnClickListener(v -> listener.onAdd());
            }
        } else
            categoryView = convertView;

        return categoryView;
    }

    public interface CategoryActionsListener {
        void onSelectCategory(Category category);

        void onAdd();
    }
}
