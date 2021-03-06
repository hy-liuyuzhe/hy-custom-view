package com.yuwq.hy_learn_draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author liuyuzhe
 */
public class MultipleCircleProgress extends View {

    public static final int CIRCLE_MARGIN_LEFT_SIZE = 20;
    public static final int CIRCLE_MARGIN_RIGHT_SIZE = 20;
    public static final String INIT_STATE_LINE_COLOR = "#EEEEEE";
    public static final String PROGRESS_STATE_LINE_COLOR = "#52AEFF";
    public static final int OFFSET = 2;
    public static final String SUCCESS_INNER_COLOR = "#52AEFF";
    public static final String FAIL_INNER_COLOR = "#FF4040";
    public static final String SUCCESS_MIDDLE_COLOR = "#CC7BC1FF";
    public static final String FAIL_MIDDLE_COLOR = "#CCFF8585";
    public static final String SUCCESS_OUTER_COLOR = "#66BADFFF";
    public static final String FAIL_OUTER_COLOR = "#66FFB6B6";
    public static final String DEFAULT_TEXT_COLOR = "#333333";
    public static final String SECOND_TEXT_TIP = "扫RFID条码";
    public static final int TEXT_MARGIN_TOP_OFFSET = 8;

    private CircleProgressStageEnum mStageEnum;
    private CircleStateEnum mCircleStateEnum;

    private LayerCircle mInitCircleLayer;
    private LayerCircle mOuterCircleLayer;
    private LayerCircle mMiddleCircleLayer;
    private LayerCircle mInnerCircleLayer;

    private int mCircleCenterY;
    private int mCircleCenterX;

    private RectF mInitCircleFirstRectF;
    private RectF mInitCircleSecondRectF;
    private RectF mInitCircleThirdRectF;

    private Paint mBasePaint;
    private Paint mLinePaint;
    private Paint mSuccessCirclePaint;
    private Paint mSuccessAndFailSignPaint;
    private Paint mTextPaint;
    private int mTextHeight;

    public MultipleCircleProgress(Context context) {
        this(context, null);
    }

    public MultipleCircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MultipleCircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mStageEnum = CircleProgressStageEnum.init;

        mBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBasePaint.setColor(Color.parseColor(INIT_STATE_LINE_COLOR));

        mSuccessCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(dipToPx(3f));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(dipToPx(11));
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(SECOND_TEXT_TIP, 0, SECOND_TEXT_TIP.length(), bounds);
        mTextHeight = bounds.height();

        mSuccessAndFailSignPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSuccessAndFailSignPaint.setStrokeCap(Paint.Cap.ROUND);
        mSuccessAndFailSignPaint.setStyle(Paint.Style.STROKE);
        mSuccessAndFailSignPaint.setStrokeWidth(dipToPx(2));
        mSuccessAndFailSignPaint.setColor(Color.WHITE);

        mInitCircleLayer = new LayerCircle(dipToPx(8 + OFFSET), INIT_STATE_LINE_COLOR);
        mOuterCircleLayer = new LayerCircle(dipToPx(13 + OFFSET));
        mMiddleCircleLayer = new LayerCircle(dipToPx(11 + OFFSET));
        mInnerCircleLayer = new LayerCircle(dipToPx(8 + OFFSET));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();

        mCircleCenterX = width / 2;
        mCircleCenterY = height / 2 - (mTextHeight + dipToPx(TEXT_MARGIN_TOP_OFFSET)) / 2;
        //16 20 24
        int initCircleDiameter = mInitCircleLayer.radius * 2;

        mInitCircleFirstRectF = new RectF();
        mInitCircleFirstRectF.set(0, 0, initCircleDiameter, initCircleDiameter);
        mInitCircleFirstRectF.offsetTo(dipToPx(CIRCLE_MARGIN_LEFT_SIZE), mCircleCenterY - mInitCircleLayer.radius);

        mInitCircleSecondRectF = new RectF();
        mInitCircleSecondRectF.set(0, 0, initCircleDiameter, initCircleDiameter);
        mInitCircleSecondRectF.offsetTo(mCircleCenterX - mInitCircleLayer.radius, mCircleCenterY - mInitCircleLayer.radius);

        mInitCircleThirdRectF = new RectF();
        mInitCircleThirdRectF.set(0, 0, initCircleDiameter, initCircleDiameter);
        mInitCircleThirdRectF.offsetTo(width - initCircleDiameter - dipToPx(CIRCLE_MARGIN_RIGHT_SIZE), mCircleCenterY - mInitCircleLayer.radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFirstLine(canvas);
        drawSecondLine(canvas);
        drawFirstCircle(canvas);
        drawSecondCircle(canvas);
        drawThirdCircle(canvas);
    }

