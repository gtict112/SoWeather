package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.LeftFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MainFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;
import com.example.administrator.soweather.com.example.administrator.soweather.view.SlidingMenu;

/**
 * Created by Administrator on 2016/10/10.
 * MainActivity
 */
public class MainActivity extends SlidingFragmentActivity implements
        View.OnClickListener {
    private ImageView topButton;
    private Fragment mContent;
    private TextView topTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSlidingMenu(savedInstanceState);
        if (mContent == null) {
            mContent = new MainFragment();
            switchConent(mContent, "首页");
        }
    }


    private void initView() {
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        topTextView = (TextView) findViewById(R.id.topTv);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LeftFragment()).commit();
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(null);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(0.0f);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchConent(Fragment fragment, String title) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
        topTextView.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                toggle();
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        final com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout confirmDialog = new DialogLogout(this, "确定要退出吗?", "退出", "取消");
//        confirmDialog.show();
//        confirmDialog.setClicklistener(new com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout.ClickListenerInterface() {
//            @Override
//            public void doConfirm() {
//                // TODO Auto-generated method stub
//                confirmDialog.dismiss();
//                //toUserHome(context);
//                AppManager.getAppManager().AppExit(this);
//            }
//
//            @Override
//            public void doCancel() {
//                // TODO Auto-generated method stub
//                confirmDialog.dismiss();
//            }
//        });
//    }
}