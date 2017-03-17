package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MoreNewsItemFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.view.TabPageIndicator;

/**
 * Created by Administrator on 2016/12/8.
 */

public class MoreNewsActivity extends FragmentActivity {
    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[]{"社会", "国内", "国际",
            "娱乐", "体育", "军事", "科技", "财经", "时尚"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_news);
        initView();
    }

    private void initView() {
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new MoreNewsItemFragment();
            Bundle args = new Bundle();
            args.putString("type", setType(TITLE[position]));
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }

    private String setType(String Tile) {
        String type = null;
        if (Tile.equals("头条")) {
            type = "top";
        }

        if (Tile.equals("社会")) {
            type = "shehui";
        }
        if (Tile.equals("国内")) {
            type = "guonei";
        }
        if (Tile.equals("国际")) {
            type = "guoji";
        }
        if (Tile.equals("娱乐")) {
            type = "yule";
        }

        if (Tile.equals("体育")) {
            type = "tiyu";
        }
        if (Tile.equals("军事")) {
            type = "junshi";
        }

        if (Tile.equals("科技")) {
            type = "keji";
        }
        if (Tile.equals("财经")) {
            type = "caijing";
        }
        if (Tile.equals("时尚")) {
            type = "shishang";
        }
        return type;
    }
}
