package com.github.androidatelier.lunchin.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.drawable.AnimationDrawable;

import com.github.androidatelier.lunchin.R;

/**
 * Created by brenda on 6/17/15.
 */
public class MyGoalFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_my_goal,container,false);

        // Type casting the Image View
        final ImageView animationView=(ImageView)v.findViewById(R.id.raining_money);

        // Do not show animated images
        animationView.setVisibility(ImageView.INVISIBLE);

        // Setting animation_list.xml as the background of the image view
       // animationView.setBackgroundResource(R.drawable.animation_list);

        final AnimationDrawable rainingMoneyAnimation=(AnimationDrawable)animationView.getBackground();

        Button showButton = (Button) v.findViewById(R.id.visible_button);
        // Listener on Button sendButton
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the animated images visible
                animationView.setVisibility(ImageView.VISIBLE);
            }
        });
        Button hideButton = (Button) v.findViewById(R.id.invisible_button);
        // Listener on Button sendButton
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the animated images visible
                animationView.setVisibility(ImageView.INVISIBLE);
            }
        });

        // Animation On Button
        Button onButton = (Button) v.findViewById(R.id.on_button);
        // Listener on Button sendButton
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rainingMoneyAnimation.isRunning())
                    rainingMoneyAnimation.start();
            }
        });
        // Animation Off Button
        Button offButton = (Button) v.findViewById(R.id.off_button);
        // Listener on Button sendButton
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rainingMoneyAnimation.isRunning())
                    rainingMoneyAnimation.stop();
            }
        });

        return v;
    }
}
