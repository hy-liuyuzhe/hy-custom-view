package com.yuwq.hy_learn_draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author liuyuzhe
 */
public class MixModeView extends View {

    private int mCircleColor = 0xFFFF0000;
    private int mSquareColor = 0xFF0000FF;
    private static final int ROW_ITEM_NUM = 4;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;

    private static final PorterDuff.Mode[] MIXED_MODE_ARR = new PorterDuff.Mode[]{
            /** [0, 0] */
            PorterDuff.Mode.CLEAR,
            /** [Sa, Sc] */
            PorterDuff.Mode.SRC,
            /** [Da, Dc] */
            PorterDuff.Mode.DST,
            /** [Sa + (1 - Sa)*Da, Rc = Sc + (1 - Sa)*Dc] */
            PorterDuff.Mode.SRC_OVER,
            /** [Sa + (1 - Sa)*Da, Rc = Dc + (1 - Da)*Sc] */
            PorterDuff.Mode.DST_OVER,
            /** [Sa * Da, Sc * Da] */
            PorterDuff.Mode.SRC_IN,
            /** [Sa * Da, Sa * Dc] */
            PorterDuff.Mode.DST_IN,
            /** [Sa * (1 - Da), Sc * (1 - Da)] */
            PorterDuff.Mode.SRC_OUT,
            /** [Da * (1 - Sa), Dc * (1 - Sa)] */
            PorterDuff.Mode.DST_OUT,
            /** [Da, Sc * Da + (1 - Sa) * Dc] */
            PorterDuff.Mode.SRC_ATOP,
            /** [Sa, Sa * Dc + Sc * (1 - Da)] */
            PorterDuff.Mode.DST_ATOP,
            /** [Sa + Da - 2 * Sa * Da, Sc * (1 - Da) + (1 - Sa) * Dc] */
            PorterDuff.Mode.XOR,
            /** [Sa + Da - Sa*Da,
            Sc*(1 - Da) + Dc*(1 - Sa) + min(Sc, Dc)] */
            PorterDuff.Mode.DARKEN,
            /** [Sa + Da - Sa*Da,
            Sc*(1 - Da) + Dc*(1 - Sa) + max(Sc, Dc)] */
            PorterDuff.Mode.LIGHTEN,
            /** [Sa * Da, Sc * Dc] */
            PorterDuff.Mode.MULTIPLY,
            /** [Sa + Da - Sa * Da, Sc + Dc - Sc * Dc] */
            PorterDuff.Mode.SCREEN,
    };

    private static final String[] MIXED_MODE_STR_ARR = new String[]{
            "CLEAR",
            "SRC",
            "DST",
            "SRC_OVER",
            "DST_OVER",
            "SRC_IN",
            "DST_IN",
            "SRC_OUT",
            "DST_OUT",
            "SRC_ATOP",
            "DST_ATOP",
            "XOR",
            "DARKEN",
            "LIGHTEN",
            "MULTIPLY",
            "SCREEN"
    };


    public MixModeView(Context context) {
        this(context, null);
    }

    public MixModeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MixModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int len = MIXED_MODE_ARR.length;
        int yOffset = mHeight / 20;
        int xOffset = mWidth / 20;
        int colSize = (len + ROW_ITEM_NUM - 1) / ROW_ITEM_NUM; //多加3个向上取整
        int yLen = (mHeight - yOffset * 2) / colSize;
        int xLen = (mWidth - yOffset) / ROW_ITEM_NUM;
        Log.d("liuyuzhe", "onDraw.yLen: " + yLen);

        RectF rectF = new RectF();
        for (int i = 0; i < len; i++) {
            int rows = i / 4;
            int cols = i % 4;
            Log.d("liuyuzhe", "rows: " + rows + "& cols: " + cols);
            int left = xOffset + cols * xLen;
            int top = yOffset + rows * yLen;
            rectF.set(left, top, left + xLen, top + yLen);

            drawMixedMode(rectF, MIXED_MODE_ARR[i], canvas, mPaint, MIXED_MODE_STR_ARR[i]);
        }
    }

    private void drawMixedMode(RectF rectF, PorterDuff.Mode mode, Canvas canvas, Paint paint, String mixedModeText) {
        int layerId = canvas.saveLayer(rectF, mPaint);
        Log.d("liuyuzhe", "drawMixedMode.layerId: "+layerId);
        //绘制圆dst
        int width = (int) rectF.width();
        int height = (int) rectF.height();
        int len = Math.min(width, height);
        int radius = len / 4; //取整体4分支一作为半径
        int squareLen = radius * 2; //整体一半作为矩形的宽高
        int offsetX = (width - squareLen - radius) / 2;


        int textSize = radius * 2 / 3;//直径除以3
        paint.setTextSize(textSize);
        Rect textBounds = new Rect();
        paint.getTextBounds(mixedModeText, 0, mixedModeText.length(), textBounds);
        paint.setColor(mCircleColor);
        //这个 cx和cy是圆心的坐标
        canvas.drawCircle(rectF.left + offsetX + radius, rectF.top + textBounds.height() * 2 + radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));

        //绘制方块 src
        paint.setColor(mSquareColor);
        Rect rect = new Rect(0, 0, squareLen, squareLen);
        int dx = (int) (rectF.left + offsetX + radius);
        int dy = (int) (rectF.top + textBounds.height() * 2 + radius);
        rect.offset(dx, dy);
        canvas.drawRect(rect, paint);
        //绘制文字

//      drawDebugRect
        paint.setXfermode(null);
        debugRect(rectF, canvas);
        canvas.restoreToCount(layerId);

        paint.setColor(Color.BLACK);
        canvas.drawText(mixedModeText, rectF.left + width / 2 - textBounds.width() / 2,
                rectF.top - textBounds.top + textBounds.height() / 2, paint);
    }

    private void debugRect(RectF rectF, Canvas canvas) {
        Paint debugPaint = new Paint();
        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setColor(Color.BLUE);
        canvas.drawRect(rectF, debugPaint);
    }
}
