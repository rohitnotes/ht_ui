package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/9/17.
 */

class CountDownView(context: Context, @Nullable attrs: AttributeSet) : View(context, attrs) {

    private var background: Int = 0
    private var duration: Int = 0
    private var interval: Int = 0
    private var deltaX: Int = 0
    private var deltaY: Int = 0
    private var oriented: Int = 0
    private var timeLeft: Int = 0

    private var bgPainter: Paint? = null
    private var borderPainter: Paint? = null

    private val handleCtrl: Runnable = Runnable {
        timeLeft -= interval
        invalidate()
    }

    init {
        initView(attrs)
    }

    fun setDuration(duration: Int) {
        this.duration = duration * Utils.SECOND
    }

    private fun initView(attrs: AttributeSet) {
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.CountDownView)
            background = array!!.getColor(R.styleable.CountDownView_cdv_background, Color.BLUE)
            duration = array.getInt(R.styleable.CountDownView_cdv_duration, Utils.DEF_TIME)
            interval = array.getInt(R.styleable.CountDownView_cdv_interval, Utils.SECOND / 10)
            oriented = array.getInt(R.styleable.CountDownView_cdv_oriented, 1)
            timeLeft = duration
        } finally {
            array?.recycle()
        }
        bgPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPainter!!.color = background
        bgPainter!!.style = Paint.Style.FILL_AND_STROKE

        borderPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPainter!!.color = background
        borderPainter!!.style = Paint.Style.STROKE
    }

    fun start() {
        timeLeft = duration
        deltaX = 0
        deltaY = 0
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop

        val ratio = timeLeft * 1.0 / duration
        if (oriented == 1) {
            deltaX = (width * (1 - ratio)).toInt()
        } else {
            deltaY = (height * (1 - ratio)).toInt()
        }

        canvas.drawRect(left.toFloat(), top.toFloat(), (left + width).toFloat(), (top + height).toFloat(), borderPainter!!)
        canvas.drawRect(left.toFloat(), (top + deltaY).toFloat(), (left + width - deltaX).toFloat(), (top + height).toFloat(), bgPainter!!)

        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval.toLong())
        }
    }
}
