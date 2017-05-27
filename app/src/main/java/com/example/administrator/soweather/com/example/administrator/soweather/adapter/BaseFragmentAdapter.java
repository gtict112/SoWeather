package com.example.administrator.soweather.com.example.administrator.soweather.adapter;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.soweather.com.example.administrator.soweather.fragment.LauncherBaseFragment;

/**
 * Viewpager适配器
 * @author apple
 *
 */
public class BaseFragmentAdapter extends FragmentStatePagerAdapter {
	private List<LauncherBaseFragment>list;
	public BaseFragmentAdapter(FragmentManager fm, List<LauncherBaseFragment> list) {
		super(fm);
		this.list = list;
	}

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
