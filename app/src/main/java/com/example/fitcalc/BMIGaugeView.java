package com.example.fitcalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class BMIGaugeView extends View {
    private float bmi=0;


    public BMIGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences sharedPreferences = context.getSharedPreferences("FitCalcPrefs", Context.MODE_PRIVATE);
        // Obnovenie posledného BMI pri štarte aplikácie
        bmi = sharedPreferences.getFloat("lastBMI", 0.0f);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

// minimum of width and height, ale obmedzené na minimálnu hodnotu
        int radius = Math.max(100, Math.min(availableWidth, availableHeight) / 2 - 60);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(80);

        RectF oval = new RectF(availableWidth / 2f - radius, availableHeight / 2f - radius,
                availableWidth / 2f + radius, availableHeight / 2f + radius);

        float [] treshholds = {16,17, 18.5f, 25, 30, 35, 40, 45};
        int []colors ={
                Color.parseColor("#FF7043"), // very underweight
                Color.parseColor("#FFEB3B"), // underweight
                Color.parseColor("#FFF176"), // mild underweight
                Color.parseColor("#66BB6A"), // normal
                Color.parseColor("#FFEB3B"), // overweight
                Color.parseColor("#EF9A9A"), // obezita 1
                Color.parseColor("#E57373"), // obezita 2
                Color.parseColor("#D32F2F") // obezita 3
        };

        float startAngle = 180;
        for (int i=0;i<treshholds.length-1;i++){
            float sweep= (treshholds[i+1] - treshholds[i]) / 45f * 180f; // 10 je faktor pre prevod BMI na uhol
            paint.setColor(colors[i]);
            canvas.drawArc(oval, startAngle, sweep, false, paint);
            startAngle += sweep;
        }

        float minBMI = 16;
        float maxBMI = 45;
        float clampedBMI = Math.max(minBMI, Math.min(bmi, maxBMI));

        float angle = (clampedBMI - minBMI) / (maxBMI - minBMI) * 180f; // Prevod BMI na uhol
        float angleRad = (float) Math.toRadians(angle + 180); // Prevod na radiány a posun o 180 stupňov

        float centerX = availableWidth /2f;
        float centerY = availableHeight / 2f;

        float endX = (float)(centerX + (radius - 60) * Math.cos(angleRad));
        float endY = (float)(centerY + (radius - 60) * Math.sin(angleRad));

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(centerX, centerY, endX, endY, paint);
        canvas.drawCircle(centerX, centerY, 12, paint);

        paint.setTextSize(50);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("BMI = " + String.format("%.1f", bmi), centerX, centerY + 200, paint);

    }
}
