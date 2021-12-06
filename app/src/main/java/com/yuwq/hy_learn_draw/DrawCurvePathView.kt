package com.yuwq.hy_learn_draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 480个点的曲线
 * @author liuyuzhe
 */
class DrawCurvePathView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var pts: java.util.ArrayList<Float>
    private val paint = Paint()


    init {
        paint.color = Color.RED
        pts = ArrayList<Float>()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat());

        for (x in -240..240) {
            pts.add(x.toFloat())
            pts.add(d(x).toFloat())
            if (x == 100){
                println("x" +x)
                println("y" +d(x))
            }
        }
        canvas.drawPoints(pts.toFloatArray(), paint)
    }

    fun d(x: Int): Int {
        val y = -x * x / 200 + 100
        return y;
    }
}