package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MoreInfoItemFragment extends Fragment {
    private TextView title;
    private String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_more_info_item, container, false);
        initView(contextView);
        return contextView;
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.title);
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            time = mBundle.getString("type");
            title.setText(time);
        }
    }
}