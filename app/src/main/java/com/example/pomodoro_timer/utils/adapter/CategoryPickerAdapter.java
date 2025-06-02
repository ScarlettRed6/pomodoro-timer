package com.example.pomodoro_timer.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.ItemCategoryPickerBinding;
import com.example.pomodoro_timer.model.CategoryModel;

import java.util.List;

public class CategoryPickerAdapter extends ArrayAdapter<CategoryModel> {
    private LayoutInflater inflater; // Cache inflater

    public CategoryPickerAdapter(Context context, List<CategoryModel> categories) {
        super(context, 0, categories);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemCategoryPickerBinding binding; // Use the generated binding class

        if (convertView == null) {
            binding = ItemCategoryPickerBinding.inflate(inflater, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding); // Store binding in tag for reuse
        } else {
            binding = (ItemCategoryPickerBinding) convertView.getTag();
        }

        CategoryModel category = getItem(position);

        if (category != null) {
            binding.setCategoryModel(category); // Set the variable for data binding
            // The app:srcCompatTheme binding adapter will now handle the icon based on category.getIcon()

            // You can remove the manual title setting if it's also in your XML via data binding
            // binding.categoryTitle.setText(category.getCategoryTitle());
            // Log.d("CategoryTitle", "TITLE: " + category.getCategoryTitle());

            // The manual icon setting logic from before is NO LONGER NEEDED HERE
            // if you use the BindingAdapter below. If you don't use a BindingAdapter
            // for the icon, then you would need to resolve and set it manually on binding.categoryIcon
        }
        binding.executePendingBindings(); // Important for immediate UI update in adapters
        return convertView;
    }

}
