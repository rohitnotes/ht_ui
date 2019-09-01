package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable

import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/11/17.
 */

class NumberLoadingView(context: Context, @Nullable attrs: AttributeSet) : View(context, attrs) {

    private var txtSize: Int = 0
    private var txtColor: Int = 0
    private var loadedColor: Int = 0
    private var unloadedColor: Int = 0
    private var progress: Int = 0
    private var strokeWidth: Int = 0
    private var content: String? = null

    private var loadedBarPainter: Paint? = null
    private var unloadedBarPainter: Paint? = null
    private var txtPainter: Paint? = null

    init {
        initView(attrs)
    }

    fun getProgress(): Int {
        return progress
    }

    fun setProgress(progress: Int) {
        if (progress < Utils.PERCENT) {
            this.progress = progress
        } else {
            this.progress = Utils.PERCENT
        }
        content = Utils.getContentFormat(context, R.string.format_percent, this.progress) + "%"
        invalidate()
    }

    private fun initView(attrs: AttributeSet) {
        var array: TypedArray? = null

        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.NumberLoadingView)
            txtSize = array!!.getDimensionPixelSize(R.styleable.NumberLoadingView_nlv_txtSize, Utils.DEF_TXT_SIZE)
            txtColor = array.getColor(R.styleable.NumberLoadingView_nlv_txtColor, Color.RED)
            loadedColor = array.getColor(R.styleable.NumberLoadingView_nlv_loadedColor, Color.GREEN)
            unloadedColor = array.getColor(R.styleable.NumberLoadingView_nlv_unloadedColor, Color.WHITE)
            strokeWidth = array.getDimensionPixelSize(R.styleable.NumberLoadingView_nlv_strokeWidth, Utils.DEF_STROKE_WIDTH)
            progress = 0
            content = Utils.getContentFormat(context, R.string.format_percent, progress) + "%"
        } finally {
            array?.recycle()
        }
        loadedBarPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        loadedBarPainter!!.color = loadedColor
        loadedBarPainter!!.style = Paint.Style.FILL_AND_STROKE

        unloadedBarPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        unloadedBarPainter!!.color = unloadedColor
        unloadedBarPainter!!.style = Paint.Style.FILL_AND_STROKE

        txtPainter = Paint(Paint.ANTI_ALIAS_FLAG)
        txtPainter!!.textSize = txtSize.toFloat()
        txtPainter!!.color = txtColor
        txtPainter!!.style = Paint.Style.STROKE

    }

    fun reset() {
        progress = 0
        content = Utils.getContentFormat(context, R.string.format_percent, this.progress) + "%"
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bound = Rect()
        if (content == null) {
            content = Utils.ZERO.toString()
        }
        txtPainter!!.getTextBounds(content, 0, content!!.length, bound)
        val max = Math.max(height, bound.height())

        setMeasuredDimension(widthMeasureSpec, max)
    }

    override fun onDraw(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        val width = width
        val height = height

        val bound = Rect()
        txtPainter!!.getTextBounds(content, 0, content!!.length, bound)

        if (progress == Utils.PERCENT) {
            canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + width / 2 - Utils.PADDING - bound.width() / 2).toFloat(), (top + height / 2).toFloat(), loadedBarPainter!!)
            canvas.drawText(content!!, (left + width / 2 - Utils.PADDING - bound.width() / 2).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter!!)
            canvas.drawLine((left + width / 2 + Utils.PADDING + bound.width() / 2).toFloat(), (top + height / 2).toFloat(), (left + width).toFloat(), (top + height / 2).toFloat(), loadedBarPainter!!)
        } else {
            val loaded = width * progress / Utils.PERCENT

            if (loaded + Utils.PADDING * 2 + bound.width() <= width) {
                canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + loaded).toFloat(), (top + height / 2).toFloat(), loadedBarPainter!!)
                canvas.drawText(content!!, (left + loaded + Utils.PADDING).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter!!)
                canvas.drawLine((left + loaded + 2 * Utils.PADDING + bound.width()).toFloat(), (top + height / 2).toFloat(), (left + width).toFloat(), (top + height / 2).toFloat(), unloadedBarPainter!!)
            } else {
                canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + width - 2 * Utils.PADDING - bound.width()).toFloat(), (top + height / 2).toFloat(), loadedBarPainter!!)
                canvas.drawText(content!!, (left + width - Utils.PADDING - bound.width()).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter!!)
            }
        }
    }
}
