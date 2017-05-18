package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.TimeDialogFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.TodayDetailFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.view.UnderlinePageIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class TipActivity extends BaseActivity implements View.OnClickListener {
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private List<Fragment> fragmentsList = new ArrayList<>();
    private String city;
    private String cityid;
    private TextView topTv;
    private ImageView topButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog_tip);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        if (intent != null) {
            mHourlyforecast = (List<Hourlyforecast>) intent.getSerializableExtra("date");
            city = intent.getStringExtra("city");
            cityid = intent.getStringExtra("cityid");
            topTv.setText(city);
        }
        ViewPager pager = (ViewPager) findViewById(R.id.vPager);
        TimeDialogFragment mTimeDialogFragment = new TimeDialogFragment();
        if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("date", (Serializable) mHourlyforecast);
            mTimeDialogFragment.setArguments(bundle);
        }
        fragmentsList.add(mTimeDialogFragment);
        TodayDetailFragment mTodayDetailFragment = new TodayDetailFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("city", city);
        bundle1.putString("cityid", cityid);
        mTodayDetailFragment.setArguments(bundle1);
        fragmentsList.add(mTodayDetailFragment);
        FragmentPagerAdapter adapter = new PagerAdapterFragment(getSupportFragmentManager(), fragmentsList);
        pager.setAdapter(adapter);
        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setSelectedColor(0xFFCC0000);
        indicator.setFadeDelay(2000);
        indicator.setFadeLength(2000);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
        }
    }


    public static class PagerAdapterFragment extends FragmentPagerAdapter {
        private List<Fragment> fragmentsList;

        public PagerAdapterFragment(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}