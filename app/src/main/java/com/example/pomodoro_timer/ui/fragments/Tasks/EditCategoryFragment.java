package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pomodoro_timer.databinding.FragmentEditCategoryBinding;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class EditCategoryFragment extends Fragment {

    //Fields
    private FragmentEditCategoryBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;

    public EditCategoryFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here

        return binding.getRoot();
    }//End of onCreateView

}



