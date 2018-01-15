package com.ht117.htui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

/**
 * Created by pqhy on 1/15/2018.
 */
abstract class BaseView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var bgColor: Int = 0
    var duration: Int = 0
    var txtSize: Float = 0f
    var txtColor: Int = 0
    var interval: Int = 0
    var strokeWidth: Int = 0

    init {
        var base: TypedArray? = null
        try {
            base = context!!.obtainStyledAttributes(attrs, R.styleable.BaseView)
            bgColor = base.getColor(R.styleable.BaseView_ht_bgColor, Color.WHITE)
            duration = base.getInt(R.styleable.BaseView_ht_duration, Utils.DEF_TIME * Utils.SECOND)
            txtSize = base.getDimension(R.styleable.BaseView_ht_txtSize, Utils.DEF_TXT_SIZE)
            txtColor = base.getColor(R.styleable.BaseView_ht_txtColor, Color.BLACK)
            interval = base.getInt(R.styleable.BaseView_ht_interval, Utils.SECOND / 10)
            strokeWidth = base.getInt(R.styleable.BaseView_ht_strokeWidth, Utils.DEF_STROKE_WIDTH)
        } catch (exp: Exception) {
            bgColor = Color.WHITE
            duration = Utils.DEF_TIME * Utils.SECOND
            txtSize = Utils.DEF_TXT_SIZE
            txtColor = Color.BLACK
            interval = Utils.SECOND / 10
            strokeWidth = Utils.DEF_STROKE_WIDTH
        } finally {
            if (base != null) {
                base.recycle()
            }
        }
    }

    abstract fun drawView(canvas: Canvas)

    override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        drawView(canvas)
    }
}