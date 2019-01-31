package com.demo.savemymoney.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.CategoryDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryDetailAdapter extends RecyclerView.Adapter<CategoryDetailAdapter.CategoryDetailViewHolder> {

    private List<CategoryDetail> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonClickListener listener;

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClicked(CategoryDetail categoryDetail);
    }

    public CategoryDetailAdapter(Context context, OnDeleteButtonClickListener listener) {
        this.data = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CategoryDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.category_detail_item_layout, parent, false);
        return new CategoryDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CategoryDetail> newData) {
        if (data != null) {
            CategoryDetailDiffCallback postDiffCallback = new CategoryDetailDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(postDiffCallback);
            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            data = newData;
        }
    }

    class CategoryDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.delete_button)
        AppCompatImageButton deleteButton;

        CategoryDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final CategoryDetail categoryDetail) {
            if (categoryDetail != null) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
                format.setCurrency(Currency.getInstance("PEN"));
                amount.setText(String.format("- %s", format.format(categoryDetail.amount)));
                date.setText(DateUtils.getRelativeTimeSpanString(categoryDetail.date.getTime()));
                description.setText(categoryDetail.description);
                deleteButton.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onDeleteButtonClicked(categoryDetail);
                });
            }
        }

    }

    class CategoryDetailDiffCallback extends DiffUtil.Callback {

        private final List<CategoryDetail> oldPosts, newPosts;

        public CategoryDetailDiffCallback(List<CategoryDetail> oldPosts, List<CategoryDetail> newPosts) {
            this.oldPosts = oldPosts;
            this.newPosts = newPosts;
        }

        @Override
        public int getOldListSize() {
            return oldPosts.size();
        }

        @Override
        public int getNewListSize() {
            return newPosts.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).detailId == newPosts.get(newItemPosition).detailId;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
        }
    }
}
