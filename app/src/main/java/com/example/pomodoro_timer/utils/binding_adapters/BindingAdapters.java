package com.example.pomodoro_timer.utils.binding_adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

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
    }

}
