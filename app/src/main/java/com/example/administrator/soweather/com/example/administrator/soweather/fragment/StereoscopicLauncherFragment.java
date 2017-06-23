package com.example.administrator.soweather.com.example.administrator.soweather.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;

/**
 * 最后一个
 *
 * @author apple
 */
public class StereoscopicLauncherFragment extends LauncherBaseFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_stereoscopic_launcher, null);
        Button imgView_immediate_experience = (Button) rooView.findViewById(R.id.imgView_immediate_experience);
        imgView_immediate_experience.setOnClickListener(this);
        return rooView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_immediate_experience:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out);
                getActivity().finish();
                getActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out);
                break;
        }
    }

    @Override
    public void startAnimation() {
    }

    @Override
    public void stopAnimation() {

    }
}
