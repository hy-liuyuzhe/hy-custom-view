package com.yuwq.hy_learn_draw.java;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @author liuyuzhe
 */
public class LoginDialogClearView extends View {

    public static final int STROKE_WIDTH = 1;
    private int mWidth;
    private int mHeight;
    private Paint paint = new Paint();
    private Path path = new Path();
    private int mLineSize;
    private int mCircleWidth;

    public LoginDialogClearView(@NonNull Context context) {
        super(context);
    }

    public LoginDialogClearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginDialogClearView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCircleWidth = (mWidth >> 1) - dp2px(STROKE_WIDTH);
        mLineSize = mCircleWidth /2 ;

        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(dp2px(STROKE_WIDTH));
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth >> 1, mHeight >> 1);

        canvas.drawCircle(0f, 0f, mCircleWidth, paint);

        canvas.rotate(45);
        //line1
        path.moveTo(-mLineSize, 0);
        path.lineTo(mLineSize, 0);
        //line2
        path.moveTo(0,-mLineSize);
        path.lineTo(0,mLineSize);

        canvas.drawPath(path, paint);
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
