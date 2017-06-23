package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;


/**
 *
 * @author ansen
 * @create time 2015-08-07
 */
public class RewardLauncherFragment extends LauncherBaseFragment {
    private boolean started;//是否开启动画
    private ImageView welcome_1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_reward_launcher, null);
        welcome_1 = (ImageView) rooView.findViewById(R.id.welcome_1);
        startAnimation();
        return rooView;
    }

    public void startAnimation() {
        started = true;
    }

    @Override
    public void stopAnimation() {
        started = false;//结束动画时标示符设置为false
    }
}
