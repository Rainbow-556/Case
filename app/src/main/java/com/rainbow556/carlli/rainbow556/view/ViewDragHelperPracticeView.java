package com.rainbow556.carlli.rainbow556.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Rainbow556 on 9/11/2016.
 */
public class ViewDragHelperPracticeView extends LinearLayout{

    private ViewDragHelper mViewDragHelper;
    private View mAutoBackView, mFirstChild, mThirdChild;
    private Point mAutoBackViewPos = new Point();

    public ViewDragHelperPracticeView(Context context){
        super(context);
        init();
    }

    public ViewDragHelperPracticeView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public ViewDragHelperPracticeView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        mFirstChild = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mThirdChild = getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        super.onLayout(changed, l, t, r, b);
        mAutoBackViewPos.set(mAutoBackView.getLeft(), mAutoBackView.getTop());
    }

    private void init(){
        //处理水平方向的越界
        //处理垂直方向的越界
        //监听拖动状态的改变
        //捕获View
        //释放View
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback(){
            @Override
            public boolean tryCaptureView(View child, int pointerId){
                return true;
            }

            //处理水平方向的越界
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx){
                return left;
            }

            //处理垂直方向的越界
            @Override
            public int clampViewPositionVertical(View child, int top, int dy){
                if(child == mFirstChild){
                    return mFirstChild.getTop();
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child){
                if(child == mThirdChild){
                    return 1;
                }
                return super.getViewHorizontalDragRange(child);
            }

            @Override
            public int getViewVerticalDragRange(View child){
                return super.getViewVerticalDragRange(child);
            }

            //监听拖动状态的改变
            @Override
            public void onViewDragStateChanged(int state){
                super.onViewDragStateChanged(state);
                switch(state){
                    case ViewDragHelper.STATE_DRAGGING:
                        break;
                    case ViewDragHelper.STATE_IDLE:
                        break;
                    case ViewDragHelper.STATE_SETTLING:
                        break;
                }
            }

            //捕获View
            @Override
            public void onViewCaptured(View capturedChild, int activePointerId){
                super.onViewCaptured(capturedChild, activePointerId);
            }

            //释放View
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel){
                super.onViewReleased(releasedChild, xvel, yvel);
                if(releasedChild == mAutoBackView){
                    mViewDragHelper.settleCapturedViewAt(mAutoBackViewPos.x, mAutoBackViewPos.y);
                    invalidate();
                }
            }
        });
    }

    //将事件拦截交给ViewDragHelper处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    //将Touch事件交给ViewDragHelper处理
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll(){
        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }
    }
}