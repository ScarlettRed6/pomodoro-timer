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

public class EditTaskFragment extends Fragment {

    public EditTaskFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 48, 48, 48);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.WHITE);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        Button editDescription = new Button(getContext());
        editDescription.setText("Edit Description");
        layout.addView(editDescription, getDefaultLayoutParams());

        Button editPriority = new Button(getContext());
        editPriority.setText("Edit Priority Level");
        layout.addView(editPriority, getDefaultLayoutParams());

        Button editPomodoros = new Button(getContext());
        editPomodoros.setText("Edit Number of Pomodoro's");
        layout.addView(editPomodoros, getDefaultLayoutParams());

        return layout;
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 32);
        return params;
    }
}




