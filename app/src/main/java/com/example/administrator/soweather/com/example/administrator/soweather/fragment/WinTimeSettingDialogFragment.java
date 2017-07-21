package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;

/**
 * Created by Administrator on 2017/7/20.
 */

public class WinTimeSettingDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView cancel;
    private RadioGroup type_radio_group;
    private long time;
    private Appconfiguration config = Appconfiguration.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_win_time_setting, null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.96),
                (int) (dm.heightPixels * 0.5));
    }

    private void initView(View view) {
        cancel = (TextView) view.findViewById(R.id.cancel);
        type_radio_group = (RadioGroup) view.findViewById(R.id.type_radio_group);
        cancel.setOnClickListener(this);
        long initialTime = config.getWinUpdateTime();
        setRadioGroup(initialTime);
        type_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //这里做操作
                switch (checkedId) {
                    case R.id.type_default:
                        time = 3600 * 1000;
                        saveTime(time);
                        break;
                    case R.id.type_30_minute:
                        time = 1800 * 1000;
                        saveTime(time);
                        break;
                    case R.id.type_2_hour:
                        time = 7200 * 1000;
                        saveTime(time);
                        break;
                    case R.id.type_4_hour:
                        time = 14400 * 1000;
                        saveTime(time);
                        break;
                    case R.id.type_6_hour:
                        time = 18000 * 1000;
                        saveTime(time);
                        break;
                }
            }
        });
    }

    private void saveTime(long time) {
        config.setWinUpdateTime(time);
        dismiss();
    }

    private void setRadioGroup(long i) {
        if (i == 3600 * 1000) {
            type_radio_group.check(R.id.type_default);
        } else if (i == 1800 * 1000) {
            type_radio_group.check(R.id.type_30_minute);
        } else if (i == 7200 * 1000) {
            type_radio_group.check(R.id.type_2_hour);
        } else if (i == 14400 * 1000) {
            type_radio_group.check(R.id.type_4_hour);
        } else {
            type_radio_group.check(R.id.type_6_hour);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
        }

    }
}
