package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.ui.base.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MoreInfoItemFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.DateToWeek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MoreInfoActivity extends BaseActivity {
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();//未来几天预报数据
    private ViewPager viewPager;
    private String time;//点击Item传进来的时间,没有就默认加载第一条
    /**
     * Tab标题
     */
    private String[] TITLE = new String[7];
    private String[] TIMES = new String[7];

    private String qlty;
    private Toolbar toolbar;

    private String city;
    private String county;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_info;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setDisplayHomeAsUpEnabled(true);
        initDate();
        initView();
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (!county.equals("")) {
            toolbar.setTitle(county);
        } else {
            toolbar.setTitle(city);
        }
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager, mDailyforecast);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        setIndicator();
    }

    private void setupViewPager(ViewPager viewPager, List<Dailyforecast> mDailyforecast) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < TITLE.length; i++) {
            Fragment fragment = new MoreInfoItemFragment();
            Bundle args = new Bundle();
            args.putString("today", TIMES[0]);
            args.putString("tomorrow", TIMES[1]);
            args.putString("type", TIMES[i]);
            args.putSerializable("dailyforecast", mDailyforecast.get(i));
            args.putString("qlty", qlty);
            args.putString("week", TITLE[i]);
            fragment.setArguments(args);
            adapter.addFrag(fragment, TITLE[i]);
        }
        viewPager.setAdapter(adapter);
    }


    private void setIndicator() {
        int j = 0;
        for (int i = 0; i < TITLE.length; i++) {
            if (DateToWeek.getWeek(time).equals(TITLE[i])) {
                j = i;
            }
        }
        viewPager.setCurrentItem(j);
    }


    private void initDate() {
        Intent intent = getIntent();
        if (intent != null) {
            qlty = intent.getStringExtra("qlty");
            mDailyforecast = (List<Dailyforecast>) intent.getSerializableExtra("date");
            for (int i = 0; i < mDailyforecast.size(); i++) {
                TITLE[i] = DateToWeek.getWeek(mDailyforecast.get(i).date);
                TIMES[i] = mDailyforecast.get(i).date;
            }
            time = intent.getStringExtra("time");
            city = intent.getStringExtra("city");
            county = intent.getStringExtra("county");
        }
    }


    /**
     * ViewPager适配器
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
}