    private void drawText(Canvas canvas, String text, String color, RectF rectF) {
        mTextPaint.setColor(Color.parseColor(color));
        float w = mTextPaint.measureText(text, 0, text.length());
        canvas.drawText(text, rectF.centerX() - w / 2, rectF.centerY() + rectF.height() + dipToPx(TEXT_MARGIN_TOP_OFFSET), mTextPaint);
    }

    private void drawSecondLine(Canvas canvas) {
        if (mStageEnum == CircleProgressStageEnum.third) {
            mLinePaint.setColor(Color.parseColor(PROGRESS_STATE_LINE_COLOR));
        } else {
            mLinePaint.setColor(Color.parseColor(INIT_STATE_LINE_COLOR));
        }
        RectF startRectF = mInitCircleSecondRectF;
        RectF stopRectF = mInitCircleThirdRectF;
        drawLine(canvas, startRectF, stopRectF);
    }

    private void drawFirstLine(Canvas canvas) {
        if (mStageEnum == CircleProgressStageEnum.third
                || mStageEnum == CircleProgressStageEnum.second) {
            mLinePaint.setColor(Color.parseColor(PROGRESS_STATE_LINE_COLOR));
        } else {
            mLinePaint.setColor(Color.parseColor(INIT_STATE_LINE_COLOR));
        }
        RectF startRectF = mInitCircleFirstRectF;
        RectF stopRectF = mInitCircleSecondRectF;
        drawLine(canvas, startRectF, stopRectF);
    }

    private void drawLine(Canvas canvas, RectF startRectF, RectF stopRectF) {
        canvas.drawLine(startRectF.centerX(), startRectF.centerY(), stopRectF.centerX(), stopRectF.centerY(), mLinePaint);
    }

    private void drawThirdCircle(Canvas canvas) {
        RectF thirdRectF = mInitCircleThirdRectF;
        String textColor = "";
        if (mStageEnum == CircleProgressStageEnum.third) {
            if (mCircleStateEnum == CircleStateEnum.success) {
                updateCircleColor(CircleStateEnum.success);
                drawHasStateCircle(canvas, mOuterCircleLayer, thirdRectF);
                drawHasStateCircle(canvas, mMiddleCircleLayer, thirdRectF);
                drawHasStateCircle(canvas, mInnerCircleLayer, thirdRectF);
                drawSuccessSign(canvas, thirdRectF);
            } else {
                updateCircleColor(CircleStateEnum.failed);
                drawHasStateCircle(canvas, mOuterCircleLayer, thirdRectF);
                drawHasStateCircle(canvas, mMiddleCircleLayer, thirdRectF);
                drawHasStateCircle(canvas, mInnerCircleLayer, thirdRectF);
                drawFailedSign(canvas, thirdRectF);
            }
        } else {
            drawInitCircle(canvas, mInitCircleThirdRectF);
            textColor = DEFAULT_TEXT_COLOR;
        }
        drawText(canvas, "绑定", TextUtils.isEmpty(textColor) ? mInnerCircleLayer.color : textColor, mInitCircleThirdRectF);
    }

    private void drawSecondCircle(Canvas canvas) {
        final RectF secondRectF = mInitCircleSecondRectF;
        String textColor = "";
        if (mStageEnum == CircleProgressStageEnum.second && mCircleStateEnum == CircleStateEnum.failed) {
            updateCircleColor(CircleStateEnum.failed);
            drawHasStateCircle(canvas, mOuterCircleLayer, secondRectF);
            drawHasStateCircle(canvas, mMiddleCircleLayer, secondRectF);
            drawHasStateCircle(canvas, mInnerCircleLayer, secondRectF);
            drawFailedSign(canvas, secondRectF);
        } else if (mStageEnum == CircleProgressStageEnum.third
                || mStageEnum == CircleProgressStageEnum.second && mCircleStateEnum == CircleStateEnum.success) {
            updateCircleColor(CircleStateEnum.success);
            drawHasStateCircle(canvas, mOuterCircleLayer, secondRectF);
            drawHasStateCircle(canvas, mMiddleCircleLayer, secondRectF);
            drawHasStateCircle(canvas, mInnerCircleLayer, secondRectF);
            drawSuccessSign(canvas, secondRectF);
        } else {
            drawInitCircle(canvas, secondRectF);
            textColor = DEFAULT_TEXT_COLOR;
        }
        drawText(canvas, SECOND_TEXT_TIP, TextUtils.isEmpty(textColor) ? mInnerCircleLayer.color : textColor, secondRectF);
    }

