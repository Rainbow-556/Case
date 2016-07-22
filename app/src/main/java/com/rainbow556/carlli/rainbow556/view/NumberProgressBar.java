package com.rainbow556.carlli.rainbow556.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.rainbow556.carlli.rainbow556.util.JLog;

import java.text.DecimalFormat;

/**
 * Created by Carl.li on 2016/6/30.
 */
public class NumberProgressBar extends View{
    private float mTextSize = 35;
    private int mBarHeight = 5;
    private int mTextOffset = 10;
    private int mReachColor = 0xff00ff00, mUnreachColor = Color.GRAY, mTextColor = 0xffff0000;
    private int mDefaultWidth = 400;
    private Paint mPaint;
    private int mWidth, mHeight;
    private Rect mViewBounds;
    private float mProgress;
    private float mMaxProgress = 100f;
    private DecimalFormat format  =   new  DecimalFormat("##0.00");

    public NumberProgressBar(Context context){
        super(context);
        init();
    }

    public NumberProgressBar(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public NumberProgressBar(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = calcSize(widthMeasureSpec, true);
        int h = calcSize(heightMeasureSpec, false);
        setMeasuredDimension(w, h);
    }

    private int calcSize(int measureSpec, boolean isWidth){
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(mode == MeasureSpec.EXACTLY){
            result = size;
        }else{
            if(mode == MeasureSpec.AT_MOST){
                if(isWidth){
                    result = Math.min(size, mDefaultWidth);
                }else{
                    Paint.FontMetricsInt metrics = mPaint.getFontMetricsInt();
                    int h = metrics.bottom - metrics.top;
                    result = Math.max(h, mBarHeight);
                }
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mViewBounds = new Rect(0, 0, mWidth, mHeight);
        JLog.e("change: "+w+", "+h);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas){
        //已完成的进度
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mBarHeight);
        int y = mHeight / 2;

        float reachDx = mWidth/mMaxProgress*mProgress;
        String progressStr = format.format(mProgress)+"%";
        float textWidth = mPaint.measureText(progressStr, 0, progressStr.length());
        float unreachStartX;
        if(textWidth + reachDx + mTextOffset >= mWidth){
            reachDx = mWidth - textWidth - mTextOffset;
            unreachStartX = mWidth;
        }else{
            unreachStartX = textWidth + mTextOffset*2 + reachDx;
        }
        canvas.drawLine(0, y, reachDx, y, mPaint);

        //文本
        int baseline = getVerticalTextBaseline(mViewBounds, mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(progressStr, reachDx + mTextOffset, baseline, mPaint);

        //未完成的进度
        mPaint.setColor(mUnreachColor);
        canvas.drawLine(unreachStartX, y, mWidth, y, mPaint);
    }

    /**
     *
     * @param bounds
     * @param paint
     * @return
     */
    private static int getVerticalTextBaseline(Rect bounds, Paint paint){
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return (bounds.bottom + bounds.top -fontMetrics.bottom - fontMetrics.top) / 2;
    }

    public synchronized void setProgress(float progress){
        this.mProgress = progress;
        postInvalidate();
    }

    public void start(){
        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(8000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                float value = (float) valueAnimator.getAnimatedValue();
                setProgress(value);
            }
        });
        animator.start();
    }
}
