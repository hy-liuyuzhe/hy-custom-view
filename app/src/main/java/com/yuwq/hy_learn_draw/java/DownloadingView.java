package com.yuwq.hy_learn_draw.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author liuyuzhe
 */
public class DownloadingView extends View {

    private static final long BEFORE_PROGRESS_CIRCLE_SCALE_DURATION = 1450;
    private static final int STATE_BEFORE_PROGRESS_CIRCLE_SCALE = 1;
    private static final int STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE = 2;
    public static final double MAX_LINE_WIDTH_FACTOR = 3.45;
    public static final double SUGGEST_X_AXIS_PADDING_TO_CIRCLE_DIAMETER_RATIO = 0.75;
    public static final double DEFAULT_ARROW_MOVE_MAX_HEIGHT_TO_CIRCLE_DIAMETER_RATIO = 1.975;
    public static final double DEFAULT_INIT_ARROW_WIDTH_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    public static final double DEFAULT_INIT_ARROW_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    private static final float DEFAULT_INIT_ARROW_TRI_WIDTH_TO_CIRCLE_RADIUS_RATIO = 1f;
    private static final float DEFAULT_INIT_ARROW_TRI_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    private static final float DEFAULT_ARROW_TOP_CONNER_RADIUS_TO_CIRCLE_RADIUS_RATIO = 0.05f;
    private static final float DEFAULT_END_ARROW_RECT_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    //animation state
    private int mCurrentState = 0;

    private Paint mDefaultPaint;
    private Paint mBaseLinePaint;

    private ValueAnimator mBFPCircleScaleAnimator;

    private int mLoadingViewCenterX;
    private int mLoadingViewCenterY;
    private float mBFPCircleScalingFactor;
    private int mCircleDiameter;
    private int mCircleRadius;
    private RectF mCircleRectF;

    private int mLoadingLineColor = 0xFF491c14;
    private int mInitArrowRectWidth;
    private int mInitArrowRectHeight;
    private int mInitArrowTriWidth;
    private int mInitArrowTriHeight;
    private int mInitArrowJointConnerRadius;
    private Path mArrowPath;
    private RectF mArrowRectF;
    private int lastArrowRectWidth;
    private int lastArrowRectHeight;
    private int mLastArrowTriWidth;
    private int mLastArrowTriHeight;
    private int mArrowColor = 0xFFFFFFFF;
    private int mEndArrowRectHeight;
    private ArrayList<Animator> mAnimatorList;
    private ValueAnimator mBFPInnerCircleScaleAnimator;
    private float mBFPInnerCircleScalingFactor;
    private PorterDuffXfermode porterDuffXfermode;
    private PorterDuffXfermode mPorterDuffXfermode;
    private boolean mIsFailed;
    private Path mBaseLinePath;
    private RectF mBaseLineRectF;


    public DownloadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDefaultPaint = new Paint();
        mDefaultPaint.setAntiAlias(true);
        mBaseLinePaint = new Paint();
        mBaseLinePaint.setAntiAlias(true);
        mBaseLinePaint.setStyle(Paint.Style.STROKE);
        mBaseLinePaint.setStrokeCap(Paint.Cap.ROUND);//drawLine 是圆角
        mBaseLinePaint.setPathEffect(new CornerPathEffect(10f));//path 折线加圆角
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mBaseLinePath = new Path();
        mBaseLineRectF = new RectF();

        mArrowPath = new Path();
        mCircleRectF = new RectF();
        mArrowRectF = new RectF();
        mIsFailed = false;
        mAnimatorList = new ArrayList<Animator>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLoadingViewCenterX = w / 2;
        mLoadingViewCenterY = h / 2;
        mCircleDiameter = (int) Math.min((w / (MAX_LINE_WIDTH_FACTOR + SUGGEST_X_AXIS_PADDING_TO_CIRCLE_DIAMETER_RATIO)),
                (h / DEFAULT_ARROW_MOVE_MAX_HEIGHT_TO_CIRCLE_DIAMETER_RATIO));
        mCircleRadius = mCircleDiameter / 2;
        mCircleRectF.set(0, 0, mCircleDiameter, mCircleDiameter);
        //居中
        mCircleRectF.offsetTo((w - mCircleRectF.width()) / 2, (h - mCircleRectF.height()) / 2);

