package com.example.pomodoro_timer.ui.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.pomodoro_timer.R;

public class TimerAnimationView extends View {

    //Fields
    private Paint progressRing;
    private Paint piePaint;
    private Paint backgroundPaint;
    private float sweepAngle = 0f;

    private ValueAnimator animator;

    //Initialize and constructor
    public TimerAnimationView(Context context, AttributeSet attrs){
        super(context, attrs);
        initializePaint();
    }

    private void initializePaint(){
        //The background of the animation
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setStyle(Paint.Style.FILL);

        //For the paint that goes along the ring
        piePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        piePaint.setColor(ContextCompat.getColor(getContext(), R.color.count_down_color));
        piePaint.setAlpha(20);
        piePaint.setStyle(Paint.Style.FILL);

        //The ring paint
        progressRing = new Paint(Paint.ANTI_ALIAS_FLAG);
        //progressRing.setColor(Color.RED);
        progressRing.setStyle(Paint.Style.STROKE);
        progressRing.setStrokeWidth(30);
        progressRing.setStrokeCap(Paint.Cap.BUTT);
    }//End of initializePaint method

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Gradient from top to bottom
        //Also changes by size/scalability

        int[] colors = new int[]{
                getColorFromAttr(getContext(), R.attr.timerDonutProgressColor),
                getColorFromAttr(getContext(), R.attr.timerDonutProgressColor2),
                getColorFromAttr(getContext(), R.attr.timerDonutProgressColor3)
        };

        float[] positionz = new float[]{ 0f, 5.5f, 1f};
        progressRing.setShader(new LinearGradient(
                0, 0, 0, h,
                colors,
                positionz,
                Shader.TileMode.CLAMP
        ));

    }//End of onSizeChanged method

    private int getColorFromAttr(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        // It's crucial to check the type.
        // It can be a color direct value (TYPE_INT_COLOR_*) or a reference to a color resource (TYPE_REFERENCE)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // It's a direct color value
            return typedValue.data;
        } else if (typedValue.type == TypedValue.TYPE_REFERENCE) {
            // It's a reference to a color resource (e.g., @color/some_color)
            // In this case, typedValue.resourceId will hold the ID (e.g., R.color.some_color)
            // and ContextCompat.getColor can resolve it.
            // However, resolveAttribute with 'resolveRefs = true' should have already resolved this.
            // If it's still a reference, it means the attribute itself points to a color resource.
            return ContextCompat.getColor(context, typedValue.resourceId);
        }
        // Fallback color if the attribute is not defined or not a color
        // You might want to throw an exception or return a default color
        Log.w("TimerAnimationView", "Warning: Attribute not found or not a color: " + context.getResources().getResourceName(attr));
        return Color.GRAY; // Default fallback color
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cX = getWidth() / 2;
        float cY = getHeight() / 2;
        //float radius = Math.min(cX, cY) / 2;
        float radius = Math.min(cX, cY) - 30;

        RectF oval = new RectF(cX - radius, cY - radius, cX + radius, cY + radius);
        canvas.drawCircle(cX, cY, radius, backgroundPaint);
        canvas.drawArc(oval, -90, sweepAngle, true, piePaint);
        canvas.drawArc(oval, -90, sweepAngle, false, progressRing);
        //Log.d("CHEK ONDRAW","ON DRAW METHOD CALLED!");
    }//End of onDraw method

    //This thang sets everything,
    //Kinda makes it look like automation
    public void setProgress(float angle){
        this.sweepAngle = angle;
        invalidate();
    }

}
