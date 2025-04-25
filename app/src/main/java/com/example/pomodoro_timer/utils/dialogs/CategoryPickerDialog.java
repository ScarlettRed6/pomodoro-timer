package com.example.pomodoro_timer.utils.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.utils.adapter.CategoryPickerAdapter;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryPickerDialog extends DialogFragment {

    private TaskViewModel taskVM;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = new Dialog(requireContext(), R.style.RoundedCornersDialog);
        dialog.setContentView(R.layout.fragment_task_category_picker);

        ListView listView = dialog.findViewById(R.id.category_list_view);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        List<CategoryModel> categories = taskVM.getCategoryList().getValue();
        CategoryPickerAdapter adapter = new CategoryPickerAdapter(requireContext(), categories);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (categories != null && position < categories.size()) {
                CategoryModel selectedCategory = categories.get(position);
                taskVM.getCategoryTitle().setValue(selectedCategory.getCategoryTitle());
                taskVM.getCategoryIcon().setValue(selectedCategory.getIcon());
                taskVM.setCategory(selectedCategory);
                dismiss();
            }
        });

        return dialog;
    }//End of onCreateDialog method

}