        mInitArrowRectWidth = (int) (mCircleRadius * DEFAULT_INIT_ARROW_WIDTH_TO_CIRCLE_RADIUS_RATIO);
        mInitArrowRectHeight = (int) (mCircleRadius * DEFAULT_INIT_ARROW_HEIGHT_TO_CIRCLE_RADIUS_RATIO);
        mInitArrowTriWidth = (int) (mCircleRadius * DEFAULT_INIT_ARROW_TRI_WIDTH_TO_CIRCLE_RADIUS_RATIO);
        mInitArrowTriHeight = (int) (mCircleRadius * DEFAULT_INIT_ARROW_TRI_HEIGHT_TO_CIRCLE_RADIUS_RATIO);
        mInitArrowJointConnerRadius = (int) (mCircleRadius * DEFAULT_ARROW_TOP_CONNER_RADIUS_TO_CIRCLE_RADIUS_RATIO);
        mEndArrowRectHeight = (int) (mCircleRadius * DEFAULT_END_ARROW_RECT_HEIGHT_TO_CIRCLE_RADIUS_RATIO);
        mArrowRectF.set(mCircleRectF);

        mBaseLinePaint.setStrokeWidth(dipToPx(11));
        mDefaultPaint.setPathEffect(new CornerPathEffect(mInitArrowJointConnerRadius));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_BEFORE_PROGRESS_CIRCLE_SCALE:
                drawCircleAndArrowScale(canvas, mBFPCircleScalingFactor);
                break;
            case STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE:
                drawInnerCircleAndArrowScale(canvas, mBFPCircleScalingFactor, mBFPInnerCircleScalingFactor);
                break;
            default:
        }
    }

    private void drawInnerCircleAndArrowScale(Canvas canvas, float circleScalingFactor, float innerCircleScalingFactor) {
        //draw xfermode
        canvas.save();
        canvas.scale(circleScalingFactor, circleScalingFactor, mCircleRectF.centerX(), mCircleRectF.centerY());
        int layerCount = canvas.saveLayer(mCircleRectF, mDefaultPaint);
        mDefaultPaint.setColor(mLoadingLineColor);
        canvas.drawCircle(mCircleRectF.centerX(), mCircleRectF.centerY(), mCircleRadius, mDefaultPaint);
        mDefaultPaint.setXfermode(mPorterDuffXfermode);
        int innerCircleRadius = (int) (mCircleRadius * innerCircleScalingFactor);
        canvas.drawCircle(mCircleRectF.centerX(), mCircleRectF.centerY(), innerCircleRadius, mDefaultPaint);
        mDefaultPaint.setXfermode(null);
        canvas.restoreToCount(layerCount);

        //draw arrow
        updateArrowPath(mInitArrowRectWidth, mInitArrowRectHeight, mInitArrowTriWidth, mInitArrowTriHeight);
        canvas.translate(mCircleRectF.centerX() - (mCircleRadius >> 1) * circleScalingFactor,
                mCircleRectF.centerY() - (mCircleRadius >> 1) * circleScalingFactor);
        mDefaultPaint.setColor(mArrowColor);
        canvas.drawPath(mArrowPath, mDefaultPaint);

        canvas.restore();
    }

    private void drawCircleAndArrowScale(Canvas canvas, float scaleFactor) {
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor, mCircleRectF.centerX(), mCircleRectF.centerY());
        mDefaultPaint.setColor(mLoadingLineColor);
        canvas.drawCircle(mCircleRectF.centerX(), mCircleRectF.centerY(), mCircleRadius, mDefaultPaint);

        updateArrowPath(mInitArrowRectWidth,
                mInitArrowRectHeight, mInitArrowTriWidth, mInitArrowTriHeight);
        canvas.translate(mCircleRectF.centerX() - ((mInitArrowTriWidth >> 1) * scaleFactor),
                mCircleRectF.centerY() - (mCircleRadius >> 1) * scaleFactor);
        mDefaultPaint.setColor(mArrowColor);
        canvas.drawPath(mArrowPath, mDefaultPaint);

        canvas.restore();
    }

    private void updateArrowPath(int rectWidth, int rectHeight, int triWidth, int triHeight) {
        if (rectWidth == lastArrowRectWidth) {
            return;
        } else {
            mArrowPath.reset();
        }
        lastArrowRectWidth = rectWidth;
        lastArrowRectHeight = rectHeight;
        mLastArrowTriWidth = triWidth;
        mLastArrowTriHeight = triHeight;

        int arrowWidth = Math.max(rectWidth, triWidth);//118
        int halfArrowWidth = arrowWidth / 2; //59
        int arrowHeight = rectHeight + triHeight;//118
        int rectPaddingLeft = (arrowWidth - rectWidth) / 2;//29
        int triPaddingLeft = (arrowWidth - triWidth) / 2;//0

        mArrowPath.moveTo(halfArrowWidth, 0);//move right
        mArrowPath.lineTo(rectPaddingLeft, 0);//line left
        mArrowPath.lineTo(rectPaddingLeft, rectHeight);//line down
        mArrowPath.lineTo(triPaddingLeft, rectHeight);//line skew line
        mArrowPath.lineTo(halfArrowWidth, arrowHeight);
        mArrowPath.lineTo(arrowWidth - triPaddingLeft, rectHeight);
        mArrowPath.lineTo(arrowWidth - rectPaddingLeft, rectHeight);
        mArrowPath.lineTo(arrowWidth - rectPaddingLeft, 0);
        mArrowPath.lineTo(halfArrowWidth, 0);
    }

    public void performAnimation() {
        releaseAnimation();
        //circle and arrow
        mBFPCircleScaleAnimator = ValueAnimator.ofFloat(1f, 0.91f, 1f);
        mBFPCircleScaleAnimator.addUpdateListener(animation -> {
            mBFPCircleScalingFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        mBFPCircleScaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCurrentState = STATE_BEFORE_PROGRESS_CIRCLE_SCALE;
            }
        });
        mBFPCircleScaleAnimator.setDuration(BEFORE_PROGRESS_CIRCLE_SCALE_DURATION);
        mAnimatorList.add(mBFPCircleScaleAnimator);
        //inner circle arrow scale

        mBFPInnerCircleScaleAnimator = ValueAnimator.ofFloat(0, 0.9f);
        mBFPInnerCircleScaleAnimator.addUpdateListener(animation -> {
            mBFPInnerCircleScalingFactor = (float) animation.getAnimatedValue();
        });
        mBFPInnerCircleScaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCurrentState = STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE;
            }
        });
        mAnimatorList.add(mBFPInnerCircleScaleAnimator);
        mBFPInnerCircleScaleAnimator.setStartDelay(BEFORE_PROGRESS_CIRCLE_SCALE_DURATION / 2);
        mBFPInnerCircleScaleAnimator.setDuration(BEFORE_PROGRESS_CIRCLE_SCALE_DURATION / 2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(mBFPCircleScaleAnimator);
        animatorSet.playTogether(mBFPInnerCircleScaleAnimator);
        animatorSet.start();
    }

    private void releaseAnimation() {
        for (Animator animator : mAnimatorList) {
            if (animator instanceof ValueAnimator) {
                ((ValueAnimator) animator).removeAllUpdateListeners();
            }
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
        mAnimatorList.clear();
    }


    private int dipToPx(int dip) {
        return (int) (dip * getScreenDensity(getContext()) + 0.5f);
    }

    private float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }
}
