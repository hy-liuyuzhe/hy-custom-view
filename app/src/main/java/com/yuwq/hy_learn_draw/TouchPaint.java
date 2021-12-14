package com.yuwq.hy_learn_draw;

import android.util.Log;

/**
 * @author liuyuzhe
 */
public class TouchPaint {

    public float x;
    public float y;

    public TouchPaint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public TouchPaint() {
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean inRange(TouchPaint point) {
        Log.d("liuyuzhe", "new point: "+point);
        Log.d("liuyuzhe", "origin point: "+this);
        return Math.abs(point.x - x) < 30 && Math.abs(point.y - y) < 30;
    }

    @Override
    public String toString() {
        return "TouchPaint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
