package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentEditCategoryBinding;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class EditCategoryFragment extends Fragment {

    //Fields
    private FragmentEditCategoryBinding binding;
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private NavController navController;
    private ImageView[] icons;
    private Integer userId;
    private boolean isUserLoggedIn = false;

    public EditCategoryFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(EditCategoryFragment.this);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initializeStuff();

        return binding.getRoot();
    }//End of onCreateView

    private void initializeStuff(){
        checkUser();
        setIconSelector();
        observeIcon();
        saveCategory();
        onCancelBtnClick();
    }//End of initializeStuff method

    private void checkUser(){
        userId = sharedVM.getCurrentUserId().getValue();
        isUserLoggedIn = sharedVM.getIsUserLoggedIn().getValue();
    }//End of checkUser method

    private void observeIcon() {
        taskVM.getCategoryIcon().observe(getViewLifecycleOwner(), iconId -> {
            if (iconId != null) {
                for (ImageView iconView : icons) {
                    if (iconView.getTag() != null && iconView.getTag().equals(iconId)) {
                        setIconSelection(iconView, iconId);
                        break;
                    }
                }
            }//End of if statement
        });
    }//End of observeIcon method

    private void setIconSelector(){
        icons = new ImageView[]{
                binding.icon1,
                binding.icon2
        };

        binding.icon1.setTag(R.attr.categoryLaptopIcon);
        binding.icon2.setTag(R.attr.categoryBookIcon);

        binding.icon1.setOnClickListener(v -> {
            setIconSelection(v, R.attr.categoryLaptopIcon);
        });

        binding.icon2.setOnClickListener(v -> {
            setIconSelection(v, R.attr.categoryBookIcon);
        });

    }//End of setIconSelector method

    private void setIconSelection(View selectedIcon, int iconDrawableId) {
        for (ImageView notSelected : icons) {
            notSelected.setSelected(false);
            notSelected.setAlpha(0.5f);
        }

        selectedIcon.setSelected(true);
        selectedIcon.setAlpha(1f);

        //Prevent infinite LiveData triggering
        if (!iconDrawableIdEquals(iconDrawableId)) {
            taskVM.setCategoryIcon(iconDrawableId);
        }
    }//End of setIconSelection method

    private boolean iconDrawableIdEquals(int newIconId) {
        Integer current = taskVM.getCategoryIcon().getValue();
        return current != null && current == newIconId;
    }//End of iconDrawableIdEquals method

    private void onCancelBtnClick(){
        binding.cancelBtnId.setOnClickListener(v -> {
            taskVM.clearCategoryFields();
            navController.popBackStack(R.id.menu_task, false);
        });
    }

    private void saveCategory(){
        binding.saveBtnId.setOnClickListener(v -> {
            String categoryTitle = taskVM.getCategoryTitle().getValue();
            int categoryIcon = taskVM.getCategoryIcon().getValue();
            String categoryDescription = taskVM.getCategoryDescription().getValue();
            int categoryId = taskVM.getCategory().getValue() != null ? taskVM.getCategory().getValue().getId() : 0;

            if (isUserLoggedIn)
                taskVM.updateCategory(userId, categoryId, categoryTitle, categoryIcon, categoryDescription);
            else
                taskVM.updateCategory(1, categoryId, categoryTitle, categoryIcon, categoryDescription);

            navController.popBackStack(R.id.menu_task, false);
            taskVM.clearCategoryFields();
        });
    }//End of saveCategory method

}



