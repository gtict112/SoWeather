package com.lhlSo.soweather.general;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhlSo.soweather.R;


/**
 * Created by Administrator on 2017/7/14.
 */

public class LifeIndexDialogFragment extends DialogFragment {
    private String title;
    private String brf;
    private String tex;
    private ImageView bg;
    private LinearLayout content_layout;

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
        TextView cancle = (TextView) view.findViewById(R.id.cancel);
        content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        bg = (ImageView) view.findViewById(R.id.index_bg);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        life_index_title.setText(title);
        life_index_brf.setText(brf);
        life_index_tex.setText(tex);

        if (title.equals("穿衣指数")) {
            bg.setImageResource(R.mipmap.drsgbrf_img);
        } else if (title.equals("旅游指数")) {
            bg.setImageResource(R.mipmap.travbrf_img);
        } else if (title.equals("感冒指数")) {
            bg.setImageResource(R.mipmap.flubrf_img);
        } else {
            bg.setImageResource(R.mipmap.sportbrf_img);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.96),
                (int) (dm.heightPixels * 0.6));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
