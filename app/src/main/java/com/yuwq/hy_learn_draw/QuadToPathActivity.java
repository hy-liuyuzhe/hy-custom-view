package com.yuwq.hy_learn_draw;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liuyuzhe
 */
public class QuadToPathActivity extends AppCompatActivity {


    private ArrayList<TouchPaint> mTouchPaints;
    private QuadToPathView mQuadToPathView;
    private int lastIndex = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadto_path);


        mQuadToPathView = findViewById(R.id.quadToView);
        mTouchPaints = new ArrayList<>();
        mTouchPaints.add(new TouchPaint(0, 0));
        mTouchPaints.add(new TouchPaint(0,80));
        mTouchPaints.add(new TouchPaint(70, 150));
        mTouchPaints.add(new TouchPaint(150, 150));
        mQuadToPathView.setQuadToList(mTouchPaints);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        mQuadToPathView.setOnTouchListener((v, event) -> {
            TouchPaint point = new TouchPaint();
            point.set(event.getX() - widthPixels / 2, event.getY() - heightPixels / 2);
            Log.d("liuyuzhe", "onTouch.x: " + point.x);
            Log.d("liuyuzhe", "onTouch.y: " + point.y);
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                update(point);
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                lastIndex = -1;
            }
            return true;
        });
    }

    private void update(TouchPaint point) {
        for (int i = 0; i < mTouchPaints.size(); i++) {
            TouchPaint touchPaint = mTouchPaints.get(i);
            if (lastIndex >= 0) {
                updatePoint(lastIndex, point);
            } else if (touchPaint.inRange(point)) {
                lastIndex = i;
                updatePoint(lastIndex, point);
            }
        }
    }

    private void updatePoint(int i, TouchPaint point) {
        mTouchPaints.set(i, point);
        Log.d("liuyuzhe", "updatePoint: " + Arrays.toString(mTouchPaints.toArray()));
        mQuadToPathView.setQuadToList(mTouchPaints);
    }

}
