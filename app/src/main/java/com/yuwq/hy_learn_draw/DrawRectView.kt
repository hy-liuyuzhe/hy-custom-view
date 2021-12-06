package com.yuwq.hy_learn_draw

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * 480个点的曲线
 * @author liuyuzhe
 */
class DrawRectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var size: Coordinate.Size
    val coordinate = Coordinate()

    private var pts: java.util.ArrayList<Float>
    private val paint = Paint()


    init {
        paint.color = Color.RED
        pts = ArrayList<Float>()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = Coordinate.Size(width = width.toFloat(), height = height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coordinate.onDraw(canvas = canvas, size = size)
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat());
        canvas.drawRect(Rect(-80, -80, 80, 80), paint.apply {
            color = Color.BLUE
            strokeWidth = 1.5f
        })
        canvas.drawRect(-150f, -150f, -80f, -80f, paint.apply {
            color = Color.BLUE
            strokeWidth = 1.5f
        })

        canvas.drawRoundRect(
            RectF(-150f, 150f, -80f, 80f),
            10f,
            10f,
            paint.apply { color = Color.GREEN })

    }

    fun d(x: Int): Int {
        val y = -x * x / 200 + 100
        return y;
    }
}