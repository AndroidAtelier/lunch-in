package com.github.androidatelier.lunchin.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.R;
import android.R.color;
import android.graphics.Color;
import android.util.Log;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;
import android.graphics.RectF;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class PieChartView extends View {
    private static final String TAG = "PieChartView";
    private Paint paint;
    private Paint bgpaint;
    private RectF rect;
    private float percentage;

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Log.v(TAG, "PieChartView init");
        paint = new Paint();

        paint.setColor(0xFF5086ED);
        //paint.setColor(getContext().getResources().getColor(R.color.cerulean));
        //paint.setColor(this.getResources().getColor(R.color.cerulean));

        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);
        bgpaint = new Paint();

        bgpaint.setColor(0xFF8D47ED);
        //bgpaint.setColor(getResources().getColor(R.color.scheme_tab_accent));

        bgpaint.setAntiAlias(false);
        bgpaint.setStyle(Paint.Style.FILL);
        rect = new RectF();
        percentage = 55.96f;
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.v(TAG, "PieChartView onDraw");

        int left = 100;
        int width = 400;
        int top = 0;
        rect.set(left, top, left + width, top + width);

        //drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
        //draw background circle anyway
        canvas.drawArc(rect, -90, 360, true, bgpaint);
        if (percentage != 0) {
            int sweepAngle = (int) (360 * (percentage / 100.f));
            int startAngle = 270 - sweepAngle / 2;
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
        }
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
        invalidate();
    }
}