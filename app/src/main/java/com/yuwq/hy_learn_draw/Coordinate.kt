package com.yuwq.hy_learn_draw

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

/**
 * @author liuyuzhe
 */
class Coordinate {

    val gridPath = Path()
    val gridPaint = Paint()
    val step = 20
    val strokeWidth = 0.5
    val axisColor = Color.BLUE
    val gridColor = Color.GRAY


    fun onDraw(canvas: Canvas, size: Size) {
        canvas.save()
        canvas.translate(size.width / 2, size.height / 2)
        drawGridLine(canvas, size)
        drawAxis(canvas, size)
        drawText(canvas,size)
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, size: Size) {
//        canvas.save()
//        for (i in 0..(size.width/2/step).toInt()){
//            if (step<30 && i.)
//        }
//
//
//        canvas.restore()
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
            drawLine(zero,size.height/2,0-17f,size.height/2-20,gridPaint)
            drawLine(zero,size.height/2,0+17f,size.height/2-20,gridPaint)
            drawLine(size.width/2,zero,size.width/2-20,0-17f,gridPaint);
            drawLine(size.width/2,zero,size.width/2-20,0+17f,gridPaint);
        };
    }

    /**
    for (int i = 0; i < size.height / 2 / step; i++) {
    _gridPath.moveTo(-size.width / 2,step * i );
    _gridPath.relativeLineTo(size.width,0 );

    _gridPath.moveTo(-size.width / 2,-step * i,  );
    _gridPath.relativeLineTo(size.width,0 );
    }

     */
    private fun drawGridLine(canvas: Canvas, size: Size) {
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