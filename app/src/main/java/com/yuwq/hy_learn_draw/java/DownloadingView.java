package com.yuwq.hy_learn_draw.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
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
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author liuyuzhe
 */
public class DownloadingView extends View {

    //animation state
    private int mCurrentState = 0;
    private static final int STATE_BEFORE_PROGRESS_CIRCLE_SCALE = 1;
    private static final int STATE_BEFORE_PROGRESS_INNER_CIRCLE_SCALE = 2;
    private static final int STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE = 3;

    private static final long BEFORE_PROGRESS_CIRCLE_SCALE_DURATION = 1450;
    private static final long BEFORE_PROGRESS_CIRCLE_TO_LINE_DURATION = 1150;
    public static final double MAX_LINE_WIDTH_FACTOR = 3.45;
    public static final double SUGGEST_X_AXIS_PADDING_TO_CIRCLE_DIAMETER_RATIO = 0.75;
    public static final double DEFAULT_ARROW_MOVE_MAX_HEIGHT_TO_CIRCLE_DIAMETER_RATIO = 1.975;
    public static final double DEFAULT_INIT_ARROW_WIDTH_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    public static final double DEFAULT_INIT_ARROW_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    private static final float DEFAULT_INIT_ARROW_TRI_WIDTH_TO_CIRCLE_RADIUS_RATIO = 1f;
    private static final float DEFAULT_INIT_ARROW_TRI_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;
    private static final float DEFAULT_ARROW_TOP_CONNER_RADIUS_TO_CIRCLE_RADIUS_RATIO = 0.05f;
    private static final float DEFAULT_END_ARROW_RECT_HEIGHT_TO_CIRCLE_RADIUS_RATIO = 0.5f;

    //circleToLine
    private static final float[] CIRCLE_TO_LINE_SEASONS = new float[]{0, 0.4f, 0.8f, 1f};
    private static final float[] CIRCLE_TO_LINE_WIDTH_FACTOR = new float[]{1f, 1.2f, 2.4f, 3.45f};
    private static final float[] CIRCLE_TO_LINE_HEIGHT_FACTOR = new float[]{1f, 0.73f, 0.36f, 0f};
    private static final float[] CIRCLE_TO_LINE_FST_CON_X_FACTOR = new float[]{-0.65f, 0.18f, 0.72f, 1.04f};
    private static final float[] CIRCLE_TO_LINE_FST_CON_Y_FACTOR = new float[]{0f, 0.036f, 0f, 0f};
    private static final float[] CIRCLE_TO_LINE_SEC_CON_X_FACTOR = new float[]{-0.65f, 0.06f, 0.72f, 1.04f};
    private static final float[] CIRCLE_TO_LINE_SEC_CON_Y_FACTOR = new float[]{1f, 0.73f, 0.36f, 0f};


    private Paint mDefaultPaint;
    private Paint mBaseLinePaint;

    private ArrayList<Animator> mAnimatorList;
    private ValueAnimator mBFPCircleScaleAnimator;
    private ValueAnimator mBFPInnerCircleScaleAnimator;

    private int mLoadingViewCenterX;
    private int mLoadingViewCenterY;
    //circle
    private float mBFPCircleScalingFactor;
    private int mCircleDiameter;
    private int mCircleRadius;
    private RectF mCircleRectF;

    private int mLoadingLineColor = 0xFF491c14;
    private int mArrowColor = 0xFFFFFFFF;

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
    private int mEndArrowRectHeight;


    private float mBFPInnerCircleScalingFactor;
    private PorterDuffXfermode mPorterDuffXfermode;
    private boolean mIsFailed;
    private Path mBaseLinePath;
    private RectF mBaseLineRectF;
    private ValueAnimator mBFPCircleToLineAnimator;
    private float mBFPCircleToLineFactor;

