package com.example.administrator.soweather.com.example.administrator.soweather.view;
import android.graphics.Bitmap;

public class GifFrame {
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}
	/**图片*/
	public Bitmap image;
	/**延时*/
	public int delay;
	/**下一帧*/
	public GifFrame nextFrame = null;
}
