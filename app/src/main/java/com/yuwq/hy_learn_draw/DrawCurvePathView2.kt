package com.yuwq.hy_learn_draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @author liuyuzhe
 */
class DrawCurvePathView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val TAG = "DrawCurvePathView2"

    private var pts: java.util.ArrayList<Float>
    private val paint = Paint()
    private val coordinate = Coordinate()
    lateinit var size: Coordinate.Size


    init {
        paint.apply {
            color = Color.RED
            strokeWidth = 3f
            style = Paint.Style.FILL
        }
        pts = ArrayList()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: ")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = Coordinate.Size(width = width.toFloat(), height = height.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coordinate.onDraw(canvas, size)
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat());

        for (x in -240..240 step 10) {
            println("x" + x)
            pts.add(x.toFloat())
            pts.add(d(x).toFloat())
        }
        canvas.drawPoints(pts.toFloatArray(), paint)
    }

    fun d(x: Int): Int {
        val y = -x * x / 200 + 100
        return y;
    }
}