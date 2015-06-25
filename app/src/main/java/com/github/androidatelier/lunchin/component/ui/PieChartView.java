package com.github.androidatelier.lunchin.component.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.github.androidatelier.lunchin.R;

public class PieChartView extends View {
    private static final String TAG = "PieChartView";
    private Paint paint;
    private Paint bgpaint;
    private RectF rect;
    private double percentage;

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();

        paint = new Paint();
        paint.setColor(res.getColor(R.color.scheme_tab_primary));
        paint.setTextSize(40);
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);
        bgpaint = new Paint();

        bgpaint.setColor(res.getColor(R.color.scheme_tab_light));
        bgpaint.setAntiAlias(false);
        bgpaint.setStyle(Paint.Style.FILL);

        rect = new RectF();
        percentage = 55.96f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Log.v(TAG, "PieChartView onDraw");

        int left = 0;
        int height = canvas.getHeight();
        int top = 0;
        rect.set(left, top, left + height, top + height);

        //drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
        //draw background circle

        canvas.drawArc(rect, 0, 360, true, bgpaint);
        if (percentage != 0) {
            // draw the percentage arc
            int sweepAngle = (int) (360 * (percentage / 100.f));
            int startAngle = 270 - sweepAngle / 2;
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
        }

        canvas.drawRect(left + height + 10, top + 10, left + height + 10 + 50, top + 10 + 50, paint);
        canvas.drawText("Savings", left + height + 10 + 50 + 10, top + 10 + 40, paint);
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
        // forces a redraw
        invalidate();
    }
}