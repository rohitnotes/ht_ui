package com.ht117.htui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ht117.htui.R;
import com.ht117.htui.Utils;

/**
 * Created by steve on 7/9/17.
 */

public class CountDownView extends View {

    private int background;
    private int duration;
    private int interval;
    private int deltaX;
    private int deltaY;
    private int oriented;
    private int timeLeft;

    private Paint bgPainter;
    private Paint borderPainter;

    private Handler handler;

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
        handler = new Handler();
    }

    public void setDuration(int duration) {
        this.duration = duration * Utils.SECOND;
    }

    private void initView(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownView);
            background = array.getColor(R.styleable.CountDownView_cdv_background, Color.BLUE);
            duration = array.getInt(R.styleable.CountDownView_cdv_duration, Utils.DEF_TIME);
            interval = array.getInt(R.styleable.CountDownView_cdv_interval, Utils.SECOND / 10);
            oriented = array.getInt(R.styleable.CountDownView_cdv_oriented, 1);
            timeLeft = duration;

            bgPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPainter.setColor(background);
            bgPainter.setStyle(Paint.Style.FILL_AND_STROKE);

            borderPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPainter.setColor(background);
            borderPainter.setStyle(Paint.Style.STROKE);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    public void start() {
        timeLeft = duration;
        deltaX = 0;
        deltaY = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

        double ratio = timeLeft * 1.0 / duration;
        if (oriented == 1) {
            deltaX = (int) (getWidth() * (1-ratio));
        } else {
            deltaY = (int) (getHeight() * (1-ratio));
        }

        canvas.drawRect(left, top, left + getWidth(), top + getHeight(), borderPainter);
        canvas.drawRect(left, top + deltaY, left + getWidth() - deltaX, top + getHeight(), bgPainter);

        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval);
        }
    }

    final Runnable handleCtrl = new Runnable() {
        @Override
        public void run() {
            timeLeft -= interval;
            invalidate();
        }
    };
}
