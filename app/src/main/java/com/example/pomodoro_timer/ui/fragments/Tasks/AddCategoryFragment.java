package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskAddCategoryBinding;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryFragment extends Fragment {

    //Fields
    private SharedViewModel sharedVM;
    private TaskViewModel taskVM;
    private FragmentTaskAddCategoryBinding binding;
    private NavController navController;
    private ImageView[] icons;

    public AddCategoryFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTaskAddCategoryBinding.inflate(inflater, container, false);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        navController = NavHostFragment.findNavController(AddCategoryFragment.this);
        binding.setTaskVM(taskVM);
        binding.setLifecycleOwner(this);

        //Set the add mode to true
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedVM.setInAddMode(true);

        //Initialize things, i should probably do this to other classes
        init();

        return binding.getRoot();
    }

    private void init(){
        setIconSelector();
        onCancelBtnClick();
        onSaveBtnClick();
    }

    private void setIconSelector(){
        icons = new ImageView[]{
                binding.icon1,
                binding.icon2
        };

        binding.icon1.setOnClickListener(v ->{
            setIconSelection(v, R.drawable.ic_category_laptop);
        });
        binding.icon2.setOnClickListener(v ->{
            setIconSelection(v, R.drawable.ic_category_book);
        });

    }//End of setIconSelector method

    private void setIconSelection(View selectedIcon, int iconDrawableId){
        for(ImageView notSelected: icons){
            notSelected.setSelected(false);
            notSelected.setAlpha(0.5f);
        }
        selectedIcon.setSelected(true);
        selectedIcon.setAlpha(1f);

        if(selectedIcon.getId() == R.id.icon1){
            taskVM.setCategoryIcon(R.drawable.ic_category_laptop);
        }else {
            taskVM.setCategoryIcon(R.drawable.ic_category_book);
        }

    }

    private void onCancelBtnClick(){
        binding.cancelBtnId.setOnClickListener(v -> {
            taskVM.clearCategoryFields();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);
        });
    }

    private void onSaveBtnClick(){
        binding.saveBtnId.setOnClickListener(v -> {
            String newCategoryTitle = taskVM.getCategoryTitle().getValue();
            int newCategoryIcon = taskVM.getCategoryIcon().getValue(); //Just test to get icon from content description,
            //Implement later the actual fetching of the icon and display it

            taskVM.addCategory(newCategoryTitle, newCategoryIcon);
            taskVM.clearCategoryFields();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);

        });
    }

}
