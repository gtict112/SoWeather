package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MoreInfoItemFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.DateToWeek;
import com.example.administrator.soweather.com.example.administrator.soweather.view.TabPageIndicator;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MoreInfoActivity extends BaseActivity implements View.OnClickListener {
    private String city;
    private String cityid;
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();//未来几天预报数据
    private ViewPager pager;
    private TabPageIndicator indicator;
    private String time;//点击Item传进来的时间,没有就默认加载第一条
    /**
     * Tab标题
     */
    private String[] TITLE = new String[7];
    private String[] TIMES = new String[7];

    private ImageView topButton;
    private TextView topTv;
    private String qlty;

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
        initDate();
        initView();
    }


    private void initView() {
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        topButton.setOnClickListener(this);
        topTv.setText(city);
        setIndicator();
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


    private void setIndicator() {
        FragmentPagerAdapter adapter = new MoreInfoActivity.TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
        int j = 0;
        for (int i = 0; i < TITLE.length; i++) {
            if (DateToWeek.getWeek(time).equals(TITLE[i])) {
                j = i;
            }
        }
        indicator.setCurrentItem(j);
    }


    private void initDate() {
        Intent intent = getIntent();
        if (intent != null) {
            city = intent.getStringExtra("city");
            cityid = intent.getStringExtra("cityid");
            qlty = intent.getStringExtra("qlty");
            mDailyforecast = (List<Dailyforecast>) intent.getSerializableExtra("date");
            for (int i = 0; i < mDailyforecast.size(); i++) {
                TITLE[i] = DateToWeek.getWeek(mDailyforecast.get(i).date);
                TIMES[i] = mDailyforecast.get(i).date;
            }
            time = intent.getStringExtra("time");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                break;
        }
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
            Fragment fragment = new MoreInfoItemFragment();
            Bundle args = new Bundle();
            args.putString("today", TIMES[0]);
            args.putString("tomorrow", TIMES[1]);
            args.putString("type", TIMES[position]);
            args.putSerializable("dailyforecast", mDailyforecast.get(position));
            args.putString("qlty", qlty);
            args.putString("week", TITLE[position]);
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
