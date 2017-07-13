package com.example.administrator.soweather.com.example.administrator.soweather.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FileUtil {

    public static void saveImage(Boolean isWallpaper, byte[] bytes, Context context) {
        File file = Environment.getExternalStorageDirectory();
        if (isWallpaper) {
            getWallpaperDir(file, bytes, context, isWallpaper);
        } else {
            getImageDir(file, bytes, context, isWallpaper);
        }
    }


    public static void getWallpaperDir(File file, byte[] bytes, Context context, Boolean isWallpaper) {
        File file1 = new File(file, "YoYo天气壁纸");
        if (!file1.exists()) {
            file1.mkdirs();
        }
        saveImageToSD(file1, bytes, context, isWallpaper);
    }

    public static void getImageDir(File file, byte[] bytes, Context context, Boolean isWallpaper) {
        File file2 = new File(file, "YoYo天气图库");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        saveImageToSD(file2, bytes, context, isWallpaper);
    }


    public static void saveImageToSD(File file, byte[] bytes, Context context, Boolean isWallpaper) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        File imgFile = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!isWallpaper) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(imgFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }

    }
}
