package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.soweather.R;
/**
 * Created by Administrator on 2016/10/10.
 * MainActivity
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_main);
       //第一次进入加载进度圆圈,进行数据更新
        // 下拉刷新
    }
}
