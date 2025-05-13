package com.example.pomodoro_timer.utils.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.databinding.ItemCategoryBinding;
import com.example.pomodoro_timer.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // Listener interface for category click
    public interface CategoryClickListener {
        void onCategoryClick();
    }

    private List<CategoryModel> categoryList = new ArrayList<>();
    private final CategoryClickListener listener;

    // Constructor with click listener
    public CategoryAdapter(CategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategoryList(List<CategoryModel> categories) {
        this.categoryList = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.bind(category);

        // Make entire square clickable
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryModel category) {
            binding.setCategoryModel(category);
            binding.executePendingBindings();
        }
    }
}
