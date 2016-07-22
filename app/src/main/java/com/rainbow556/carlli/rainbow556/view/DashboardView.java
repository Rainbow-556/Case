package com.rainbow556.carlli.rainbow556.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.rainbow556.carlli.rainbow556.util.JLog;

import java.text.DecimalFormat;

/**
 * Created by Carl.li on 2016/7/19.
 */
public class DashboardView extends View{
    private int mWidth, mHeight;
    private int mProgress;
    private Paint mPaint;
    private int mOffsetDegree = 6, mTotalLineCount, mCurrentLineCount;
    private String mProgressStr = "0%";
    private Rect bounds = new Rect();
    private DecimalFormat format  =   new  DecimalFormat("##0.00");

    public DashboardView(Context context){
        super(context);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(5);
        mPaint.setTextSize(75);
        int upOffset = mOffsetDegree;
        int downOffset = mOffsetDegree;
        while(180 % upOffset != 0){
            upOffset++;
        }
        while(180 % downOffset != 0){
            downOffset--;
        }
        if(upOffset - mOffsetDegree < mOffsetDegree - downOffset){
            mOffsetDegree = upOffset;
        }else{
            mOffsetDegree = downOffset;
        }
        JLog.e("mOffsetDegree: "+ mOffsetDegree +", up: "+upOffset+", down: "+downOffset);
        mTotalLineCount = 180 / mOffsetDegree + 2;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        drawLine(canvas);
        drawProgress(canvas);
    }

    private void drawProgress(Canvas canvas){
        mPaint.setColor(Color.WHITE);
        mPaint.getTextBounds(mProgressStr, 0, mProgressStr.length(), bounds);
        canvas.drawText(mProgressStr, (mWidth - bounds.width())/2, mHeight - bounds.height(), mPaint);
    }

    private void drawLine(Canvas canvas){
        canvas.save();
        canvas.translate(mWidth/2, mHeight-20);
        mPaint.setColor(Color.WHITE);
        for(int i = 0; i < mCurrentLineCount; i++){
            canvas.save();
            canvas.rotate(mOffsetDegree *i);
            canvas.drawLine(-300, 0, -265, 0, mPaint);
            canvas.restore();
        }
        mPaint.setColor(0xffff0000);
        for(int i = mCurrentLineCount; i < mTotalLineCount; i++){
            canvas.save();
            canvas.rotate(mOffsetDegree *i);
            canvas.drawLine(-300, 0, -265, 0, mPaint);
            canvas.restore();
        }
        canvas.restore();
    }

    public void start(int endProgress, int duration){
        ValueAnimator animator = ValueAnimator.ofInt(0, endProgress);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                mProgress = (int) valueAnimator.getAnimatedValue();
                mCurrentLineCount = (int) (mTotalLineCount * (mProgress/100f));
                mProgressStr = mProgress+"%";
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
