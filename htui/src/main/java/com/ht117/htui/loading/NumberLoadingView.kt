package com.ht117.htui.loading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.ht117.htui.BaseView
import com.ht117.htui.R
import com.ht117.htui.Utils

/**
 * Created by steve on 7/11/17.
 */

class NumberLoadingView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {

    private var loadedColor: Int = 0
    private var unloadedColor: Int = 0
    private var progress: Int = 0
    private lateinit var content: String

    private lateinit var loadedBarPainter: Paint
    private lateinit var unloadedBarPainter: Paint
    private lateinit var txtPainter: Paint

    init {
        var array: TypedArray? = null

        try {
            array = context.obtainStyledAttributes(attrs, R.styleable.NumberLoadingView)
            loadedColor = array.getColor(R.styleable.NumberLoadingView_ht_loadedColor, Color.GREEN)
            unloadedColor = array.getColor(R.styleable.NumberLoadingView_ht_unloadedColor, Color.WHITE)
            content = Utils.getContentFormat(context, R.string.format_percent, progress) + "%"

            loadedBarPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            loadedBarPainter.color = loadedColor
            loadedBarPainter.style = Paint.Style.FILL_AND_STROKE

            unloadedBarPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            unloadedBarPainter.color = unloadedColor
            unloadedBarPainter.style = Paint.Style.FILL_AND_STROKE

            txtPainter = Paint(Paint.ANTI_ALIAS_FLAG)
            txtPainter.textSize = txtSize
            txtPainter.color = txtColor
            txtPainter.style = Paint.Style.STROKE
        } catch (exp: Exception) {
            txtSize = Utils.DEF_TXT_SIZE
            txtColor = Color.RED
            loadedColor = Color.GREEN
            unloadedColor = Color.WHITE
            strokeWidth = Utils.DEF_STROKE_WIDTH
        } finally {
            if (array != null) {
                array.recycle()
            }
        }
    }

    fun getProgress() : Int = progress

    fun setProgress(value: Int) {
        progress = if (value <= Utils.PERCENT) value else Utils.PERCENT
        content = Utils.getContentFormat(context, R.string.format_percent, progress) + "%"
        invalidate()
    }

    override fun drawView(canvas: Canvas) {
        val left = paddingLeft
        val top = paddingTop
        val width = width
        val height = height

        val bound = Rect()
        txtPainter.getTextBounds(content, 0, content.length, bound)

        if (progress == Utils.PERCENT) {
            canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + width / 2 - Utils.PADDING - bound.width() / 2).toFloat(), (top + height / 2).toFloat(), loadedBarPainter)
            canvas.drawText(content, (left + width / 2 - Utils.PADDING - bound.width() / 2).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter)
            canvas.drawLine((left + width / 2 + Utils.PADDING + bound.width() / 2).toFloat(), (top + height / 2).toFloat(), (left + width).toFloat(), (top + height / 2).toFloat(), loadedBarPainter)
        } else {
            val loaded = width * progress / Utils.PERCENT

            if (loaded + Utils.PADDING * 2 + bound.width() <= width) {
                canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + loaded).toFloat(), (top + height / 2).toFloat(), loadedBarPainter)
                canvas.drawText(content, (left + loaded + Utils.PADDING).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter)
                canvas.drawLine((left + loaded + 2 * Utils.PADDING + bound.width()).toFloat(), (top + height / 2).toFloat(), (left + width).toFloat(), (top + height / 2).toFloat(), unloadedBarPainter)
            } else {
                canvas.drawLine(left.toFloat(), (top + height / 2).toFloat(), (left + width - 2 * Utils.PADDING - bound.width()).toFloat(), (top + height / 2).toFloat(), loadedBarPainter)
                canvas.drawText(content, (left + width - Utils.PADDING - bound.width()).toFloat(), (top + height / 2 + bound.height() / 2).toFloat(), txtPainter)
            }
        }
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
        txtPainter.getTextBounds(content, 0, content.length, bound)
        val max = Math.max(height, bound.height())

        setMeasuredDimension(widthMeasureSpec, max)
    }
}
