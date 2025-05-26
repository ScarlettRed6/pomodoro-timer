package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsAccountControlsBinding;
import com.example.pomodoro_timer.utils.shared_preferences.SessionManager;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.example.pomodoro_timer.viewmodels.TaskViewModel;

public class AccountSettingsControl extends Fragment {

    //Fields
    private FragmentSettingsAccountControlsBinding binding;
    private SettingsViewModel settingsVM;
    private TaskViewModel taskVM;
    private SharedViewModel sharedVM;
    private NavController navController;

    public AccountSettingsControl(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSettingsAccountControlsBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        taskVM = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(AccountSettingsControl.this);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();

        return binding.getRoot();
    }//End of onCreateView

    private void initStuff(){
        setUsernameBySession();
        onEditProfile();
        onLogout();
    }

    private void setUsernameBySession(){
        if (sharedVM.getIsUserLoggedIn().getValue()){
            settingsVM.setLoginEmail(sharedVM.getCurrentEmail().getValue());
        }
    }//End of setUsernameBySession method

    private void onLogout(){
        SessionManager sessionManager = new SessionManager(requireContext());
        binding.logoutBtnId.setOnClickListener(v -> {
            sessionManager.clearLoginSession();
            settingsVM.setLoginEmail(null);
            settingsVM.setLoginPassword(null);
            sharedVM.setIsUserLoggedIn(false);
            sharedVM.setCurrentEmail("Email");
            sharedVM.setCurrentUserId(-1);  //Set to invalid user ID
            navController.popBackStack(R.id.menu_timer, false);
        });
    }//End of onLogout method

    private void onEditProfile(){
        binding.editProfileLayoutId.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_account_container_view_id, new AccountSettingsEditProfile())
                    .commit();

        });
    }//End of onEditProfile method

}
