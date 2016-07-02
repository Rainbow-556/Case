package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl.li on 2016/6/30.
 */
public class GestureLockViewGroup extends RelativeLayout{
    private int mLockViewCount = 3;
    private int mLockViewMargin;
    private int mWidth, mHeight;
    private GestureLockView[] mLockViews;
    private List<Integer> mSelectedLockIds = new ArrayList<>();
    private Paint mPaint;
    private Path mPath;
    private int mPathWidth = 10;
    private int mTouchRadius;
    private int mPathColor = 0xff0000ff, mPathWrongColor = 0xffff0000;
    private int mTmpX, mTmpY, mLastPathX, mLastPathY;
    private int[] mAnswer ={1, 2, 5, 8};

    public GestureLockViewGroup(Context context){
        super(context);
        init();
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPathWidth);
        mPaint.setColor(mPathColor);

        mPath = new Path();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(mLockViews == null){
            mLockViews = new GestureLockView[mLockViewCount*mLockViewCount];
        }
        mLockViewMargin = Math.min(mWidth, mHeight) / 8;
        int w = (mWidth - getPaddingLeft() - getPaddingRight() - (mLockViewCount - 1)*mLockViewMargin) / mLockViewCount;
        int h = (mHeight - getPaddingTop() - getPaddingBottom() - (mLockViewCount - 1)*mLockViewMargin) / mLockViewCount;
        int size = Math.min(w, h);
        mTouchRadius = size / 8;
        for(int i = 0; i < mLockViews.length; i++){
            GestureLockView view = new GestureLockView(getContext());
            view.setId(i+1);
            LayoutParams lp = new LayoutParams(size, size);
            //设置右下左上的边距
            int rightMargin = mLockViewMargin;
            int bottomMargin = 0;
            int leftMargin = 0;
            int topMargin = 0;
            //不是每行的第一个，则设置位置为前一个的右边
            if(i % mLockViewCount != 0){
                lp.addRule(RelativeLayout.RIGHT_OF, mLockViews[i - 1].getId());
            }
            //从第二行开始，设置为上一行同一位置View的下面
            if(i > mLockViewCount - 1){
                lp.addRule(RelativeLayout.BELOW, mLockViews[i - mLockViewCount].getId());
                topMargin = mLockViewMargin;
            }
            //一列中最后一个去除右边距
            if((i+1) % mLockViewCount == 0){
                rightMargin = 0;
            }
            lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            addView(view, lp);
            mLockViews[i] = view;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        super.dispatchDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        if(mLastPathX != 0 && mLastPathY != 0){
            canvas.drawLine(mLastPathX, mLastPathY, mTmpX, mTmpY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                reset();
                GestureLockView lockView1 = getLockViewByPos(x, y);
                if(lockView1 != null){
                    mSelectedLockIds.add(lockView1.getId());
                    lockView1.setState(GestureLockView.STATE_PRESS);
                    //路径
                    int cx = lockView1.getLeft()+lockView1.getWidth()/2;
                    int cy = lockView1.getTop()+lockView1.getHeight()/2;
                    mPath.moveTo(cx, cy);
                    mLastPathX = cx;
                    mLastPathY = cy;
                    mTmpX = cx;
                    mTmpY = cy;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //move时，还没有任何点被选中，则直接break
                if(mLastPathX == 0 || mLastPathY == 0){
                    break;
                }
                GestureLockView lockView2 = getLockViewByPos(x, y);
                if(lockView2 != null){
                    int id = lockView2.getId();
                    if(!mSelectedLockIds.contains(id)){
                        mSelectedLockIds.add(id);
                        lockView2.setState(GestureLockView.STATE_PRESS);
                        //路径
                        int cx = lockView2.getLeft()+lockView2.getWidth()/2;
                        int cy = lockView2.getTop()+lockView2.getHeight()/2;
                        mPath.lineTo(cx, cy);
                        mLastPathX = cx;
                        mLastPathY = cy;
                    }
                }
                mTmpX = x;
                mTmpY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起时，不去绘制直线
                mLastPathX =0;
                mLastPathY =0;
                invalidate();
                checkGesture();
                break;
        }

        return true;
    }

    private void checkGesture(){
        if(mSelectedLockIds.size() == 0){
            return;
        }
        int size = mSelectedLockIds.size();
        if(size != mAnswer.length){
            updateSelectLockViewState(GestureLockView.STATE_WRONG);
            updatePathColor(GestureLockView.STATE_WRONG);
        }else{
            for(int i = 0; i < mAnswer.length; i++){
                if(mSelectedLockIds.get(i) != mAnswer[i]){
                    updateSelectLockViewState(GestureLockView.STATE_WRONG);
                    updatePathColor(GestureLockView.STATE_WRONG);
                    break;
                }
            }
        }
        postDelayed(new Runnable(){
            @Override
            public void run(){
                reset();
            }
        }, 350);
    }

    private void updatePathColor(int state){
        if(state == GestureLockView.STATE_NORMAL){
            mPaint.setColor(mPathColor);
        }else if((state == GestureLockView.STATE_WRONG)){
            mPaint.setColor(mPathWrongColor);
        }
        invalidate();
    }

    private void updateSelectLockViewState(int state){
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            GestureLockView v = (GestureLockView) getChildAt(i);
            if(mSelectedLockIds.contains(v.getId())){
                v.setState(state);
            }
        }
    }

    private GestureLockView getLockViewByPos(int x, int y){
        GestureLockView target = null;
        int padding = mTouchRadius;
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            GestureLockView v = (GestureLockView) getChildAt(i);
            if(x > v.getLeft()+padding && x < v.getRight()-padding && y > v.getTop()+padding && y < v.getBottom()-padding){
                target = v;
            }
        }
        return target;
    }

    private void reset(){
        mPaint.setColor(mPathColor);
        mPath.reset();
        updateSelectLockViewState(GestureLockView.STATE_NORMAL);
        mSelectedLockIds.clear();

        mLastPathX = 0;
        mLastPathY = 0;
        mTmpX = 0;
        mTmpY = 0;
        invalidate();
    }
}
