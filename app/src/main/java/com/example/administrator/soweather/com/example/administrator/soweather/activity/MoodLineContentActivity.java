package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/11/29.
 */

public class MoodLineContentActivity extends Activity implements View.OnClickListener {
    private TextView cancle;
    private TextView confirm;
    private ImageView skin;
    private ImageView photo;
    private ImageView graffiti;
    private ImageView clock;
    private TextView date;
    private EditText content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodline_content);
        initView();
    }

    private void initView() {
        cancle = (TextView) findViewById(R.id.topLeft);
        confirm = (TextView) findViewById(R.id.topRight);
        skin = (ImageView) findViewById(R.id.skin);
        photo = (ImageView) findViewById(R.id.photo);
        graffiti = (ImageView) findViewById(R.id.graffiti);
        clock = (ImageView) findViewById(R.id.clock);
        date = (TextView) findViewById(R.id.date);
        content = (EditText) findViewById(R.id.content);
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        skin.setOnClickListener(this);
        photo.setOnClickListener(this);
        graffiti.setOnClickListener(this);
        clock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topLeft:
                finish();
                break;
            case R.id.topRight:
                //完成,先检查有无内容,在将数据保存到数据库中
                if (content.getText().toString().equals("")) {
                    Toast.makeText(this, "亲,请先输入内容哦", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "待开发", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.skin:
                //换肤
                Toast.makeText(this, "换肤待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.photo:
                //选择图片  相册或者拍照
                Toast.makeText(this, "选择图片待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.graffiti:
                //涂鸦
                Toast.makeText(this, "涂鸦待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.clock:
                //设置提醒时间
                Toast.makeText(this, "设置提醒时间待开发", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
