package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import com.ht117.htui.BaseView
import com.ht117.htui.R

/**
 * Created by steve on 7/9/17.
 */

class SquareCountDownView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {

    private var deltaX: Int = 0
    private var deltaY: Int = 0
    private var oriented: Int = 0
    private var timeLeft: Int = 0

    private var bgPainter: Paint
    private var borderPainter: Paint

    private val handleCtrl: Runnable = Runnable {
        timeLeft -= interval
        invalidate()
    }

    init {
        Log.d("Debug", "Timeleft: " + timeLeft + " - Duration: " + duration)
        timeLeft = duration

        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.SquareCountDownView)
            oriented = array.getInt(R.styleable.SquareCountDownView_ht_oriented, 1)
        } finally {
            if (array != null) {
                array.recycle()
            }
        }

        bgPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPainter.color = bgColor
        bgPainter.style = Paint.Style.FILL_AND_STROKE

        borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPainter.color = bgColor
        borderPainter.style = Paint.Style.STROKE
    }

    fun start() {
        timeLeft = duration
        deltaX = 0
        deltaY = 0
        invalidate()
    }

    override fun drawView(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        Log.d("Debug", "Timeleft: " + timeLeft + " - Duration: " + duration)
        val ratio = timeLeft * 1.0 / duration
        if (oriented == 1) {
            deltaX = (width * (1 - ratio)).toInt()
        } else {
            deltaY = (height * (1 - ratio)).toInt()
        }

        canvas.drawRect(left.toFloat(), top.toFloat(), (left + width).toFloat(), (top + height).toFloat(), borderPainter)
        canvas.drawRect(left.toFloat(), (top + deltaY).toFloat(), (left + width - deltaX).toFloat(), (top + height).toFloat(), bgPainter)

        Log.d("Debug", "Call me square count down view " + ratio)
        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval.toLong())
        }
    }
}

