package com.example.pomodoro_timer.utils.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.databinding.ItemCategoryBinding;
import com.example.pomodoro_timer.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    //Fields
    private List<CategoryModel> categoryList = new ArrayList<>();

    public void setCategoryList(List<CategoryModel> categories){
        this.categoryList = categories;
        //Log.d("CategoryAdapter", "TEST SET CATEGORY LIST!");
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
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryModel category){
            binding.setCategoryModel(category);
            binding.executePendingBindings();
        }
    }//End of setCategoryList method

}
