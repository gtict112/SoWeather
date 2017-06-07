package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.view.TabPageIndicator;

import cn.feng.skin.manager.base.BaseSkinFragment;

/**
 * Created by Administrator on 2017/6/6.
 */

public class BeautyFragment extends BaseSkinFragment {
    /**
     * Tab标题
     */
    private String[] TITLE = {"性感美女", "韩日美女", "丝袜美腿", "美女照片", "美女写真", "清纯美女", "性感车模"};
    private String[] IDS = {"1", "2", "3", "4", "5", "6", "7"};
    private ViewPager pager;
    private TabPageIndicator indicator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beauty, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        pager = (ViewPager) view.findViewById(R.id.beauty_pager);
        indicator = (TabPageIndicator) view.findViewById(R.id.beauty_indicator);
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getChildFragmentManager());
        pager.setOffscreenPageLimit(7);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        indicator.setCurrentItem(0);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
            Fragment fragment = new BeautyItemFragment();
            Bundle args = new Bundle();
            args.putString("id", IDS[position]);
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
}
