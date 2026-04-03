package com.n7.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n7.miniproject2.R;
import com.n7.miniproject2.entities.Category;
import com.n7.miniproject2.listeners.OnItemClicked;

import java.util.List;

public class ListCategoriesAdapter extends RecyclerView.Adapter<ListCategoriesAdapter.ViewHolder> {

    private final List<Category> categories;
    private OnItemClicked<Category> onItemClicked;

    public ListCategoriesAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public void setOnItemClicked(OnItemClicked<Category> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoryName;
        private final TextView tvCategoryDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryDescription = itemView.findViewById(R.id.tvCategoryDescription);
        }

        public void bind(Category category) {
            tvCategoryName.setText(category.getName());
            tvCategoryDescription.setText("(" + category.getDescription() + ")");

            itemView.setOnClickListener(v -> {
                if (onItemClicked != null) {
                    onItemClicked.onItemClick(category);
                }
            });
        }
    }
}