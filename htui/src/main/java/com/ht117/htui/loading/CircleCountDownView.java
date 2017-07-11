package com.ht117.htui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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

public class CircleCountDownView extends View {

    private int bgColor;
    private int txtColor;
    private int duration;
    private int interval;
    private double timeLeft;
    private float txtSize;
    private int startDeg;
    private double degree;
    private String content;

    private Paint bgPainter;
    private Paint txtPainter;

    private Handler handler;

    public CircleCountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);

        handler = new Handler();
    }

    public void setDuration(int duration) {
        this.duration = duration * Utils.SECOND;
    }

    public int getDuration() {
        return this.duration;
    }

    private void initView(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.CircleCountDownView);
            bgColor = array.getColor(R.styleable.CircleCountDownView_ccdv_background, Color.BLUE);
            txtColor = array.getColor(R.styleable.CircleCountDownView_ccdv_textcolor, Color.WHITE);
            duration = array.getInt(R.styleable.CircleCountDownView_ccdv_duration, Utils.DEF_TIME);
            txtSize = array.getDimensionPixelSize(R.styleable.CircleCountDownView_ccdv_txtSize, Utils.DEF_TXT_SIZE);
            interval = array.getInt(R.styleable.CircleCountDownView_ccdv_interval, Utils.SECOND / 10);
            timeLeft = duration;
            startDeg = Utils.TWO_PI / 4 * 3;
            degree = Utils.TWO_PI;

            bgPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPainter.setStyle(Paint.Style.FILL_AND_STROKE);
            bgPainter.setColor(bgColor);

            txtPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            txtPainter.setStyle(Paint.Style.STROKE);
            txtPainter.setColor(txtColor);
            txtPainter.setTextSize(txtSize);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect bound = new Rect();
        if (content == null) {
            content = String.valueOf(Utils.PERCENT);
        }
        txtPainter.getTextBounds(content, 0, content.length(), bound);
        int max = Math.max(bound.height(), bound.width()) * 2;
        setMeasuredDimension(max, max);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int width = getWidth();
        int height = getHeight();

        RectF bound = new RectF(left + Utils.PADDING, top + Utils.PADDING, left + width - Utils.PADDING, top + height - Utils.PADDING);

        Rect boundTxt = new Rect();
        txtPainter.getTextBounds(content, 0, content.length(), boundTxt);

        if (degree >= Utils.ZERO) {
            canvas.drawArc(bound, startDeg, Utils.TWO_PI, false, txtPainter);
            canvas.drawArc(bound, startDeg, -(float) degree, true, bgPainter);
        } else {
            canvas.drawArc(bound, Utils.ZERO, Utils.TWO_PI, false, txtPainter);
        }

        canvas.drawText(content, bound.centerX() - boundTxt.width() / 2, bound.centerY() + boundTxt.height() / 2, txtPainter);

        if (timeLeft >= 0) {
            handler.postDelayed(handleCtrl, interval);
        }
    }

    public void start() {
        timeLeft = duration;
        degree = Utils.TWO_PI;
        invalidate();
    }

    private final Runnable handleCtrl = new Runnable() {
        @Override
        public void run() {
            timeLeft -= interval;
            degree = (timeLeft / duration) * Utils.TWO_PI;

            content = null;
            if (timeLeft >= Utils.ZERO) {
                content = Utils.getContentFormat(getContext(), R.string.format_one_decimal, timeLeft / Utils.SECOND);
            } else {
                content = String.valueOf(Utils.ZERO);
            }

            invalidate();
        }
    };
}
