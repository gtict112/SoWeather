package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyDetail;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.BeautyService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class BeautyDetailActivity extends BaseActivity implements ResponseListenter<BeautyDetail> {
    private String id = "1";
    private Handler mHandler;
    private BeautyDetail mDate = new BeautyDetail();
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView topTv;
    private ImageView topButton;
    private ImageView src;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        initDate();
        initView();
        getDate();
        getHandleMessge();
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    setView(mDate);
                }
            }
        };
    }

    private void setView(BeautyDetail mDate) {
        config.dismissProgressDialog();
        topTv.setText("详情");
        Glide.with(this).load(mDate.img).animate(R.anim.img_loading).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_loading_eholder).centerCrop().into(src);
    }


    private void initDate() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    private void initView() {
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        src = (ImageView) findViewById(R.id.src);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getDate() {
        config.showProgressDialog("正在加载..", this);
        BeautyService mBeautyService = new BeautyService();
        mBeautyService.getBeautyDetail(this, id);
    }

    @Override
    public void onReceive(Result<BeautyDetail> result) {
        if (result.isSuccess()) {
            mDate = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
        } else {
            config.dismissProgressDialog();
        }
    }


}
