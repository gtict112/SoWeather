package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.HelpFeedbackActivity;

/**
 * Created by Administrator on 2016/10/26.
 */

public class HelpFeedbackFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout feed_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help_feedback, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        feed_back = (RelativeLayout) view.findViewById(R.id.feed_back);
        feed_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feed_back:
                Intent intent = new Intent(getActivity(), HelpFeedbackActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
