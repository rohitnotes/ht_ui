package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.ht117.htui.BaseView

import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/9/17.
 */

class SquareCoundDownView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {

    private var deltaX: Int = 0
    private var deltaY: Int = 0
    private var oriented: Int = 0
    private var timeLeft: Int = 0

    private lateinit var bgPainter: Paint
    private lateinit var borderPainter: Paint

    private val handleCtrl: Runnable = Runnable {
        timeLeft -= interval
        invalidate()
    }

    init {
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.SquareCountDownView)
            timeLeft = duration

            bgPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            bgPainter.color = bgColor
            bgPainter.style = Paint.Style.FILL_AND_STROKE

            borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            borderPainter.color = bgColor
            borderPainter.style = Paint.Style.STROKE
        } catch (exp: Exception) {
            bgColor = Color.BLUE
            duration = Utils.DEF_TIME
            interval = Utils.SECOND / 10
            oriented = 1
        } finally {
            if (array != null) {
                array.recycle()
            }
        }
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

        val ratio = timeLeft * 1.0 / duration
        if (oriented == 1) {
            deltaX = (width * (1 - ratio)).toInt()
        } else {
            deltaY = (height * (1 - ratio)).toInt()
        }

        canvas.drawRect(left.toFloat(), top.toFloat(), (left + width).toFloat(), (top + height).toFloat(), borderPainter)
        canvas.drawRect(left.toFloat(), (top + deltaY).toFloat(), (left + width - deltaX).toFloat(), (top + height).toFloat(), bgPainter)

        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval.toLong())
        }
    }
}

