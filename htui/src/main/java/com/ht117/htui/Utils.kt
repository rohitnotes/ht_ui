package com.ht117.htui

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.view.WindowManager

/**
 * Created by steve on 7/9/17.
 */

object Utils {

    val DEF_TIME = 7
    val DEF_TXT_SIZE = 25f
    val DEF_STROKE_WIDTH = 7
    val SECOND = 1000
    val TWO_PI = 360
    val ZERO = 0
    val PADDING = 5
    val PERCENT = 100
    val DEF_RADIUS = 10
    val DEF_TXT_COLOR = Color.BLACK

    fun getContentFormat(context: Context, formatID: Int, vararg values: Any): String {
        val formula = context.getString(formatID)
        return String.format(formula, *values)
    }

    fun getCircleBound(txtBound: Rect): Rect {
        val max = Math.max(txtBound.width(), txtBound.height())
        return Rect(0, 0, max, max)
    }

    fun calculateScreenSize(context: Context) : Pair<Int, Int> {
        var dspManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var dsp = dspManager.defaultDisplay
        var size = Point()
        dsp.getSize(size)
        return Pair(size.x, size.y)
    }
}
