package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.feng.skin.manager.listener.ILoaderListener;
import cn.feng.skin.manager.loader.SkinManager;
import cn.feng.skin.manager.util.L;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SettingSkinActivity extends BaseActivity implements View.OnClickListener {
    private static final String DATAPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SoWeather主题包";
    /**
     * 在DATAPATH中新建这个目录，TessBaseAPI初始化要求必须有这个目录。
     */
    private static final String tessdata = DATAPATH + File.separator + "主题";
    /**
     * TessBaseAPI初始化测第二个参数，就是识别库的名字不要后缀名。
     */
    private static final String DEFAULT_LANGUAGE = "BlackFantacy";
    /**
     * assets中的文件名
     */
    private static final String DEFAULT_LANGUAGE_NAME = DEFAULT_LANGUAGE + ".skin";
    /**
     * 保存到SD卡中的完整文件名
     */
    private static final String LANGUAGE_PATH = tessdata + File.separator + DEFAULT_LANGUAGE_NAME;


    private Button setOfficalSkinBtn;
    private Button setNightSkinBtn;

    private boolean isOfficalSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_skin);
        initSkinData();
        initView();
    }

    private void initSkinData() {
        //如果存在就删掉
        File f = new File(LANGUAGE_PATH);
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            File p = new File(f.getParent());
            if (!p.exists()) {
                p.mkdirs();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = this.getAssets().open(DEFAULT_LANGUAGE_NAME);
            File file = new File(LANGUAGE_PATH);
            os = new FileOutputStream(file);
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void initView() {
        ImageView topButton = (ImageView) findViewById(R.id.topButton);
        TextView topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("主题设置");
        topButton.setOnClickListener(this);
        setOfficalSkinBtn = (Button) findViewById(R.id.set_default_skin);
        setNightSkinBtn = (Button) findViewById(R.id.set_night_skin);


        isOfficalSelected = !SkinManager.getInstance().isExternalSkin();

        if (isOfficalSelected) {
            setOfficalSkinBtn.setText("官方默认(当前)");
            setNightSkinBtn.setText("黑色幻想");
        } else {
            setNightSkinBtn.setText("黑色幻想(当前)");
            setOfficalSkinBtn.setText("官方默认");
        }

        setNightSkinBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSkinSetClick();
            }
        });

        setOfficalSkinBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSkinResetClick();
            }
        });
    }

    protected void onSkinResetClick() {
        if (!isOfficalSelected) {
            SkinManager.getInstance().restoreDefaultTheme();
            Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
            setOfficalSkinBtn.setText("官方默认(当前)");
            setNightSkinBtn.setText("黑色幻想");
            isOfficalSelected = true;
        }
    }

    private void onSkinSetClick() {
        if (!isOfficalSelected) return;

        File skin = new File(LANGUAGE_PATH);

        if (skin == null || !skin.exists()) {
            Toast.makeText(getApplicationContext(), "请检查" + LANGUAGE_PATH + "是否存在", Toast.LENGTH_SHORT).show();
            return;
        }

        SkinManager.getInstance().load(skin.getAbsolutePath(),
                new ILoaderListener() {
                    @Override
                    public void onStart() {
                        L.e("startloadSkin");
                    }

                    @Override
                    public void onSuccess() {
                        L.e("loadSkinSuccess");
                        Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                        setNightSkinBtn.setText("黑色幻想(当前)");
                        setOfficalSkinBtn.setText("官方默认");
                        isOfficalSelected = false;
                    }

                    @Override
                    public void onFailed() {
                        L.e("loadSkinFail");
                        Toast.makeText(getApplicationContext(), "切换失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                break;
        }
    }
}
