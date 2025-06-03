package com.example.pomodoro_timer.utils.binding_adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("app:srcCompatThemeAttr")
    public static void setSrcCompatThemeAttr(ImageView imageView, int attrResId) {
        if (attrResId == 0) {
            // imageView.setImageDrawable(null); // Or set a default
            Log.w("BindingAdapters", "srcCompatThemeAttr received 0 attrResId.");
            return;
        }
        Context context = imageView.getContext();
        TypedValue typedValue = new TypedValue();
        boolean resolved = context.getTheme().resolveAttribute(attrResId, typedValue, true);

        if (resolved && typedValue.resourceId != 0) {
            try {
                imageView.setImageResource(typedValue.resourceId);
                Log.d("BindingAdapters", "Set image resource " + context.getResources().getResourceEntryName(typedValue.resourceId) + " from attribute " + context.getResources().getResourceEntryName(attrResId));
            } catch (Resources.NotFoundException e) {
                Log.e("BindingAdapters", "Drawable resource not found (" + typedValue.resourceId + ") for attribute: "
                        + context.getResources().getResourceName(attrResId), e);
                // imageView.setImageResource(R.drawable.default_icon_fallback);
            }
        } else {
            Log.w("BindingAdapters", "Attribute could not be resolved to a resourceId: "
                    + context.getResources().getResourceName(attrResId) + " (Resolved: " + resolved + ", TypedValue resid: " + typedValue.resourceId + ")");
            // imageView.setImageResource(R.drawable.default_icon_fallback);
        }
    }//setSrcCompatThemeAttr

    @BindingAdapter("app:formattedEmail")
    public static void setFormattedEmail(TextView textView, String email) {
        if (email == null || email.isEmpty()) {
            textView.setText(""); // Handle null or empty email
            return;
        }

        int atIndex = email.indexOf('@');
        if (atIndex != -1) {
            // Get the part before '@'
            String localPart = email.substring(0, atIndex);

            // Option 1: Show the full local part (everything before @)
            textView.setText(localPart);

            // Option 2: Show only the first few characters of the local part (e.g., first 5)
            // int charsToShow = 5;
            // if (localPart.length() > charsToShow) {
            //     textView.setText(localPart.substring(0, charsToShow) + "...");
            // } else {
            //     textView.setText(localPart);
            // }

            // Option 3: If you specifically want to remove "@gmail.com" and show what's before it
            // if (email.toLowerCase().endsWith("@gmail.com")) {
            //     textView.setText(localPart); // localPart is already everything before @
            // } else {
            //     // For other domains, you might want to show the full email or a different format
            //     textView.setText(email); // Or keep localPart as above
            // }

        } else {
            // No '@' symbol, display as is or handle as an invalid email format
            textView.setText(email);
        }
    }

}
