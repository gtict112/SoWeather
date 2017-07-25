package com.lhlSo.soweather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class AutofitTextView extends TextView {
    // private static final Log log = LogFactory.getLog(AutofitTextView.class);
    private static float DEFAULT_MIN_TEXT_SIZE = 1;
    private static float DEFAULT_MAX_TEXT_SIZE = 20;
    private TextPaint paint;
    private float minTextSize, maxTextSize;

    public AutofitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise();
    }

    public AutofitTextView(Context context) {
        super(context);
        initialise();
    }

    public AutofitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    // 除非初始文字大小太小，否则最大大小默认为初始文字大小
    private void initialise() {
        paint = getPaint();
        maxTextSize = getTextSize();
        // if (maxTextSize >= DEFAULT_MIN_TEXT_SIZE) {
        // maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        // }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
        setSingleLine(true);
        setEllipsize(null);
        setHorizontallyScrolling(false);
        // setGravity(Gravity.CENTER);
        // //如果不调用ondraw，另外画text的话当setSingleLine（true）和setGravity（Gravity。center）同时设置时textView中的内容无法正常显示
    }

    ;

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    /**
     * 根据文字内容和textView的大小设置文字大小使文字能全部显示
     *
     * @param text       textView中的内容
     * @param textWidth  textView控件的宽度
     * @param textHeight textView控件的高度
     */
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0) {
            // 得到textView中显示文本部分的长度
            int availableWidth = textWidth - getPaddingLeft() - getPaddingRight();
            float maxSize = maxTextSize;
            float minSize = minTextSize;
            float mid = maxSize;
            float widths[] = new float[text.length()];
            setTextSize(TypedValue.COMPLEX_UNIT_PX, mid);
            float widthSum = getTextWidth(text, widths);
            if (widthSum < availableWidth) {
                requestLayout();
                invalidate();
                return;
            }
            while (true) {
                if (maxSize - minSize < 0.01) {
                    requestLayout();
                    invalidate();
                }
                if (widthSum <= availableWidth) {
                    if (Math.abs(maxSize - minSize) < 0.5f) {
                        requestLayout();
                        invalidate();
                        return;
                    }
                    minSize = mid;
                    mid = (minSize + maxSize) / 2;
                } else {
                    maxSize = mid;
                    mid = (minSize + maxSize) / 2;
                }
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mid);
                widthSum = getTextWidth(text, widths);
            }
        }
    }

    private float getTextWidth(String text, float[] widths) {
        paint.getTextWidths(text, widths);
        float widthSum = 0;
        for (float f : widths) {
            widthSum += f;
        }
        return widthSum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        refitText(text.toString(), getWidth(), getHeight());
        super.onTextChanged(text, start, before, after);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(getText().toString(), w, h);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }
}