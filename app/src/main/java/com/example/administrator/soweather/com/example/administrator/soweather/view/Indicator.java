package com.example.administrator.soweather.com.example.administrator.soweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.administrator.soweather.R;

public class Indicator extends LinearLayout {
	private int mNormalIcId;
	private int mSelectedIcId;
	private SparseArray<ImageView> mIcHost;
	private int mLastCheckedItem = 0;
	public Indicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public Indicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Indicator(Context context) {
		this(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		
		setOrientation(LinearLayout.HORIZONTAL);
		mNormalIcId = R.mipmap.ic_indicator_normal;
		mSelectedIcId = R.mipmap.ic_indicator_selected;
		mIcHost = new SparseArray<ImageView>();
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Indicator);
		int num = a.getInt(R.styleable.Indicator_ItemNum, 3); 
		mLastCheckedItem = a.getInt(R.styleable.Indicator_CheckedItem, 0); 
		a.recycle();
		setIndicatorNum(num);
		setCheckItem(mLastCheckedItem);
	}

	public void setIndicatorNum(int indicatorNum) {
		initIndicator(indicatorNum);
		setCheckItem(mLastCheckedItem);
	}

	private void initIndicator(int indicatorNum) {
		removeAllViews();
		int dp = dptopx(5);
		for (int i = 0; i < indicatorNum; i++) {
			ImageView ic = new ImageView(getContext());
			ic.setImageResource(mNormalIcId);
			ic.setPadding(dp,dp,dp, dp);
			addView(ic);
			mIcHost.put(i, ic);
		}

	}

	public void setCheckItem(int position) {
		ImageView imageView = mIcHost.get(mLastCheckedItem);
		if (imageView != null)
			imageView.setImageResource(mNormalIcId);
		imageView = mIcHost.get(position);
		if (imageView != null)
			imageView.setImageResource(mSelectedIcId);
		mLastCheckedItem = position;
	}
	private int dptopx(int dp){
		return (int) (getContext().getResources().getDisplayMetrics().density*dp+0.5f);
		
	}
}
