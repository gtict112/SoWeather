package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;

/**
 * 私信
 *
 * @author ansen
 */
public class PrivateMessageLauncherFragment extends LauncherBaseFragment {
    private ImageView welcome_2;
    private boolean started;//是否开启动画

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_private_message_launcher, null);
        welcome_2 = (ImageView)rooView.findViewById(R.id.welcome_2);
        return rooView;
    }

    public void stopAnimation() {
        //动画开启标示符设置成false
        started = false;
        Glide.with(getActivity()).load(R.mipmap.welcome_2).asGif().animate(R.anim.img_loading).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_loading_eholder)
                .into(welcome_2);
    }


    public void startAnimation() {
        started = true;
    }
}
