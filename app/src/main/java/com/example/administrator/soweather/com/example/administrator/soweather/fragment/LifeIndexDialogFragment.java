package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2017/7/14.
 */

public class LifeIndexDialogFragment extends DialogFragment {
    private String title;
    private String brf;
    private String tex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_life_index, null);
        initDate();
        initView(view);
        return view;
    }

    private void initDate() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            title = mBundle.getString("title");
            brf = mBundle.getString("brf");
            tex = mBundle.getString("tex");
        }
    }

    private void initView(View view) {
        TextView life_index_title = (TextView) view.findViewById(R.id.life_index_title);
        TextView life_index_brf = (TextView) view.findViewById(R.id.life_index_brf);
        TextView life_index_tex = (TextView) view.findViewById(R.id.life_index_tex);
        TextView close = (TextView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        life_index_title.setText(title);
        life_index_brf.setText(brf);
        life_index_tex.setText(tex);
    }


    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.96),
                (int) (dm.heightPixels * 0.6));
    }
}
