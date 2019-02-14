package com.demo.savemymoney.graphics;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.savemymoney.R;
import com.demo.savemymoney.data.entity.CategoryDetailHistory;
import com.maltaisn.icondialog.IconView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.demo.savemymoney.common.util.NumberFormatUtils.formatAsCurrency;

public class SpendingHistoryAdapter extends RecyclerView.Adapter<SpendingHistoryAdapter.SpendingHistoryViewHolder> {

    private List<CategoryDetailHistory> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public SpendingHistoryAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SpendingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.history_item_layout, parent, false);
        return new SpendingHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingHistoryViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<CategoryDetailHistory> newData) {
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

    class SpendingHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.category_name)
        TextView categoryName;
        @BindView(R.id.layout)
        LinearLayout layout;

        @BindView(R.id.category_icon)
        IconView iconView;

        SpendingHistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final CategoryDetailHistory history) {
            if (history != null) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                amount.setText(String.format("- %s", formatAsCurrency(history.amount)));
                date.setText(dateFormatter.format(history.date.getTime()));
                description.setText(history.description);
                layout.setBackgroundColor(Color.parseColor(history.category.color));
                categoryName.setText(history.category.name);
                iconView.setIcon(history.category.icon);
            }
        }

    }

    class CategoryDetailDiffCallback extends DiffUtil.Callback {

        private final List<CategoryDetailHistory> oldPosts, newPosts;

        public CategoryDetailDiffCallback(List<CategoryDetailHistory> oldPosts, List<CategoryDetailHistory> newPosts) {
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
