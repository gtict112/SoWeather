package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.example.administrator.soweather.R;


/**
 * 打赏页面
 *
 * @author ansen
 * @create time 2015-08-07
 */
public class RewardLauncherFragment extends LauncherBaseFragment {
    private boolean started;//是否开启动画
    private ImageView iv_receive_1, iv_receive_2, iv_receive_3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_reward_launcher, null);
        iv_receive_1 =(ImageView)rooView.findViewById(R.id.iv_receive_1);
        iv_receive_2 =(ImageView)rooView.findViewById(R.id.iv_receive_2);
        iv_receive_3 =(ImageView)rooView.findViewById(R.id.iv_receive_3);
        //获取硬币的高度
        startAnimation();
        return rooView;
    }

    public void startAnimation() {
        started = true;
        //初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0.1f, 100.0f, 0.1f, 100.0f);
        //初始化 Alpha动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        //动画集
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);
        set.addAnimation(alphaAnimation);
        //设置动画时间 (作用到每个动画)
        set.setDuration(2000);
        iv_receive_1.startAnimation(set);
        iv_receive_2.startAnimation(set);
        iv_receive_3.startAnimation(set);
    }

    @Override
    public void stopAnimation() {
        started = false;//结束动画时标示符设置为false
        iv_receive_1.clearAnimation();//清空view上的动画
        iv_receive_2.clearAnimation();//清空view上的动画
        iv_receive_3.clearAnimation();//清空view上的动画
    }
}
