package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.adapter.BaseFragmentAdapter;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.PrivateMessageLauncherFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.RewardLauncherFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.StereoscopicLauncherFragment;

import net.youmi.android.AdManager;

import java.util.ArrayList;
import java.util.List;

import cn.waps.AppConnect;

/**
 * Created by Administrator on 2016/10/10.
 * WelcomeActivity
 * First enter the App
 */

public class WelcomeActivity extends FragmentActivity {
    private ViewPager vPager;
    private List<Fragment> list = new ArrayList<>();
    private BaseFragmentAdapter adapter;

    private ImageView[] tips;
    private int currentSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        AdManager.getInstance(Appconfiguration.getInstance().getContext()).init("6a741d1cb4fa8d83", "f0b8ac4ca96cd1d9", true);//有米
        AppConnect.getInstance("0aa0ac95f187d3b59db4bba59b48e19e", "goapk", this);//万普
        final Appconfiguration appConfig = Appconfiguration.getInstance();
        appConfig.setIsFirstStartApp(false);
        if (appConfig.getActivitySet().size() > 0) {
            finish();
            return;
        } else {
            //初始化点点点控件
            ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
            tips = new ImageView[3];
            for (int i = 0; i < tips.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(10, 10));
                if (i == 0) {
                    imageView.setBackgroundResource(R.drawable.page_indicator_focused);
                } else {
                    imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
                tips[i] = imageView;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                layoutParams.leftMargin = 20;//设置点点点view的左边距
                layoutParams.rightMargin = 20;//设置点点点view的右边距
                group.addView(imageView, layoutParams);
            }
        }
        //获取自定义viewpager 然后设置背景图片
        vPager = (ViewPager) findViewById(R.id.viewpager_launcher);

        /**
         * 初始化三个fragment  并且添加到list中
         */
        RewardLauncherFragment rewardFragment = new RewardLauncherFragment();
        PrivateMessageLauncherFragment privateFragment = new PrivateMessageLauncherFragment();
        StereoscopicLauncherFragment stereoscopicFragment = new StereoscopicLauncherFragment();
        list.add(rewardFragment);
        list.add(privateFragment);
        list.add(stereoscopicFragment);

        adapter = new BaseFragmentAdapter(getSupportFragmentManager(), list);
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(2);
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(changeListener);
    }

    /**
     * 监听viewpager的移动
     */
    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            setImageBackground(index);//改变点点点的切换效果
            currentSelect = index;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * 改变点点点的切换效果
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
}