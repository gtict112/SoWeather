package com.lhlSo.soweather.activity;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.lhlSo.soweather.R;
import com.lhlSo.soweather.mode.Result;
import com.lhlSo.soweather.service.BeautyService;
import com.lhlSo.soweather.base.BaseActivity;
import com.lhlSo.soweather.utils.FileUtil;
import com.lhlSo.soweather.utils.ResponseListenter;
import com.lhlSo.soweather.utils.ShareUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liyu on 2016/11/3.
 */

public class PictureActivity extends BaseActivity {

    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    private ImageView mImageView;
    private String mImageUrl;
    private String mImageTitle;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    FileUtil.saveImage(true, (byte[]) msg.obj, PictureActivity.this);
                    SetWallPaper((byte[]) msg.obj);
                    break;
                case 1:
                    FileUtil.saveImage(false, (byte[]) msg.obj, PictureActivity.this);
                    break;
                case 2:
                    shareIamge((byte[]) msg.obj);
                    break;
            }
        }
    };


    public static Intent newIntent(Context context, String url, String desc) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture;
    }

    @Override
    protected int getMenuId() {
        return R.menu.menu_pic;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        showSystemUI();
        setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#000000"));
        mImageView = (ImageView) findViewById(R.id.picture);
        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideOrShowToolbar();
            }
        });
        loadData();
    }

    private void loadData() {
        mImageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(EXTRA_IMAGE_TITLE);
        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
        Glide.with(this).load(mImageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE).crossFade(0)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(mImageView);
        hideOrShowToolbar();
    }

    private void hideOrShowToolbar() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
            hideSystemUI();
        } else {
            showSystemUI();
            getSupportActionBar().show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.menu_share) {
            //分享
            BeautyService mBeautyService = new BeautyService();
            mBeautyService.loadImage(mImageUrl, new ResponseListenter<byte[]>() {
                @Override
                public void onReceive(Result<byte[]> result) {
                    if (result.isSuccess()) {
                        handler.sendMessage(handler.obtainMessage(2, result.getData()));
                    } else {
                        Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                                "分享失败", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (id == R.id.menu_save) {
            //保存
            BeautyService mBeautyService = new BeautyService();
            mBeautyService.loadImage(mImageUrl, new ResponseListenter<byte[]>() {
                @Override
                public void onReceive(Result<byte[]> result) {
                    if (result.isSuccess()) {
                        handler.sendMessage(handler.obtainMessage(1, result.getData()));
                        Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                                "图片保存成功", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                                "图片保存失败", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (id == R.id.menu_wallpaper) {
            //设置壁纸
            BeautyService mBeautyService = new BeautyService();
            mBeautyService.loadImage(mImageUrl, new ResponseListenter<byte[]>() {
                @Override
                public void onReceive(Result<byte[]> result) {
                    if (result.isSuccess()) {
                        handler.sendMessage(handler.obtainMessage(0, result.getData()));
                    } else {
                        Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                                "壁纸设置失败", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return true;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //设置壁纸
    public void SetWallPaper(byte[] bytes) {
        WallpaperManager mWallManager = WallpaperManager.getInstance(this);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        try {
            mWallManager.setBitmap(bitmap);
            Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                    "壁纸设置成功", Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                    "壁纸设置失败", Snackbar.LENGTH_SHORT).show();
        }
    }

    public static Uri getImageContentUri(Context context, String absPath) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=? "
                , new String[]{absPath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    private void shareIamge(byte[] bytes) {
        File file = Environment.getExternalStorageDirectory();
        File file1 = new File(file, "YoYo天气福利社");
        if (!file1.exists()) {
            file1.mkdirs();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        File imgFile = new File(file1, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(imgFile);
            Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            PictureActivity.this.sendBroadcast(scannerIntent);
            Uri contentURI = getImageContentUri(PictureActivity.this, imgFile.getAbsolutePath());
            ShareUtils.shareImage(PictureActivity.this, contentURI, "YOYO天气");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                    "分享失败", Snackbar.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Snackbar.make(PictureActivity.this.getWindow().getDecorView().findViewById(R.id.picture),
                    "分享失败", Snackbar.LENGTH_SHORT).show();
        }
    }
}
