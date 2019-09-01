package com.ht117.htui

import android.content.Context
import android.graphics.Rect

/**
 * Created by steve on 7/9/17.
 */

object Utils {

    const val DEF_TIME = 7
    const val DEF_TXT_SIZE = 25
    const val DEF_STROKE_WIDTH = 7
    const val SECOND = 1000
    const val TWO_PI = 360
    const val ZERO = 0
    const val PADDING = 5
    const val PERCENT = 100

    fun getContentFormat(context: Context, formatID: Int, vararg values: Any): String {
        val formula = context.getString(formatID)
        return java.lang.String.format(formula, *values)
    }

    fun getCircleBound(txtBound: Rect): Rect {
        val max = Math.max(txtBound.width(), txtBound.height())
        return Rect(0, 0, max, max)
    }
}
