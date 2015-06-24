package com.github.androidatelier.lunchin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.androidatelier.database.LunchInApi;
import com.github.androidatelier.lunchin.R;
import com.github.androidatelier.lunchin.model.SettingsAccess;
import com.github.androidatelier.lunchin.util.Formatter;

public class MyGoalFragment extends Fragment implements Updateable {
    private static final String TAG = "MyGoalFragment";

    private SettingsAccess mSettingsAccess;
    private LunchInApi mLunchInApi;

    private TextView mGoalProgress;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_my_goal,container,false);
        mGoalProgress = (TextView) v.findViewById(R.id.fragment_my_goal_progress_gained);

        mSettingsAccess = new SettingsAccess(getActivity());
        mLunchInApi = new LunchInApi(getActivity());

        initializeViews(v);
        initializeGoalAchievedAnimation(v);

        update();

        return v;
    }

    private void initializeViews(View v) {
        TextView goalName = (TextView) v.findViewById(R.id.fragment_my_goal_name);
        goalName.setText(mSettingsAccess.getSavingsGoalName());

        TextView goalValue = (TextView) v.findViewById(R.id.fragment_my_goal_value);
        goalValue.setText(Formatter.formatIntToCurrencyUSD(mSettingsAccess.getSavingsGoalValue()));

        imageView = (ImageView) v.findViewById(R.id.dollarView);
    }

    private void initializeGoalAchievedAnimation(View v) {
        // animation type selection buttons
        // TODO: demo for selecting which animation to use
        Button zoomButton = (Button) v.findViewById(R.id.button1);
        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom(v);
            }
        });

        Button clockwiseButton = (Button) v.findViewById(R.id.button2);
        clockwiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockwise(v);
            }
        });

        Button fadeButton = (Button) v.findViewById(R.id.button3);
        fadeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fade(v);
            }
        });

        Button blinkButton = (Button) v.findViewById(R.id.button4);
        blinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blink(v);
            }
        });


        final Button scaleButton = (Button) v.findViewById(R.id.button5);
        // Listener on Button sendButton
        scaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleAnimateSelectImageView(imageView);
            }
        });
    }

    @Override
    public void update() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        double averageLunchCost = mSettingsAccess.getAverageLunchCost();
        int goal = mSettingsAccess.getSavingsGoalValue();
        int numLunchIns = mLunchInApi.getNumberOfLunchIns();

        mGoalProgress.setText(getString(R.string.goal_progress,
                Formatter.formatDoubleToCurrencyUSD(numLunchIns * averageLunchCost),
                Formatter.formatIntToCurrencyUSD(goal)));
    }

    // animation
    public void zoom(View view) {
        //ImageView image = (ImageView)view.findViewById(R.id.dollarView);
        Animation animation = AnimationUtils.loadAnimation(view.getContext().getApplicationContext(), R.anim.zoom);
        imageView.startAnimation(animation);
    }

    public void clockwise(View view) {
        //ImageView image = (ImageView)view.findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(view.getContext().getApplicationContext(), R.anim.clockwise);
        imageView.startAnimation(animation1);
    }

    public void fade(View view) {
        //ImageView image = (ImageView)view.findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(view.getContext().getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation1);
    }

    public void blink(View view) {
        //ImageView image = (ImageView)view.findViewById(R.id.imageView);
        Animation animation1 = AnimationUtils.loadAnimation(view.getContext().getApplicationContext(), R.anim.blink);
        imageView.startAnimation(animation1);

    }

    private void scaleAnimateSelectImageView(ImageView imageView) {
        Log.v(TAG, "animateSelectButton");
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.2f, 0.1f, 1.2f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setRepeatMode(Animation.RESTART);
        scaleAnimation.setInterpolator(new OvershootInterpolator(6f));
        imageView.startAnimation(scaleAnimation);
    }
}