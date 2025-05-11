package com.example.pomodoro_timer.ui.fragments.Settings;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.FragmentSettingsLoginAccountBinding;
import com.example.pomodoro_timer.viewmodels.SettingsViewModel;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;

public class LoginSettingsFragment extends Fragment {

    //Fields
    private FragmentSettingsLoginAccountBinding binding;
    private SharedViewModel sharedVM;
    private SettingsViewModel settingsVM;
    private NavController navController;

    public LoginSettingsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSettingsLoginAccountBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        sharedVM = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        navController = NavHostFragment.findNavController(LoginSettingsFragment.this);
        binding.setSettingsVM(settingsVM);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        //Context here
        initStuff();

        return binding.getRoot();
    }

    private void initStuff(){
        onSignupClick();
        showTogglePassword();
        onLoginClick();
    }//End of initStuff method

    private void onSignupClick(){
        binding.signUpTextBtnId.setOnClickListener(v -> {
            //Set visibility to GONE
            binding.loginTitleId.setText(R.string.sign_up_title);
            binding.loginGroup.setVisibility(View.GONE);
            //Set Visibility to VISIBLE
            binding.signInGroup.setVisibility(View.VISIBLE);

        });
    }//End of onSignupClick

    private void showTogglePassword(){
        binding.showPasswordCheckboxId.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

            binding.newPasswordInputId.setInputType(inputType);
            binding.confirmPasswordInputId.setInputType(inputType);
        });
    }//End of showTogglePassword method

    private void onLoginClick(){
        binding.loginBtnId.setOnClickListener(v -> settingsVM.login());
        settingsVM.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result){
                sharedVM.setIsUserLoggedIn(true);
                navController.popBackStack(R.id.menu_timer, false);
            }else{
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }//End of onLoginClick method

}
