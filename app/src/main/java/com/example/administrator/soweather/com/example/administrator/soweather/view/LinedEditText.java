package com.example.administrator.soweather.com.example.administrator.soweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.administrator.soweather.R;

/**
 * Created by Administrator on 2016/11/29.
 */

public class LinedEditText extends EditText {
    private int color =0xFF660000;
    public LinedEditText(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        int lineBounds = 0;
        lineBounds = getLineBounds(0, rect);
        int lineHeight = getLineHeight();
        int num = this.getMeasuredHeight() - lineBounds / lineHeight;
        for (int j = 0; j < num; j++) {
            canvas.drawLine(rect.left, lineBounds + lineHeight * j, rect.right,
                    lineBounds + lineHeight * j, paint);
        }
        super.onDraw(canvas);
    }
}
