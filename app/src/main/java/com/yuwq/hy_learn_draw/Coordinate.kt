package com.yuwq.hy_learn_draw

import android.graphics.*
import android.util.Log
import com.blankj.utilcode.util.SizeUtils

/**
 * @author liuyuzhe
 */
class Coordinate {

    private val textPaint: Paint = Paint()
    val gridPath = Path()
    val gridPaint = Paint()
    val step = 25
    val strokeWidth = 0.5
    val axisColor = Color.BLUE
    val gridColor = Color.GRAY

    fun onDraw(canvas: Canvas, size: Size) {
        canvas.save()
        canvas.translate(size.width / 2, size.height / 2)
        drawGridLine(canvas, size)
        drawAxis(canvas, size)
        drawText(canvas, size)
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, size: Size) {
        canvas.save()
        //y>0 文字
        for (i in 0..(size.height / 2 / step).toInt()) {
            val stepFloat = step.toFloat()
            if (i % 2 != 0|| i==0) {
                canvas.translate(0f, stepFloat)
                continue
            } else {
                val str = (i * step).toString()
                drawAxisText(canvas, str, false)
            }
            canvas.translate(0f, stepFloat)
        }
        canvas.restore()

        // y < 0 轴 文字
        canvas.save()
        for (i in 0..(size.height / 2 / step).toInt()) {
            val stepFloat = step.toFloat()
            if (i % 2 != 0|| i==0) {
                canvas.translate(0f, -stepFloat)
                continue
            } else {
                val str = (i * -step).toString()
                drawAxisText(canvas, str, false)
            }
            canvas.translate(0f, -stepFloat)
        }
        canvas.restore()

        //x>0轴 文字
        canvas.save()
        for (i in 0..(size.width / 2 / step).toInt()) {
            val stepFloat = step.toFloat()
            if (i % 2 != 0 || i==0) {
                canvas.translate(stepFloat, 0f)
                continue
            } else {
                val str = (i * step).toString()
                drawAxisText(canvas, str, true)
            }
            canvas.translate(stepFloat, 0f)
        }
        canvas.restore()
        //x<0轴 文字
        canvas.save()
        for (i in 0..(size.width / 2 / step).toInt()) {
            val stepFloat = -step.toFloat()
            if (i % 2 != 0 || i==0) {
                canvas.translate(stepFloat, 0f)
                continue
            } else {
                val str = (i * -step).toString()
                drawAxisText(canvas, str, true)
            }
            canvas.translate(stepFloat, 0f)
        }
        canvas.restore()
    }

    private fun drawAxisText(canvas: Canvas, str: String, x: Boolean) {
        textPaint.color = Color.GREEN
        textPaint.textSize = SizeUtils.sp2px(8f).toFloat()
        val rect = Rect()
        textPaint.getTextBounds(
            str,
            0,
            str.length,
            rect
        )
        val textH = rect.height()
        val textW = rect.width()
        Log.d("liuyuzhe", "textW: " + textW);
        canvas.save()
        if (x) {
            canvas.rotate(90f, 0.toFloat(), 0.toFloat())
            canvas.drawText(str, SizeUtils.dp2px(5f).toFloat(), (textH/2).toFloat(), textPaint)
        } else {
            canvas.rotate(90f, (-10).toFloat(), 0.toFloat())
            canvas.drawText(str, -textW.toFloat(), (textH / 2 + 5).toFloat(), textPaint)
        }
        canvas.restore()
    }

    private fun drawAxis(canvas: Canvas, size: Size) {
        gridPaint.let {
            it.color = Color.BLUE
            it.strokeWidth = 1.5f
            it.isAntiAlias = true
        }
        val zero = 0f
        with(canvas) {
            drawLine(-size.width / 2, zero, size.width / 2, zero, gridPaint)
            drawLine(zero, -size.height / 2, zero, size.height / 2, gridPaint)
            drawLine(zero, size.height / 2, 0 - 17f, size.height / 2 - 20, gridPaint)
            drawLine(zero, size.height / 2, 0 + 17f, size.height / 2 - 20, gridPaint)
            drawLine(size.width / 2, zero, size.width / 2 - 20, 0 - 17f, gridPaint);
            drawLine(size.width / 2, zero, size.width / 2 - 20, 0 + 17f, gridPaint);
        };
    }

    private fun drawGridLine(canvas: Canvas, size: Size) {
        gridPath.reset()
        gridPaint.apply {
            strokeWidth = 0.5f
            style = Paint.Style.STROKE
            color = Color.GRAY
        }

        //画竖线
        for (i in 0..(size.width / 2 / step).toInt()) {
            //画屏幕右半边的竖线
            gridPath.moveTo((step * i).toFloat(), -size.height / 2) //先移动到屏幕顶部
            gridPath.rLineTo(0f, size.height)//从屏幕顶部画线到屏幕下面
            //画屏幕左半边的竖线
            gridPath.moveTo((-step * i).toFloat(), -size.height / 2)
            gridPath.rLineTo(0f, size.height)
        }

        //画横线
        for (i in 0..(size.height / 2 / step).toInt()) {
            //移动到屏幕中线左边 0，0，从左到右画线
            gridPath.moveTo(-size.width / 2, (step * i).toFloat())
            gridPath.rLineTo(size.width, 0f)

            gridPath.moveTo(-size.width / 2, (-step * i).toFloat())
            gridPath.rLineTo(size.width, 0f)
        }
        canvas.drawPath(gridPath, gridPaint)
    }


    class Size(val width: Float, val height: Float)

}