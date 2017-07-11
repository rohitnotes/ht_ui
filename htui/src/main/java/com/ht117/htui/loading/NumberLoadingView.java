package com.ht117.htui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ht117.htui.R;
import com.ht117.htui.Utils;

/**
 * Created by steve on 7/11/17.
 */

public class NumberLoadingView extends View {

    private int txtSize;
    private int txtColor;
    private int loadedColor;
    private int unloadedColor;
    private int progress;
    private int strokeWidth;
    private String content;

    private Paint loadedBarPainter;
    private Paint unloadedBarPainter;
    private Paint txtPainter;

    public NumberLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < Utils.PERCENT) {
            this.progress = progress;
        } else {
            this.progress = Utils.PERCENT;
        }
        content = Utils.getContentFormat(getContext(), R.string.format_percent, this.progress).concat("%");
        invalidate();
    }

    private void initView(AttributeSet attrs) {
        TypedArray array = null;

        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.NumberLoadingView);
            txtSize = array.getDimensionPixelSize(R.styleable.NumberLoadingView_nlv_txtSize, Utils.DEF_TXT_SIZE);
            txtColor = array.getColor(R.styleable.NumberLoadingView_nlv_txtColor, Color.RED);
            loadedColor = array.getColor(R.styleable.NumberLoadingView_nlv_loadedColor, Color.GREEN);
            unloadedColor = array.getColor(R.styleable.NumberLoadingView_nlv_unloadedColor, Color.WHITE);
            strokeWidth = array.getDimensionPixelSize(R.styleable.NumberLoadingView_nlv_strokeWidth, Utils.DEF_STROKE_WIDTH);
            progress = 0;
            content = Utils.getContentFormat(getContext(), R.string.format_percent, progress).concat("%");

            loadedBarPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            loadedBarPainter.setColor(loadedColor);
            loadedBarPainter.setStyle(Paint.Style.FILL_AND_STROKE);

            unloadedBarPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            unloadedBarPainter.setColor(unloadedColor);
            unloadedBarPainter.setStyle(Paint.Style.FILL_AND_STROKE);

            txtPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
            txtPainter.setTextSize(txtSize);
            txtPainter.setColor(txtColor);
            txtPainter.setStyle(Paint.Style.STROKE);
        } finally {
            if (array != null) {
                array.recycle();
            }
        }
    }

    public void reset() {
        progress = 0;
        content = Utils.getContentFormat(getContext(), R.string.format_percent, this.progress).concat("%");
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect bound = new Rect();
        if (content == null) {
            content = String.valueOf(Utils.ZERO);
        }
        txtPainter.getTextBounds(content, 0, content.length(), bound);
        int max = Math.max(getHeight(), bound.height());

        setMeasuredDimension(widthMeasureSpec, max);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int width = getWidth();
        int height = getHeight();

        Rect bound = new Rect();
        txtPainter.getTextBounds(content, 0, content.length(), bound);

        if (progress == Utils.PERCENT) {
            canvas.drawLine(left, top + height / 2, left + width / 2 - Utils.PADDING - bound.width() / 2, top + height / 2, loadedBarPainter);
            canvas.drawText(content, left + width / 2 - Utils.PADDING - bound.width() / 2, top + height / 2 + bound.height() / 2, txtPainter);
            canvas.drawLine(left + width / 2 + Utils.PADDING + bound.width() / 2, top + height / 2, left + width, top + height / 2, loadedBarPainter);
        } else {
            int loaded = width * progress / Utils.PERCENT;

            if (loaded + Utils.PADDING * 2 + bound.width() <= width) {
                canvas.drawLine(left, top + height / 2, left + loaded, top + height / 2, loadedBarPainter);
                canvas.drawText(content, left + loaded + Utils.PADDING, top + height / 2 + bound.height() / 2, txtPainter);
                canvas.drawLine(left + loaded + 2 * Utils.PADDING + bound.width(), top + height / 2, left + width, top + height / 2, unloadedBarPainter);
            } else {
                canvas.drawLine(left, top + height / 2, left + width - 2 * Utils.PADDING - bound.width(), top + height / 2, loadedBarPainter);
                canvas.drawText(content, left + width - Utils.PADDING - bound.width(), top + height / 2 + bound.height() / 2, txtPainter);
            }
        }
    }
}
