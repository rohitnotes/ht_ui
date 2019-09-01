package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/9/17.
 */

class CircleLoadingView(context: Context, @Nullable attrs: AttributeSet) : View(context, attrs) {

    private var coLeft: Int = 0
    private var coRight: Int = 0
    private var coTop: Int = 0
    private var coBot: Int = 0
    private var interval: Int = 0
    private var strokeWidth: Int = 0
    private var startDeg: Int = 0
    private val SWEEP = 90

    private var painterLeft: Paint? = null
    private var painterTop: Paint? = null
    private var painterRight: Paint? = null
    private var painterBot: Paint? = null

    private val handleCtrl = Runnable {
        val temp = coLeft
        coLeft = coTop
        coTop = coRight
        coRight = coBot
        coBot = temp
        startDeg = (startDeg + 1) % Utils.TWO_PI

        painterLeft!!.color = coLeft
        painterTop!!.color = coTop
        painterRight!!.color = coRight
        painterBot!!.color = coBot
        invalidate()
    }

    init {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet) {
        var array: TypedArray? = null
        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.CircleLoadingView)

            coLeft = array!!.getColor(R.styleable.CircleLoadingView_clv_colorLeft, Color.BLUE)
            coTop = array.getColor(R.styleable.CircleLoadingView_clv_colorTop, Color.YELLOW)
            coRight = array.getColor(R.styleable.CircleLoadingView_clv_colorRight, Color.GREEN)
            coBot = array.getColor(R.styleable.CircleLoadingView_clv_colorBot, Color.RED)
            interval = array.getInt(R.styleable.CircleLoadingView_clv_interval, Utils.SECOND / 10)
            strokeWidth = array.getInt(R.styleable.CircleLoadingView_clv_strokeWidth, Utils.DEF_STROKE_WIDTH)
            startDeg = Utils.ZERO
        } finally {
            array?.recycle()
        }

        painterLeft = Paint(Paint.ANTI_ALIAS_FLAG)
        painterLeft!!.style = Paint.Style.STROKE
        painterLeft!!.strokeWidth = strokeWidth.toFloat()
        painterLeft!!.color = coLeft

        painterRight = Paint(Paint.ANTI_ALIAS_FLAG)
        painterRight!!.style = Paint.Style.STROKE
        painterRight!!.strokeWidth = strokeWidth.toFloat()
        painterRight!!.color = coRight

        painterTop = Paint(Paint.ANTI_ALIAS_FLAG)
        painterTop!!.style = Paint.Style.STROKE
        painterTop!!.strokeWidth = strokeWidth.toFloat()
        painterTop!!.color = coTop

        painterBot = Paint(Paint.ANTI_ALIAS_FLAG)
        painterBot!!.style = Paint.Style.STROKE
        painterBot!!.strokeWidth = strokeWidth.toFloat()
        painterBot!!.color = coBot

    }

    override fun onDraw(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        val width = width
        val height = height

        val padding = Utils.PADDING
        val rect = RectF((left + padding).toFloat(), (top + padding).toFloat(), (left + width - padding).toFloat(), (top + height - padding).toFloat())

        canvas.drawArc(rect, startDeg.toFloat(), SWEEP.toFloat(), false, painterLeft!!)
        canvas.drawArc(rect, (startDeg + SWEEP).toFloat(), SWEEP.toFloat(), false, painterTop!!)
        canvas.drawArc(rect, (startDeg + SWEEP * 2).toFloat(), SWEEP.toFloat(), false, painterRight!!)
        canvas.drawArc(rect, (startDeg + SWEEP * 3).toFloat(), SWEEP.toFloat(), false, painterBot!!)
        handler.postDelayed(handleCtrl, interval.toLong())
    }
}
