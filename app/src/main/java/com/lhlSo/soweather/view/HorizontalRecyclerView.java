package com.lhlSo.soweather.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * Created by Administrator on 2016/11/22.
 */

public class HorizontalRecyclerView extends RecyclerView {
    public HorizontalRecyclerView(Context context) {
        super(context);
    }

    public HorizontalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
            case MotionEvent.ACTION_DOWN: setParentScrollAble(false);

            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 当手指松开时，让父控件重新获取onTouch权限
                setParentScrollAble(true);
                break;

        }
        return super.onInterceptTouchEvent(ev);

    }

    // 设置父控件是否可以获取到触摸处理权限
    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }

}