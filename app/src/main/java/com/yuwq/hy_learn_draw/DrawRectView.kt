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

    private val paint = Paint()


    init {
        paint.color = Color.RED
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = Coordinate.Size(width = width.toFloat(), height = height.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coordinate.onDraw(canvas = canvas, size = size)
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat());
        rect(canvas)
    }

//    private fun drawPaint(canvas: Canvas) {
//        val colors = intArrayOf(
//            Color.parseColor("#F60C0C"),
//            Color.parseColor("#F3B913"),
//            Color.parseColor("#E7F716"),
//            Color.parseColor("#3DF30B"),
//            Color.parseColor("#0DF6EF"),
//            Color.parseColor("#0829FB"),
//            Color.parseColor("#B709F4"),
//        )
//        val positions = floatArrayOf(
//            (1.0 / 7).toFloat(),
//            (2.0 / 7).toFloat(), (3.0 / 7).toFloat(), (4.0 / 7).toFloat(),
//            (5.0 / 7).toFloat(), (6.0 / 7).toFloat(), 1.0F
//        )
//        val p = Paint()
//        p.shader = LinearGradient(
//            -size.width,
//            0f,
//            size.width,
//            0f,
//            colors,
//            positions,
//            Shader.TileMode.CLAMP
//        )
//
//
////        p.blendMode  = setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
//
//        canvas.drawPaint(p)
//    }

    private fun rect(canvas: Canvas) {
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
}