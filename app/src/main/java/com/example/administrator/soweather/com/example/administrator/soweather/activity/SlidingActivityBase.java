package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.example.administrator.soweather.com.example.administrator.soweather.view.SlidingMenu;

public interface SlidingActivityBase {
	
	public void setBehindContentView(View view, LayoutParams layoutParams);
	public void setBehindContentView(View view);

	public void setBehindContentView(int layoutResID);

	public SlidingMenu getSlidingMenu();
	public void toggle();
	
	public void showContent();
	
	public void showMenu();

	public void showSecondaryMenu();
	public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled);
	
}
