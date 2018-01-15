package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.ht117.htui.BaseView

import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/9/17.
 */

class CircleCountDownView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {

    private var timeLeft: Double = 0.toDouble()
    private var startDeg: Int = 0
    private var degree: Double = 0.toDouble()
    private lateinit var content: String

    private var bgPainter: Paint
    private var txtPainter: Paint

    private val handleCtrl = Runnable {
        timeLeft -= interval.toDouble()
        degree = timeLeft / duration * Utils.TWO_PI

        content = if (timeLeft >= Utils.ZERO) Utils.getContentFormat(getContext(), R.string.format_one_decimal, timeLeft / Utils.SECOND) else Utils.ZERO.toString()
        invalidate()
    }

    init {
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.CircleCountDownView)
            timeLeft = duration.toDouble()
            startDeg = Utils.TWO_PI / 4 * 3
            degree = Utils.TWO_PI.toDouble()
        } catch (exp: Exception) {
            bgColor = Color.BLUE
            txtColor = Color.WHITE
            duration = Utils.DEF_TIME
            txtSize = Utils.DEF_TXT_SIZE
            interval = Utils.SECOND / 10
        } finally {
            if (array != null) {
                array.recycle()
            }
            bgPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            bgPainter.style = Paint.Style.FILL_AND_STROKE
            bgPainter.color = bgColor

            txtPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            txtPainter.style = Paint.Style.STROKE
            txtPainter.color = txtColor
            txtPainter.textSize = txtSize.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bound = Rect()
        content = Utils.PERCENT.toString()
        txtPainter.getTextBounds(content, 0, content.length, bound)
        val max = Math.max(bound.height(), bound.width()) * 2
        setMeasuredDimension(max, max)
    }

    override fun drawView(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        val width = width
        val height = height

        val bound = RectF((left + Utils.PADDING).toFloat(), (top + Utils.PADDING).toFloat(), (left + width - Utils.PADDING).toFloat(), (top + height - Utils.PADDING).toFloat())

        val boundTxt = Rect()
        txtPainter.getTextBounds(content, 0, content.length, boundTxt)

        if (degree >= Utils.ZERO) {
            canvas.drawArc(bound, startDeg.toFloat(), Utils.TWO_PI.toFloat(), false, txtPainter)
            canvas.drawArc(bound, startDeg.toFloat(), -degree.toFloat(), true, bgPainter)
        } else {
            canvas.drawArc(bound, Utils.ZERO.toFloat(), Utils.TWO_PI.toFloat(), false, txtPainter)
        }

        canvas.drawText(content, bound.centerX() - boundTxt.width() / 2, bound.centerY() + boundTxt.height() / 2, txtPainter)

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
