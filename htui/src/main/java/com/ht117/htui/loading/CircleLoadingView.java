package com.ht117.htui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ht117.htui.R;
import com.ht117.htui.Utils;

/**
 * Created by steve on 7/9/17.
 */

public class CircleLoadingView extends View {

    private int coLeft;
    private int coRight;
    private int coTop;
    private int coBot;
    private int interval;
    private int strokeWidth;
    private int startDeg;
    private final int SWEEP = 90;

    private Paint painterLeft;
    private Paint painterTop;
    private Paint painterRight;
    private Paint painterBot;

    private Handler handler;

    public CircleLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        handler = new Handler();
    }

    private void initView(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);

            coLeft = array.getColor(R.styleable.CircleLoadingView_clv_colorLeft, Color.BLUE);
            coTop = array.getColor(R.styleable.CircleLoadingView_clv_colorTop, Color.YELLOW);
            coRight = array.getColor(R.styleable.CircleLoadingView_clv_colorRight, Color.GREEN);
            coBot = array.getColor(R.styleable.CircleLoadingView_clv_colorBot, Color.RED);
            interval = array.getInt(R.styleable.CircleLoadingView_clv_interval, Utils.SECOND / 10);
            strokeWidth = array.getInt(R.styleable.CircleLoadingView_clv_strokeWidth, Utils.DEF_STROKE_WIDTH);
            startDeg = Utils.ZERO;

            painterLeft = new Paint(Paint.ANTI_ALIAS_FLAG);
            painterLeft.setStyle(Paint.Style.STROKE);
            painterLeft.setStrokeWidth(strokeWidth);
            painterLeft.setColor(coLeft);

            painterRight = new Paint(Paint.ANTI_ALIAS_FLAG);
            painterRight.setStyle(Paint.Style.STROKE);
            painterRight.setStrokeWidth(strokeWidth);
            painterRight.setColor(coRight);

            painterTop = new Paint(Paint.ANTI_ALIAS_FLAG);
            painterTop.setStyle(Paint.Style.STROKE);
            painterTop.setStrokeWidth(strokeWidth);
            painterTop.setColor(coTop);

            painterBot = new Paint(Paint.ANTI_ALIAS_FLAG);
            painterBot.setStyle(Paint.Style.STROKE);
            painterBot.setStrokeWidth(strokeWidth);
            painterBot.setColor(coBot);

        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int width = getWidth();
        int height = getHeight();

        int padding = Utils.PADDING;
        RectF rect = new RectF(left + padding, top + padding, left + width - padding, top + height - padding);

        canvas.drawArc(rect, startDeg, SWEEP, false, painterLeft);
        canvas.drawArc(rect, startDeg + SWEEP, SWEEP, false, painterTop);
        canvas.drawArc(rect, startDeg + SWEEP * 2, SWEEP, false, painterRight);
        canvas.drawArc(rect, startDeg + SWEEP * 3, SWEEP, false, painterBot);
        handler.postDelayed(handleCtrl, interval);
    }

    private final Runnable handleCtrl = new Runnable() {
        @Override
        public void run() {
            int temp = coLeft;
            coLeft = coTop;
            coTop = coRight;
            coRight = coBot;
            coBot = temp;
            startDeg = (startDeg + 1) % Utils.TWO_PI;

            painterLeft.setColor(coLeft);
            painterTop.setColor(coTop);
            painterRight.setColor(coRight);
            painterBot.setColor(coBot);
            invalidate();
        }
    };
}
