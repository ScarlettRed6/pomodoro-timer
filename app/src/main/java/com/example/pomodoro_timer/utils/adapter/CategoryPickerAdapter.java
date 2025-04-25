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
import com.example.pomodoro_timer.model.CategoryModel;

import java.util.List;

public class CategoryPickerAdapter extends ArrayAdapter<CategoryModel> {

    public CategoryPickerAdapter(Context context, List<CategoryModel> categories){
        super(context, 0 , categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CategoryModel category = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category_picker, parent, false);
        }

        ImageView iconImageView = convertView.findViewById(R.id.category_icon);
        TextView titleTextView = convertView.findViewById(R.id.category_title);

        try{
            titleTextView.setText(category.getCategoryTitle());
            Log.d("CategoryTitle", "TITLE: " + category.getCategoryTitle());
        }catch (NullPointerException e){
            Log.d("CategoryPickerAdapter", "NULL POINTER EXCEPTION ENCOUNTERED: category");
        }
        iconImageView.setImageResource(R.drawable.ic_category_laptop);

        return convertView;
    }//End of getView method

}