    private void drawFirstCircle(Canvas canvas) {
        if (mStageEnum == CircleProgressStageEnum.init) {
            drawInitCircle(canvas, mInitCircleFirstRectF);
        } else if (mStageEnum == CircleProgressStageEnum.first && mCircleStateEnum == CircleStateEnum.failed) {
            updateCircleColor(CircleStateEnum.failed);
            drawHasStateCircle(canvas, mOuterCircleLayer, mInitCircleFirstRectF);
            drawHasStateCircle(canvas, mMiddleCircleLayer, mInitCircleFirstRectF);
            drawHasStateCircle(canvas, mInnerCircleLayer, mInitCircleFirstRectF);
            drawFailedSign(canvas, mInitCircleFirstRectF);
        } else {
            updateCircleColor(CircleStateEnum.success);
            drawHasStateCircle(canvas, mOuterCircleLayer, mInitCircleFirstRectF);
            drawHasStateCircle(canvas, mMiddleCircleLayer, mInitCircleFirstRectF);
            drawHasStateCircle(canvas, mInnerCircleLayer, mInitCircleFirstRectF);
            drawSuccessSign(canvas, mInitCircleFirstRectF);
        }
        String textColor = mStageEnum == CircleProgressStageEnum.init ? DEFAULT_TEXT_COLOR : mInnerCircleLayer.color;
        drawText(canvas, "扫商品条码", textColor, mInitCircleFirstRectF);
    }

    private void drawFailedSign(Canvas canvas, RectF rectF) {
        canvas.save();
        Path path = new Path();
        path.moveTo(rectF.centerX() + (mInnerCircleLayer.radius >> 1), rectF.centerY());
        path.lineTo(rectF.centerX() - (mInnerCircleLayer.radius >> 1), rectF.centerY());
        path.moveTo(rectF.centerX(), rectF.centerY() + (mInnerCircleLayer.radius >> 1));
        path.rLineTo(0, -mInnerCircleLayer.radius);

        Matrix matrix = new Matrix();
        matrix.setRotate(-45, rectF.centerX(), rectF.centerY());
        path.transform(matrix);
        canvas.drawPath(path, mSuccessAndFailSignPaint);
        canvas.restore();

    }

    private void drawSuccessSign(Canvas canvas, RectF rectF) {
        canvas.save();
        Path path = new Path();
        path.moveTo(rectF.centerX() + (mInitCircleLayer.radius >> 1), rectF.centerY() + (mLinePaint.getStrokeWidth() / 2));
        path.rLineTo(-dipToPx(8), 0);
        path.rLineTo(0, -dipToPx(4));

        Matrix matrix = new Matrix();
        matrix.setRotate(-45, rectF.centerX(), rectF.centerY());
        path.transform(matrix);
        canvas.drawPath(path, mSuccessAndFailSignPaint);
        canvas.restore();
    }

    private void drawHasStateCircle(Canvas canvas, LayerCircle layerCircle, RectF rectF) {
        mSuccessCirclePaint.setColor(Color.parseColor(layerCircle.color));
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), layerCircle.radius, mSuccessCirclePaint);
    }

    private void drawInitCircle(Canvas canvas, RectF rectF) {
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), dipToPx(8 + OFFSET), mBasePaint);
    }

    public void setFirstCircleState(CircleStateEnum circleStateEnum) {
        mStageEnum = CircleProgressStageEnum.first;
        mCircleStateEnum = circleStateEnum;
        invalidate();
    }

    public void setSecondCircleState(CircleStateEnum circleStateEnum) {
        mStageEnum = CircleProgressStageEnum.second;
        mCircleStateEnum = circleStateEnum;
        invalidate();
    }

    public void setThirdCircleState(CircleStateEnum circleStateEnum) {
        mStageEnum = CircleProgressStageEnum.third;
        mCircleStateEnum = circleStateEnum;
        invalidate();
    }

    private void updateCircleColor(CircleStateEnum circleStateEnum) {
        boolean success = circleStateEnum == CircleStateEnum.success;
        mOuterCircleLayer.updateColor(success ? SUCCESS_OUTER_COLOR : FAIL_OUTER_COLOR);
        mMiddleCircleLayer.updateColor(success ? SUCCESS_MIDDLE_COLOR : FAIL_MIDDLE_COLOR);
        mInnerCircleLayer.updateColor(success ? SUCCESS_INNER_COLOR : FAIL_INNER_COLOR);
    }

    private int dipToPx(float dip) {
        return (int) (dip * getResources().getDisplayMetrics().density);
    }

    public void reset() {
        mStageEnum = CircleProgressStageEnum.init;
        invalidate();
    }


    private enum CircleProgressStageEnum {
        init,
        first,
        second,
        third;
    }

    public enum CircleStateEnum {
        failed,
        success
    }

    private static class LayerCircle {
        final int radius;
        String color;

        public LayerCircle(int radius, String color) {
            this.radius = radius;
            this.color = color;
        }

        private LayerCircle(int radius) {
            this.radius = radius;
        }

        public void updateColor(String color) {
            this.color = color;
        }
    }
}
