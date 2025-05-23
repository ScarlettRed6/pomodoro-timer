package com.example.pomodoro_timer.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.ItemCategoryBinding;
import com.example.pomodoro_timer.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    //Fields
    private List<CategoryModel> categoryList = new ArrayList<>();
    private final CategoryItemMenuListener categoryListener;

    //Interface callback
    public interface CategoryItemMenuListener {
        void onViewCategory(CategoryModel category);
        void onEditCategory(CategoryModel category);
        void onDeleteCategory(CategoryModel category);
    }

    // Constructor with click listener
    public CategoryAdapter(CategoryItemMenuListener listener) {
        this.categoryListener = listener;
    }

    public void setCategoryList(List<CategoryModel> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryModel category) {
            binding.setCategoryModel(category);
            binding.executePendingBindings();

            binding.categoryCard.setOnClickListener(v -> {
                Context wrapperTheme = new ContextThemeWrapper(v.getContext(), R.style.PopupMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapperTheme, v);
                popupMenu.inflate(R.menu.category_item_options_menu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.view_category) {
                        categoryListener.onViewCategory(category);
                        //Log.d("CategoryAdapter", "VIEW CATEGORY CLICKED!");
                        return true;
                    }else if (itemId == R.id.edit_category) {
                        categoryListener.onEditCategory(category);
                        //Log.d("CategoryAdapter", "EDIT CATEGORY CLICKED!");
                        return true;
                    } else if (itemId == R.id.delete_category) {
                        categoryListener.onDeleteCategory(category);
                        //Log.d("CategoryAdapter", "DELETE CATEGORY CLICKED!");
                        return true;
                    }
                    return false;
                });//End of setOnMenuItemClickListener
                popupMenu.show();
            });

        }//End of bind
    }//End of CategoryViewHolder
}
