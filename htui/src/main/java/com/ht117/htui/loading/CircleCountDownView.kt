package com.ht117.htui.loading

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/9/17.
 */

class CircleCountDownView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var bgColor: Int = 0
    private var txtColor: Int = 0
    private var duration: Int = 0
    private var interval: Int = 0
    private var timeLeft: Double = 0.toDouble()
    private var txtSize: Float = 0.toFloat()
    private var startDeg: Int = 0
    private var degree: Double = 0.toDouble()
    private var content: String? = null

    private var bgPainter: Paint? = null
    private var txtPainter: Paint? = null

    private val handleCtrl = Runnable {
        timeLeft -= interval.toDouble()
        degree = timeLeft / duration * Utils.TWO_PI

        content = if (timeLeft >= Utils.ZERO) {
            Utils.getContentFormat(getContext(), R.string.format_one_decimal, timeLeft / Utils.SECOND)
        } else {
            Utils.ZERO.toString()
        }

        invalidate()
    }

    init {
        initView(attrs)
    }

    fun setDuration(duration: Int) {
        this.duration = duration * Utils.SECOND
    }

    fun getDuration(): Int {
        return this.duration
    }

    private fun initView(attrs: AttributeSet) {
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.CircleCountDownView)
            bgColor = array!!.getColor(R.styleable.CircleCountDownView_ccdv_background, Color.BLUE)
            txtColor = array.getColor(R.styleable.CircleCountDownView_ccdv_textcolor, Color.WHITE)
            duration = array.getInt(R.styleable.CircleCountDownView_ccdv_duration, Utils.DEF_TIME)
            txtSize = array.getDimensionPixelSize(R.styleable.CircleCountDownView_ccdv_txtSize, Utils.DEF_TXT_SIZE).toFloat()
            interval = array.getInt(R.styleable.CircleCountDownView_ccdv_interval, Utils.SECOND / 10)
            timeLeft = duration.toDouble()
            startDeg = Utils.TWO_PI / 4 * 3
            degree = Utils.TWO_PI.toDouble()
        } finally {
            array?.recycle()
        }
        bgPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPainter!!.style = Paint.Style.FILL_AND_STROKE
        bgPainter!!.color = bgColor

        txtPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        txtPainter!!.style = Paint.Style.STROKE
        txtPainter!!.color = txtColor
        txtPainter!!.textSize = txtSize
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bound = Rect()
        if (content == null) {
            content = Utils.PERCENT.toString()
        }
        txtPainter!!.getTextBounds(content, 0, content!!.length, bound)
        val max = Math.max(bound.height(), bound.width()) * 2
        setMeasuredDimension(max, max)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        val width = width
        val height = height

        val bound = RectF((left + Utils.PADDING).toFloat(), (top + Utils.PADDING).toFloat(), (left + width - Utils.PADDING).toFloat(), (top + height - Utils.PADDING).toFloat())

        val boundTxt = Rect()
        txtPainter!!.getTextBounds(content, 0, content!!.length, boundTxt)

        if (degree >= Utils.ZERO) {
            canvas.drawArc(bound, startDeg.toFloat(), Utils.TWO_PI.toFloat(), false, txtPainter!!)
            canvas.drawArc(bound, startDeg.toFloat(), -degree.toFloat(), true, bgPainter!!)
        } else {
            canvas.drawArc(bound, Utils.ZERO.toFloat(), Utils.TWO_PI.toFloat(), false, txtPainter!!)
        }

        canvas.drawText(content!!, bound.centerX() - boundTxt.width() / 2, bound.centerY() + boundTxt.height() / 2, txtPainter!!)

        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval.toLong())
        }
    }

    fun start() {
        timeLeft = duration.toDouble()
        degree = Utils.TWO_PI.toDouble()
        invalidate()
    }
}
