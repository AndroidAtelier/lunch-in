package com.androidatelier.lunchin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.PluralsRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidatelier.lunchin.R;

public class StatsRow extends TableRow {
    private final TextView mNumber;
    private final TextView mUnit;
    private final TextView mTitle;
    private final LinearLayout mBackground;

    public StatsRow(Context context) {
        this(context, null);
    }

    public StatsRow(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.stats_row, this);
        mNumber = (TextView) findViewById(R.id.stats_row_number);
        mUnit = (TextView) findViewById(R.id.stats_row_unit);
        mTitle = (TextView) findViewById(R.id.stats_row_title);
        mBackground = (LinearLayout) findViewById(R.id.stats_number_bg);

        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.title
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            String title = array.getString(0);
            mTitle.setText(title);
            array.recycle();
        }
    }

    public void setNumber(int number, @PluralsRes int unitResId) {
        mNumber.setText(String.valueOf(number));
        mUnit.setText(getResources().getQuantityString(unitResId, number));
    }

    public void setNumberBackground(@DrawableRes int intColor) {
        mBackground.setBackground(getResources().getDrawable(intColor));
    }
}