    private int mLastArrowOffsetX;
    private int mLastArrowOffsetY;
    private Matrix mArrowRotateMatrix;
    private float mDegress;


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
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);


        mBaseLinePaint = new Paint();
        mBaseLinePaint.setAntiAlias(true);
        mBaseLinePaint.setStyle(Paint.Style.STROKE);
        mBaseLinePaint.setStrokeCap(Paint.Cap.ROUND);//drawLine 是圆角
        mBaseLinePaint.setPathEffect(new CornerPathEffect(10f));//path 折线加圆角
        mBaseLinePath = new Path();
        mBaseLineRectF = new RectF();

        mArrowPath = new Path();
        mCircleRectF = new RectF();
        mArrowRectF = new RectF();

        mIsFailed = false;
        mAnimatorList = new ArrayList<>();
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
        mBaseLinePaint.setStrokeWidth(dipToPx(2));

        mDefaultPaint.setPathEffect(new CornerPathEffect(mInitArrowJointConnerRadius));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_BEFORE_PROGRESS_CIRCLE_SCALE:
                drawCircleAndArrowScale(canvas, mBFPCircleScalingFactor);
                break;
            case STATE_BEFORE_PROGRESS_INNER_CIRCLE_SCALE:
                drawInnerCircleAndArrowScale(canvas, mBFPCircleScalingFactor, mBFPInnerCircleScalingFactor);
                break;
            case STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE:
                drawBeforeProgressCircleToLine(canvas, mBFPCircleToLineFactor);
                break;
            default:
        }
    }

    private void drawBeforeProgressCircleToLine(Canvas canvas, float percentTime) {
        mBaseLinePaint.setColor(mLoadingLineColor);
        //update path animation
        updateCircleToLinePath(mBaseLinePath, mCircleDiameter, percentTime);
        //offset the path update circle center
        mBaseLinePath.offset(mCircleRectF.left, mCircleRectF.top);
        canvas.drawPath(mBaseLinePath, mBaseLinePaint);
        mBaseLinePath.computeBounds(mBaseLineRectF, false);

        //update arrow to center
        //mArrowRectF 只是[0,0,width,height] , mCircleRectF是已经有left和top
        //通过mCircleRectF来定位mArrowRectF的中心
        mLastArrowOffsetX = (int) (mCircleRectF.centerX() - mArrowRectF.centerX());
        mLastArrowOffsetY = (int) (mCircleRectF.centerY() - mArrowRectF.centerY());

        //圆心加箭头底部
        int arrowBottom = (int) (mLastArrowOffsetY + mArrowRectF.bottom);

        if (mBaseLineRectF.bottom <= arrowBottom) {
            //变line后底部变小,我们也移动arrow
            mLastArrowOffsetY += mBaseLineRectF.bottom - arrowBottom;
        }
        drawArrowTrans(canvas, mLastArrowOffsetX, mLastArrowOffsetY, 0);
    }

    private void drawArrowTrans(Canvas canvas, int offsetX, int offsetY, int rotateAngle) {
        canvas.save();
        if (mArrowRotateMatrix == null) {
            mArrowRotateMatrix = new Matrix();
        } else {
            mArrowRotateMatrix.reset();
        }
        mDefaultPaint.setColor(mArrowColor);
        canvas.translate(offsetX, offsetY);
        if (rotateAngle != 0) {
            //失败箭头旋转
            mArrowRotateMatrix.postRotate(rotateAngle, mArrowRectF.centerX(), mArrowRectF.bottom);
            canvas.concat(mArrowRotateMatrix);
        }
        canvas.drawPath(mArrowPath, mDefaultPaint);
        canvas.restore();
    }

    private void updateCircleToLinePath(Path linePath, int circleDiameter, float percentTime) {
        //calculate every stage percent
        int index = 0;
        float adjustNormalizedTime = 0;
        if (percentTime <= CIRCLE_TO_LINE_SEASONS[1]) {
            adjustNormalizedTime = percentTime / CIRCLE_TO_LINE_SEASONS[1];
        } else if (percentTime < CIRCLE_TO_LINE_SEASONS[2]) {
            index = 1;
            adjustNormalizedTime = (percentTime - CIRCLE_TO_LINE_SEASONS[1]) / (CIRCLE_TO_LINE_SEASONS[2] - CIRCLE_TO_LINE_SEASONS[1]);
        } else {
            index = 2;
            adjustNormalizedTime = (percentTime - CIRCLE_TO_LINE_SEASONS[2]) / (CIRCLE_TO_LINE_SEASONS[3] - CIRCLE_TO_LINE_SEASONS[2]);
        }

        Log.d("liuyuzhe", "updateCircleToLinePath: "+adjustNormalizedTime);

        //the path bounds width
        float boundWidthPercent = ((CIRCLE_TO_LINE_WIDTH_FACTOR[index + 1] - CIRCLE_TO_LINE_WIDTH_FACTOR[index])
                * adjustNormalizedTime + CIRCLE_TO_LINE_WIDTH_FACTOR[index]);
        int boundWidth = (int) (boundWidthPercent * circleDiameter);

        int adjustBoundWidth = boundWidth;
        if (percentTime <= CIRCLE_TO_LINE_SEASONS[1]) {
            adjustBoundWidth = (int) (boundWidth * adjustNormalizedTime);
        }

        //the path bounds height
        float boundHeightPercent = (CIRCLE_TO_LINE_HEIGHT_FACTOR[index + 1] - CIRCLE_TO_LINE_HEIGHT_FACTOR[index]) * adjustNormalizedTime + CIRCLE_TO_LINE_HEIGHT_FACTOR[index];
        int boundHeight = (int) (boundHeightPercent * circleDiameter);

        //calculate the four points
        float firstControlX = ((CIRCLE_TO_LINE_FST_CON_X_FACTOR[index + 1] - CIRCLE_TO_LINE_FST_CON_X_FACTOR[index])
                * adjustNormalizedTime + CIRCLE_TO_LINE_FST_CON_X_FACTOR[index]) * circleDiameter;
        float firstControlY = ((CIRCLE_TO_LINE_FST_CON_Y_FACTOR[index + 1] - CIRCLE_TO_LINE_FST_CON_Y_FACTOR[index])
                * adjustNormalizedTime + CIRCLE_TO_LINE_FST_CON_Y_FACTOR[index]) * circleDiameter;
        float secondControlX = ((CIRCLE_TO_LINE_SEC_CON_X_FACTOR[index + 1] - CIRCLE_TO_LINE_SEC_CON_X_FACTOR[index])
                * adjustNormalizedTime + CIRCLE_TO_LINE_SEC_CON_X_FACTOR[index]) * circleDiameter;
        float secondControlY = ((CIRCLE_TO_LINE_SEC_CON_Y_FACTOR[index + 1] - CIRCLE_TO_LINE_SEC_CON_Y_FACTOR[index])
                * adjustNormalizedTime + CIRCLE_TO_LINE_SEC_CON_Y_FACTOR[index]) * circleDiameter;

        linePath.reset();
        //left line
        linePath.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, adjustBoundWidth / 2, boundHeight);
        //right line
        linePath.cubicTo(adjustBoundWidth - secondControlX, secondControlY, adjustBoundWidth - firstControlX, firstControlY, adjustBoundWidth, 0);
        //offset line
        float offsetX = (circleDiameter - adjustBoundWidth) / 2;
        float offsetY = (circleDiameter - boundHeight) / 2;
        linePath.offset(offsetX, offsetY);
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

        if (mDegress != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(mDegress, mArrowRectF.centerX(), mArrowRectF.centerY());
            canvas.concat(matrix);
        }
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

        // update path RectF
        if (mArrowRectF == null) {
            mArrowRectF = new RectF();
        }
        mArrowPath.computeBounds(mArrowRectF, true);
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
                mCurrentState = STATE_BEFORE_PROGRESS_INNER_CIRCLE_SCALE;
            }
        });
        mAnimatorList.add(mBFPInnerCircleScaleAnimator);
        mBFPInnerCircleScaleAnimator.setStartDelay(BEFORE_PROGRESS_CIRCLE_SCALE_DURATION / 2);
        mBFPInnerCircleScaleAnimator.setDuration(BEFORE_PROGRESS_CIRCLE_SCALE_DURATION / 2);

        //circle to line
        mBFPCircleToLineAnimator = ValueAnimator.ofFloat(0, 1f);
        mBFPCircleToLineAnimator.addUpdateListener(animation -> {
            mBFPCircleToLineFactor = (float) animation.getAnimatedValue();
            invalidate();
        });
        mBFPCircleToLineAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mCurrentState = STATE_BEFORE_PROGRESS_CIRCLE_TO_LINE;
            }
        });
        mBFPCircleToLineAnimator.setDuration(BEFORE_PROGRESS_CIRCLE_TO_LINE_DURATION);
        mAnimatorList.add(mBFPCircleToLineAnimator);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playSequentially(mBFPCircleScaleAnimator, mBFPCircleToLineAnimator);
        animatorSet.playTogether(mBFPInnerCircleScaleAnimator);
        animatorSet.setInterpolator(new LinearInterpolator());
        mAnimatorList.add(animatorSet);
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

    public void setTestDegree(String degress) {
        mDegress = Float.parseFloat(degress);
    }

}
