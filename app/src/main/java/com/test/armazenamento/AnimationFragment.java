package com.test.armazenamento;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationFragment extends Fragment {

    public AnimationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_animation, container, false);
        FloatingActionButton animationButton = (FloatingActionButton) view.findViewById(R.id.animationButton);
        animationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View star = view.findViewById(R.id.starImageView);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animation_star);
                star.startAnimation(animation);
            }
        });
        return view;
    }

}
