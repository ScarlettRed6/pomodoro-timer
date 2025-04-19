package com.example.pomodoro_timer.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.viewmodels.SharedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Put context here
        setupNavigation();
        showFab();

    }

    private void setupNavigation() {
        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHost.getNavController();

        //Setup bottom nav control
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }//End of setupNavigation method

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

    }//End of showFab method

}