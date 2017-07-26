package com.lhlSo.soweather.module.weixinselect;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.lhlSo.soweather.R;
import com.lhlSo.soweather.module.activity.MainActivity;
import com.lhlSo.soweather.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/18.
 */

public class WeixinSelectFragment extends BaseFragment {
    private String[] TITLE = {"社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    private String[] IDS = {"shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weixin_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weixinselect;
    }

    @Override
    protected void initViews() {
        mToolbar.setTitle("微信精选");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        initView();
    }

    @Override
    protected void lazyFetchData() {

    }


    private void initView() {
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(viewPager.getAdapter().getCount());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < TITLE.length; i++) {
            Fragment fragment = new WeixinSelectCategoryFragment();
            Bundle args = new Bundle();
            args.putString("title", IDS[i]);
            fragment.setArguments(args);
            adapter.addFrag(fragment, TITLE[i]);
        }
        viewPager.setAdapter(adapter);
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
