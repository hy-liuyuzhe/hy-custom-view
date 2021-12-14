package com.yuwq.hy_learn_draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 贝塞尔曲线
 * @author liuyuzhe
 */
class QuadToPathView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var points: List<TouchPaint>? = null
    private lateinit var size: Coordinate.Size
    private val coordinate = Coordinate()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        paint.apply {
            color = Color.RED
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        size = Coordinate.Size(width = width.toFloat(), height = height.toFloat())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        coordinate.onDraw(canvas, size)
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat());

        points?.let { quadto(canvas, it) }
    }

    private fun quadto(canvas: Canvas, list: List<TouchPaint>) {
        val point0X = list[0].x
        val point0Y = list[0].y
        val point1X = list[1].x
        val point1Y = list[1].y
        val point2X = list[2].x
        val point2Y = list[2].y

        canvas.drawPath(Path().apply {
            this.moveTo(point0X, point0Y)
            this.quadTo(point1X, point1Y, point2X, point2Y)
        }, paint)
        val path = Path().also { it.moveTo(point0X, point0Y) }
        path.lineTo(point1X, point1Y)
        path.lineTo(point2X, point2Y)
        canvas.drawPath(path, paint)
        canvas.drawPoints(
            floatArrayOf(point0X, point0Y, point1X, point1Y, point2X, point2Y),
            Paint().apply {
                strokeWidth = 13f
                style = Paint.Style.FILL
                strokeCap = Paint.Cap.ROUND
                color = Color.YELLOW
            })
    }

    fun setQuadToList(points: List<TouchPaint>) {
        this.points = points
        invalidate()
    }

}