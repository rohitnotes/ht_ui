package com.ht117.htui.text

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.ht117.htui.BaseView
import com.ht117.htui.Utils

/**
 * Created by pqhy on 1/15/2018.
 */
class RoundText(context: Context?, attrs: AttributeSet?) : BaseView(context, attrs) {

    private var radius: Int = 0

    init {
        if (attrs == null) {
            txtSize = Utils.DEF_TXT_SIZE
            txtColor = Utils.DEF_TXT_COLOR
            radius = Utils.DEF_RADIUS
        } else {

        }
    }

    override fun drawView(canvas: Canvas) {

    }
}