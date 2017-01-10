package com.rainbow556.carlli.rainbow556.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * Created by Rainbow556 on 11/5/2016.
 */
public class BrokenLineView extends View{
    private int mWidth, mHeight;
    private Paint mPaint;
    private int mShadeStartColor = 0xff8A2BE2, mShadeEndColor = 0xff008B45;
    private int mLineStartColor = 0xffCD0000, mLineEndColor = 0xffCD00CD;
    private Path mLinePath, mShadePath;
    private int mXAxisNum = 5;
    private PointF[] mPoints;
    private float mStrokeWidth = 2f;
    private float mSmileProgress;
    private PathMeasure mPathMeasure;
    private float[] pos = new float[2];
    private LinearGradient mShadeGradient, mLineGradient;
    private float[] mProgressArr = new float[mXAxisNum];
    private String[] mCalibrationArr = new String[mXAxisNum];

    public BrokenLineView(Context context){
        super(context);
        init(context);
    }

    public BrokenLineView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public BrokenLineView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(20);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(0xff00ff00);
        mLinePath = new Path();
        mShadePath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Random random = new Random();
        float d = mWidth*0.85f/6;
        //模拟数据
        mPoints = new PointF[mXAxisNum];
        for(int i = 0; i < mXAxisNum; i++){
            float x = i*d + mStrokeWidth /2;
            mPoints[i] = new PointF(x, -random.nextInt((int) (mHeight*0.8f*0.5f)));

            mCalibrationArr[i] = i+1+"月";
        }
        mShadeGradient = new LinearGradient(0, 0, mPoints[mXAxisNum-1].x, 0,
                                            mShadeStartColor, mShadeEndColor, Shader.TileMode.CLAMP);
        mLineGradient = new LinearGradient(0, 0, mPoints[mXAxisNum-1].x, 0,
                                            mLineStartColor, mLineEndColor, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.save();
        //画轴
        canvas.translate(mWidth*0.15f, mHeight*0.8f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xff00ff00);
        mPaint.setShader(null);
        //x
        canvas.drawLine(-mWidth*0.15f, 0, mWidth*0.85f, 0, mPaint);
        //y
        canvas.drawLine(0, 0, 0, -mHeight*0.8f, mPaint);
        //shade
        mLinePath.reset();
        mLinePath.moveTo(mStrokeWidth/2, mPoints[0].y*mProgressArr[0]);
        boolean isEmpty = true;
        for(int i = 0; i < mXAxisNum; i++){
            if(mProgressArr[i] != 0f){
                isEmpty = false;
            }
            mLinePath.lineTo(mPoints[i].x, mPoints[i].y*mProgressArr[i]);
        }
        if(!isEmpty){
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(0xffaafcdc);
            mPaint.setShader(mShadeGradient);
            mShadePath.set(mLinePath);
            mShadePath.lineTo(mPoints[mXAxisNum - 1].x, -mStrokeWidth / 2);
            mShadePath.lineTo(mStrokeWidth / 2, -mStrokeWidth / 2);
            mShadePath.close();
            canvas.drawPath(mShadePath, mPaint);
        }
        //折线
        if(!isEmpty){
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0xffff0000);
            mPaint.setShader(mLineGradient);
            canvas.drawPath(mLinePath, mPaint);
        }
        //刻度
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(null);
        mPaint.setColor(0xff0000ff);
        float d = mWidth*0.85f/6;
        for(int i = 0; i < mXAxisNum; i++){
            if(i != 0){
                canvas.drawLine(i * d, 0, i * d, -10, mPaint);
            }
            float textW = mPaint.measureText(mCalibrationArr[i]);
            canvas.drawText(mCalibrationArr[i], i*d-textW/2, 30, mPaint);
        }
        //头像
        if(mSmileProgress != 0f){
            mPathMeasure.setPath(mLinePath, false);
            mPathMeasure.getPosTan(mSmileProgress * mPathMeasure.getLength(), pos, null);
            mPaint.setColor(0xff7FFF00);
            mPaint.setShader(null);
            canvas.drawCircle(pos[0], pos[1], 10, mPaint);
        }

        canvas.restore();
    }

    public void anim(){
        for(int i = 0; i < mXAxisNum; i++){
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1.1f, 1f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setStartDelay(i*100);
            anim.setDuration(600);
            final int index = i;
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                @Override
                public void onAnimationUpdate(ValueAnimator animation){
                    mProgressArr[index] = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            if(i == mXAxisNum-1){
                anim.addListener(new AnimatorListenerAdapter(){
                    @Override
                    public void onAnimationEnd(Animator animation){
                        animSmile();
                    }
                });
            }
            anim.start();
        }
    }

    private void animSmile(){
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setStartDelay(200);
        anim.setDuration(2000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                mSmileProgress = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }
}
