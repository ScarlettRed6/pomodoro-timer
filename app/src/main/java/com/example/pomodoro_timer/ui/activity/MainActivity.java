package com.example.pomodoro_timer.ui.activity;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.utils.shared_preferences.SessionManager;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //Fields
    private ConstraintLayout mainLayout;
    //private LinearLayout profileContainer;
    //private View dropdownOverlay;
    //private LinearLayout dropdownMenu;
    //private MaterialTextView viewProfile;
    private SharedViewModel sharedVM;
    private boolean isLoggedIn = false;

    //method for edge to edge(optional)
    protected void edge(){
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set first the theme
        // Load selected theme from preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int themeIndex = prefs.getInt("selected_theme_index", 0);

        // Apply theme before calling super.onCreate
        if (themeIndex == 0) {
            setTheme(R.style.Theme_Pomodorotimer);
            Log.d("MYTHEME", "Theme 1: " + themeIndex);
            //Restart app
            //recreate();
        } else {
            setTheme(R.style.AppTheme_Theme2);
            Log.d("MYTHEME", "BASE THEME: " + themeIndex);
            //recreate();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //Put context here
        angInit();

    }//End of onCreate method

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LOG_CHECK_DESTROY", "onDestroy() called. HashCode: " + this.hashCode());
        // Are there any listeners or resources you manually manage that should be cleaned up here?
    }

    private void angInit(){
        mainLayout = findViewById(R.id.main);
        //mainLayout.setBackgroundResource(R.drawable.background_app1);
        //profileContainer = findViewById(R.id.profile_container_id);
        //viewProfile = findViewById(R.id.view_profile);
        sharedVM = new ViewModelProvider(this).get(SharedViewModel.class);
        isLoggedIn = sharedVM.getIsUserLoggedIn().getValue();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) // Use android.Manifest here
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1); // And here
            }
        }

        setGuest();
        restoreSession();
        setupNavigation();
        showFab();
        //showProfileDropDown();
    }//End of angInit method

    private void setGuest(){
        if(!isLoggedIn){
            sharedVM.ensureGuestUserExists();
            sharedVM.setCurrentEmail("GuestEmail@guest.com");
        }
    }//End of setGuest method

    private void setupNavigation() {
        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHost.getNavController();

        //Setup bottom nav control
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        SharedViewModel sharedVM = new ViewModelProvider(this).get(SharedViewModel.class);

//        //Navigate to settings screen from view profile
//        viewProfile.setOnClickListener(v -> {
//            Log.d("Dropdown", "View Profile clicked");
//
//            //Delays to allow navigation to happen first
//            v.postDelayed(() -> {
//                hideDropdown();
//                bottomNav.setSelectedItemId(R.id.menu_settings);
//            }, 100);
//        });

        //Setup FAB control
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            sharedVM.setShowAddTaskBtn(destId == R.id.menu_task); //Show FAB only in Task
            changeBackground(destId);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int destId = item.getItemId();
            int currentId = navController.getCurrentDestination() != null ? navController.getCurrentDestination().getId() : -1;

            //Return this in the condition statement if not working :     && destId != R.id.menu_task
            if (currentId == R.id.addFragment) {
                Boolean inAdd = sharedVM.getInAddMode().getValue();

                Log.d("AddModeCheck", "ID ADD MODE: " + inAdd);
                if (inAdd != null && inAdd) {
                    showLeaveAddDialog(() -> {
                        sharedVM.setInAddMode(false);
                        navController.navigate(destId);
                        bottomNav.setSelectedItemId(destId);
                    });
                    return false;
                }

            }//End of if statement

            NavOptions options = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setEnterAnim(R.anim.fade_in)
                    .setExitAnim(R.anim.fade_out)
                    .setPopEnterAnim(R.anim.fade_in)
                    .setPopExitAnim(R.anim.fade_out)
                    .build();
            navController.navigate(destId, null, options);
            changeBackground(destId);
            return true;
        });

    }//End of setupNavigation method

    private void changeBackground(int destinationId) {
        int backgroundAttr = R.attr.backgroundDefaultScreen;

        if (destinationId == R.id.menu_task) {
            backgroundAttr = R.attr.backgroundTaskScreen;
        } else if (destinationId == R.id.menu_timer) {
            backgroundAttr = R.attr.backgroundTimerScreen;
        } else if (destinationId == R.id.menu_stats) {
            backgroundAttr = R.attr.backgroundStatsScreen;
        } else if (destinationId == R.id.menu_settings) {
            backgroundAttr = R.attr.backgroundSettingsScreen;
        }
        // Resolve the theme attribute to an actual resource ID
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(backgroundAttr, typedValue, true);

        if (typedValue.resourceId != 0) {
            mainLayout.setBackgroundResource(typedValue.resourceId);
        } else {
            // Fallback if the attribute is not defined in the current theme
            // This shouldn't happen if you've defined it in all themes.
            // You could set a default color or a common default drawable here.
            Log.w("MainActivity", "Background attribute not found in theme, using default.");
            // Example fallback:
            // mainLayout.setBackgroundResource(R.drawable.default_fallback_background);
            // Or get another attribute like backgroundDefaultScreen if it was missed in the if-else
        }
    }

    private void showLeaveAddDialog(Runnable onConfirm) {
        Log.d("TaskFragment", "DIALOG CALLED!");
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Discard Changes?")
                .setMessage("You have unsaved changes. Are you sure you want to leave?")
                .setPositiveButton("Yes", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showFab(){
        SharedViewModel sharedVM = new ViewModelProvider(this).get(SharedViewModel.class);

        View fab = findViewById(R.id.add_task_category_btn_id);
        sharedVM.getShowAddTaskBtn().observe(this, show -> {
            if(show){
                fab.setAlpha(0f);
                fab.setRotation(270);
                fab.setVisibility(View.VISIBLE);
                fab.animate()
                        .alpha(1f)
                        .rotation(0)
                        .setDuration(300)
                        .start();
            }else{
                fab.animate()
                        .alpha(0f)
                        .rotation(270)
                        .setDuration(300)
                        .withEndAction(() -> fab.setVisibility(View.GONE))
                        .start();
            }
        });
        fab.setOnClickListener(v -> sharedVM.onAddBtnClick());

    }//End of showFab method

//    private void showProfileDropDown() {
//        dropdownOverlay = findViewById(R.id.dropdown_overlay);
//        dropdownMenu = findViewById(R.id.profile_dropdown);
//
//        profileContainer.setOnClickListener(v -> {
//            boolean isVisible = dropdownMenu.getVisibility() == View.VISIBLE;
//
//            if (isVisible) {
//                hideDropdown();
//            } else {
//                dropdownOverlay.setVisibility(View.VISIBLE);
//                dropdownMenu.setVisibility(View.VISIBLE);
//            }
//        });
//
//        dropdownOverlay.setOnClickListener(v -> hideDropdown());
//
//        //Handle dropdown item clicks
//        findViewById(R.id.my_account).setOnClickListener(v -> {
//            Log.d("Dropdown", "My Account clicked");
//            hideDropdown();
//        });
//
////        findViewById(R.id.view_profile).setOnClickListener(v -> {
////            Log.d("Dropdown", "View Profile clicked");
////            hideDropdown();
////        });
//    }//End of showProfileDropDown method

//    private void hideDropdown() {
//        dropdownOverlay.setVisibility(View.GONE);
//        dropdownMenu.setVisibility(View.GONE);
//    }

    private void restoreSession(){
        SessionManager sessionManager = new SessionManager(this);
        if(sessionManager.isLoggedIn()){
            sharedVM.setIsUserLoggedIn(true);
            sharedVM.setCurrentEmail(sessionManager.getEmail());
            sharedVM.setCurrentUserId(sessionManager.getUserId());
            Log.d("LOG_RESTORE_SESSION", "Session restored CALLED");
        }
    }//End of restoreSession method

}