package com.github.androidatelier.lunchin.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;

public class GoalView extends View {
        public GoalView(Context cxt) {
        super(cxt);
        setMinimumHeight(500);
        setMinimumWidth(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: replace hardcoded percentage goalPercent1
        float goalPercent1 = 50.0f;
        float goalPercent2 = 30.0f;
        String goalString1 = "$2000.0";
        String goalString2 = "Goal 2";
        String goalCurrent1 = "$1000.0";
        String goalCurrent2 = "To Date";

        canvas.drawColor(Color.WHITE);

        int w = canvas.getWidth();
        int h = canvas.getHeight();

        Paint p = new Paint();
        int barWidth = 150;
        p.setTextSize(50);
        float lineWidth = 4.0f;
        p.setStrokeWidth(lineWidth);

        p.setAntiAlias(true);

        int lLeft = 50;
        int top = 50;
        int lRight = lLeft + barWidth;
        int bottom = h - 200;

        int divisor = (int)Math.round(100.0f / goalPercent1);
        int progress = (bottom - top) - ((bottom - top) / divisor);

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLUE);
        canvas.drawRect(lLeft, top + progress, lRight, bottom, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        canvas.drawRect(lLeft, top, lRight, bottom, p);
        //canvas.drawText("$2,000.00", lRight + 5, top + 50, p);
        canvas.drawText(goalString1, lRight + 5, top + 50, p);
        canvas.drawText(goalCurrent1, lRight + 5, top + progress + 50, p);

        int rLeft = (w/2) + 50;
        int rRight = rLeft + barWidth;

        divisor = (int)Math.round(100.0f / goalPercent2);
        progress = (bottom - top) - ((bottom - top) / divisor);

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.RED);
        canvas.drawRect(rLeft, top + progress, rRight, bottom, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        canvas.drawRect(rLeft, top, rRight, bottom, p);
        canvas.drawText(goalString2, rRight + 5, top + 50, p);
        canvas.drawText(goalCurrent2, rRight + 5, top + progress + 50, p);
    }
}