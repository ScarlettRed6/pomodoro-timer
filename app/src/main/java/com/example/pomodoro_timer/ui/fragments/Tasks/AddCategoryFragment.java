package com.example.pomodoro_timer.ui.fragments.Tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentTaskAddCategoryBinding;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

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

        for (ImageView icon : icons){
            icon.setOnClickListener(v -> {
                //Set dim if not selected
                for (ImageView notSelected : icons){
                    notSelected.setSelected(false);
                    notSelected.setAlpha(0.5f);
                }
                v.setSelected(true);
                v.setAlpha(1f);
                taskVM.setCategoryIcon(v.getContentDescription().toString());
            });
        }//End of foreach
    }//End of setIconSelector method

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
            String newCategoryIcon = taskVM.getCategoryIcon().getValue(); //Just test to get icon from content description,
            //Implement later the actual fetching of the icon and display it

            taskVM.addCategory(newCategoryTitle, newCategoryIcon);
            taskVM.clearCategoryFields();
            navController.popBackStack(R.id.menu_task, false);
            sharedVM.setInAddMode(false);

        });
    }

}
