package com.ht117.htui;

import android.content.Context;
import android.graphics.Rect;

/**
 * Created by steve on 7/9/17.
 */

public class Utils {

    public static final int DEF_TIME = 7;
    public static final int DEF_TXT_SIZE = 25;
    public static final int DEF_STROKE_WIDTH = 7;
    public static final int SECOND = 1000;
    public static final int TWO_PI = 360;
    public static final int ZERO = 0;
    public static final int PADDING = 5;
    public static final int PERCENT = 100;

    public static String getContentFormat(Context context, int formatID, Object... values) {
        String formula = context.getString(formatID);
        return String.format(formula, values);
    }

    public static Rect getCircleBound(Rect txtBound) {
        int max = Math.max(txtBound.width(), txtBound.height());
        return new Rect(0, 0, max, max);
    }
}
