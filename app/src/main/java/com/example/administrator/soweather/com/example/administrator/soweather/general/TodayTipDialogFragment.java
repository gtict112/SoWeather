package com.example.administrator.soweather.com.example.administrator.soweather.general;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2017/5/27.
 */

public class TodayTipDialogFragment extends DialogFragment {
    private TextView index;
    private TextView wind;
    private TextView desc;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_today_tip, null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void initView(View view) {
        index = (TextView) view.findViewById(R.id.index);
        wind = (TextView) view.findViewById(R.id.wind);
        desc = (TextView) view.findViewById(R.id.desc);
        wind.setText(getArguments().getString("wind"));
        desc.setText(getArguments().getString("desc"));
        index.setText(getArguments().getString("index"));
    }
}
