package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;

/**
 * Created by Administrator on 2016/10/10.
 */

public class LeftFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout mHome;//首页
    private RelativeLayout mLifeindex;//生活指数
    private RelativeLayout mLittlebear;//小笨熊客服
    private RelativeLayout mSetting;//设置
    private RelativeLayout mHelp;//帮助与反馈
    private RelativeLayout mLogout;//退出
    private TextView tmp;
    private TextView tmp_txt;
   private Appconfiguration config = Appconfiguration.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mean, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mHome = (RelativeLayout) view.findViewById(R.id.home);
        mLifeindex = (RelativeLayout) view.findViewById(R.id.life_index);
        mLittlebear = (RelativeLayout) view.findViewById(R.id.little_bear);
        mSetting = (RelativeLayout) view.findViewById(R.id.setting);
        mHelp = (RelativeLayout) view.findViewById(R.id.help);
        mLogout = (RelativeLayout) view.findViewById(R.id.logout);
        tmp =(TextView)view.findViewById(R.id.tmp);
        tmp_txt=(TextView)view.findViewById(R.id.tmp_txt);
        mHome.setOnClickListener(this);
        mLifeindex.setOnClickListener(this);
        mLittlebear.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mHelp.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment newContent = null;
        String title = null;
        switch (v.getId()) {
            case R.id.home:
                newContent = new MainFragment();
                title = "首页";
                break;
            case R.id.life_index:
                newContent = new LifeIndexFragment();
                title = "生活指数";
                break;
            case R.id.little_bear:
                title = "小笨熊";
                break;
            case R.id.setting:
                title = "系统设置";
                break;
            case R.id.help:
                title = "帮助与反馈";
                break;
            case R.id.logout:
                DialogLogout dialog = new DialogLogout(getActivity(), new DialogLogout.OnCancleDialogListener() {
                    @Override
                    public void cancle() {
                        Toast.makeText(getActivity(),"您真的太赞了",Toast.LENGTH_LONG).show();
                    }
                }, new DialogLogout.onConFirmDialogListener() {
                    @Override
                    public void confirm() {
                      getActivity().finish();
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
        if (newContent != null) {
            switchFragment(newContent, title);
        }
    }

    private void switchFragment(Fragment fragment, String title) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity) {
            MainActivity fca = (MainActivity) getActivity();
            fca.switchConent(fragment, title);
        }
    }
}
