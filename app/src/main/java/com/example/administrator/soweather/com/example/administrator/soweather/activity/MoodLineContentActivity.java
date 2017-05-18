package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.view.LinedEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/29.
 */

public class MoodLineContentActivity extends BaseActivity implements View.OnClickListener {
    private TextView cancle;
    private TextView confirm;
    private ImageView skin;
    private ImageView photo;
    private ImageView graffiti;
    private TextView date;
    private LinedEditText content;
    private Boolean isSkin = false;
    private Boolean isPhoto = false;
    private PopupWindow popupwindowskin;
    private PopupWindow popupwindowphoto;
    private LinearLayout bg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodline_content);
        initView();
        getTime();
    }


    private void initView() {
        cancle = (TextView) findViewById(R.id.topLeft);
        confirm = (TextView) findViewById(R.id.topRight);
        skin = (ImageView) findViewById(R.id.skin);
        photo = (ImageView) findViewById(R.id.photo);
        graffiti = (ImageView) findViewById(R.id.graffiti);
        date = (TextView) findViewById(R.id.date);
        content = (LinedEditText) findViewById(R.id.content);
        bg = (LinearLayout) findViewById(R.id.bg);
        cancle.setOnClickListener(this);
        confirm.setOnClickListener(this);
        skin.setOnClickListener(this);
        photo.setOnClickListener(this);
        graffiti.setOnClickListener(this);
    }

    private void getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curdata = new Date(System.currentTimeMillis());
        String str = format.format(curdata);
        date.setText(str);
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
                if (!isSkin) {
                    checkpopupwindowphoto();
                    initmPopupWindowViewSkin();
                    popupwindowskin.showAsDropDown(skin, 30, -90);
                    isSkin = true;
                } else if (isSkin) {
                    checkpopupwindowphoto();
                    checkpopupwindowskin();
                }
                break;
            case R.id.photo:
                //选择图片  相册或者拍照
                if (!isPhoto) {
                    checkpopupwindowskin();
                    initmPopupWindowViewPhoto();
                    popupwindowphoto.showAsDropDown(photo, 30, -130);
                    isPhoto = true;

                } else if (isPhoto) {
                    checkpopupwindowphoto();
                    checkpopupwindowskin();
                }
                break;
            case R.id.graffiti:
                //涂鸦
                Toast.makeText(this, "涂鸦待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.a1:
                bg.setBackgroundResource(R.color.a1);
                checkpopupwindowskin();
                break;
            case R.id.a2:
                bg.setBackgroundResource(R.color.a2);
                checkpopupwindowskin();
                break;

            case R.id.a3:
                bg.setBackgroundResource(R.color.a3);
                checkpopupwindowskin();
                break;
            case R.id.a4:
                bg.setBackgroundResource(R.color.a4);
                checkpopupwindowskin();
                break;
            case R.id.a5:
                bg.setBackgroundResource(R.color.a5);
                checkpopupwindowskin();
                break;
            case R.id.a6:
                bg.setBackgroundResource(R.color.a6);
                checkpopupwindowskin();
                break;
            case R.id.camera:
                //拍照
                Toast.makeText(this, "拍照待开发", Toast.LENGTH_LONG).show();
                break;
            case R.id.photo_album:
                //相册
                Toast.makeText(this, "相册待开发", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }


    /**
     * 换肤
     */
    private void initmPopupWindowViewSkin() {
        View customView = getLayoutInflater().inflate(R.layout.popview_skin,
                null, false);
        popupwindowskin = new PopupWindow(customView, 350, 50);
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkpopupwindowskin();
                return false;
            }
        });
        TextView a1 = (TextView) customView.findViewById(R.id.a1);
        TextView a2 = (TextView) customView.findViewById(R.id.a2);
        TextView a3 = (TextView) customView.findViewById(R.id.a3);
        TextView a4 = (TextView) customView.findViewById(R.id.a4);
        TextView a5 = (TextView) customView.findViewById(R.id.a5);
        TextView a6 = (TextView) customView.findViewById(R.id.a6);
        a1.setOnClickListener(this);
        a2.setOnClickListener(this);
        a3.setOnClickListener(this);
        a4.setOnClickListener(this);
        a5.setOnClickListener(this);
        a6.setOnClickListener(this);
    }

    /**
     * 图片(相册或拍照)
     */
    private void initmPopupWindowViewPhoto() {
        View customView = getLayoutInflater().inflate(R.layout.popview_photo,
                null, false);
        popupwindowphoto = new PopupWindow(customView, 200, 80);
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkpopupwindowphoto();
                return false;
            }
        });
        TextView camera = (TextView) customView.findViewById(R.id.camera);
        TextView photo_album = (TextView) customView.findViewById(R.id.photo_album);
        camera.setOnClickListener(this);
        photo_album.setOnClickListener(this);
    }


    private void checkpopupwindowskin() {
        if (popupwindowskin != null && popupwindowskin.isShowing()) {
            popupwindowskin.dismiss();
            popupwindowskin = null;
        }
        isSkin = false;
    }

    private void checkpopupwindowphoto() {
        if (popupwindowphoto != null && popupwindowphoto.isShowing()) {
            popupwindowphoto.dismiss();
            popupwindowphoto = null;
        }
        isPhoto = false;
    }
}
