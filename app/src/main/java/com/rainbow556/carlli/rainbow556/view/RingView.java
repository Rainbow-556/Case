package com.rainbow556.carlli.rainbow556.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.JLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Rainbow556 on 11/12/2016.
 */
public class RingView extends View{
    private int mArcCount;
    private int mWidth, mHeight;
    private float mStrokeWidth;
    private float mDensity;
    private Paint mPaint;
    private ArrayList<Data> mDataList;
    private RectF mViewRect;
    private float mRadius, mGapDegree = 3;
    private float mTotalAmount;
    private float mMinDegree = 10;
    private Bitmap mIcon;
    private Rect mSrcRect = new Rect();
    private RectF mDestRect = new RectF();

    public RingView(Context context){
        super(context);
        init(context);
    }

    public RingView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public RingView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mViewRect = new RectF(-mRadius, -mRadius, mRadius, mRadius);
    }

    private void init(Context context){
        mDensity = getResources().getDisplayMetrics().density;
        mStrokeWidth = 15 * mDensity;
        mRadius = 80 * mDensity;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mSrcRect.set(0, 0, mIcon.getWidth(), mIcon.getHeight());

        setData();
    }

    @Override
    protected void onDraw(Canvas canvas){
        mPaint.setColor(0xff0000ff);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);

        float start = -90;
        for(int i = 0; i < mArcCount; i++){
            float degree = mDataList.get(i).degree;
            canvas.drawArc(mViewRect, start, degree, false, mPaint);
            start += (degree + mGapDegree);
        }

//        float left =0, top = 0;
//        mDestRect.set(left, top, left+mIcon.getWidth()/2, top+mIcon.getHeight()/2);
        canvas.drawBitmap(mIcon, mSrcRect, mDestRect, null);

        canvas.restore();
    }

    public void anim(){
        ValueAnimator va = ValueAnimator.ofFloat(0.5f, 1f);
        va.setDuration(2000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                float f = (float) animation.getAnimatedValue();
                mDestRect.set(f*mRadius, -f*mIcon.getHeight(), f*mRadius+f*mIcon.getWidth(), 0);
                invalidate();
            }
        });
        va.start();
    }

    private void calcDegree(){
        float canUse = 360 - mArcCount * mGapDegree;
        ArrayList<Data> tempDataList = new ArrayList<>(mArcCount);
        int i = 0;
        //最多只是循环mArcCount次
        while(i < mArcCount){
            Iterator<Data> iterator = mDataList.iterator();
            while(iterator.hasNext()){
                //把所有金额的对应的degree<=最小degree的元素放入一个临时集合中
                Data d = iterator.next();
                float degree = d.percent * canUse;
                if(degree <= mMinDegree){
                    d.degree = mMinDegree;
                    tempDataList.add(d);
                    iterator.remove();
                    //减去移除的degree
                    canUse -= mMinDegree;
                }
            }
            i++;
        }
        //算出degree大于mMinDegree的金额总和
        float amount = 0;
        for(Data d : tempDataList){
            amount += d.amount;
        }
        amount = mTotalAmount - amount;
        //算出degree大于mMinDegree的degree
        for(int j = 0; j < mDataList.size(); j++){
            Data d = mDataList.get(j);
            if(j == mDataList.size() - 1){
                d.degree = canUse - sumDegree(mDataList);
            }else{
                //amount占剩余amount的百分比 * 剩余canUse degree
                d.degree = d.amount / amount * canUse;
            }
        }
        JLog.e("source: " + mDataList);
        JLog.e("temp: " + tempDataList);
        mDataList.addAll(tempDataList);
        tempDataList.clear();
        Collections.sort(mDataList, new Comparator<Data>(){
            @Override
            public int compare(Data lhs, Data rhs){
                if(lhs.index > rhs.index){
                    return 1;
                }else if(lhs.index < rhs.index){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
        JLog.e("source: " + mDataList);
    }

    private void setData(){
        mArcCount = 4;
        mDataList = new ArrayList<>(mArcCount);
        mTotalAmount = 1000;
        mDataList.add(new Data(250, 0));
        mDataList.add(new Data(250, 1));
        mDataList.add(new Data(250, 2));
        mDataList.add(new Data(250, 3));
        for(int i = 0; i < mArcCount; i++){
            Data d = mDataList.get(i);
            if(i == mArcCount - 1){
                d.percent = 1 - sumPercent(mDataList);
            }else{
                d.percent = d.amount / mTotalAmount;
            }
        }
        calcDegree();
    }

    private float sumPercent(ArrayList<Data> list){
        float per = 0;
        for(int i = 0; i < list.size() - 1; i++){
            per += list.get(i).percent;
        }
        return per;
    }

    private float sumDegree(ArrayList<Data> list){
        float degree = 0;
        for(int i = 0; i < list.size() - 1; i++){
            degree += list.get(i).degree;
        }
        return degree;
    }

    public static class Data{
        public int index;
        public float amount;
        public float percent;
        public float degree;

        public Data(float amount, int index){
            this.amount = amount;
            this.index = index;
        }

        @Override
        public String toString(){
            return "index=" + index + "  percent=" + percent + "  degree=" + degree;
        }
    }
}
