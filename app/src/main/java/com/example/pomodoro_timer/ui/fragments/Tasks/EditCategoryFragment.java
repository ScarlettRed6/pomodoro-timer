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

public class EditCategoryFragment extends Fragment {

    public EditCategoryFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create root layout
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 48, 48, 48);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.WHITE);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Create buttons
        Button editTitle = new Button(getContext());
        editTitle.setText("Edit Title");
        layout.addView(editTitle, getDefaultLayoutParams());

        Button editDescription = new Button(getContext());
        editDescription.setText("Edit Description");
        layout.addView(editDescription, getDefaultLayoutParams());

        Button editIcon = new Button(getContext());
        editIcon.setText("Edit Category Icon (Optional)");
        layout.addView(editIcon, getDefaultLayoutParams());

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



