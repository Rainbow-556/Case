package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Carl.li on 2016/6/30.
 */
public class GestureLockView extends View{
    public static final int STATE_NORMAL = 1;
    public static final int STATE_PRESS = 2;
    public static final int STATE_WRONG = 3;
    private int mNormalColor = Color.GRAY;
    private int mPressColor = 0xff0000ff;
    private int mWrongColor = 0xffff0000;
    private int mWidth, mHeight, mRadius, mStrokeWidth = 5;
    private Paint mPaint;
    private int mCurrentState = STATE_NORMAL;

    public GestureLockView(Context context){
        super(context);
        init();
    }

    public GestureLockView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public GestureLockView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(w, h)/2 - mStrokeWidth*2;
    }

    @Override
    protected void onDraw(Canvas canvas){
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        switch(mCurrentState){
            case STATE_NORMAL:
                mPaint.setColor(mNormalColor);
                break;
            case STATE_PRESS:
                mPaint.setColor(mPressColor);
                break;
            case STATE_WRONG:
                mPaint.setColor(mWrongColor);
                break;
        }
        //画外圆
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, mPaint);
        //画内圆
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius/4, mPaint);
    }

    public void setState(int state){
        if(state == mCurrentState){
            return;
        }
        mCurrentState = state;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }
}
