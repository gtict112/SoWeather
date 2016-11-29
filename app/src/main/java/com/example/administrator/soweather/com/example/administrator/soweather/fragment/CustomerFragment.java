package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.CustomerServiceActivity;

/**
 * Created by Administrator on 2016/11/11.
 */

public class CustomerFragment extends Fragment implements View.OnClickListener {
    private LinearLayout customer_service;
    private LinearLayout alarm_clock;
    private LinearLayout running;

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
        View view = inflater.inflate(R.layout.fragment_customer, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        customer_service = (LinearLayout) view.findViewById(R.id.customer_service);
        alarm_clock = (LinearLayout) view.findViewById(R.id.alarm_clock);
        running = (LinearLayout) view.findViewById(R.id.running);
        alarm_clock.setOnClickListener(this);
        running.setOnClickListener(this);
        customer_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_service:
                Intent intent = new Intent(getActivity(), CustomerServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.alarm_clock:
                Toast.makeText(getActivity(),"待开发",Toast.LENGTH_LONG).show();
                break;
            case R.id.running:
                Toast.makeText(getActivity(),"待开发",Toast.LENGTH_LONG).show();
                break;

        }
    }
}
