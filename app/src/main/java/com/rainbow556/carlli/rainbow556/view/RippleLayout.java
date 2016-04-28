package com.rainbow556.carlli.rainbow556.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by Carl.li on 2016/4/28.
 */
public class RippleLayout extends FrameLayout{
    private float mDownX, mDownY;
    private int mWidth, mHeight;
    private int mRadius, mCurrentRadius;
    private Paint mPaint;
    private ValueAnimator animator;
    private OverlayView overlayView;

    public RippleLayout(Context context){
        this(context, null);
    }

    public RippleLayout(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        addOverlayView();
    }

    private void addOverlayView(){
        overlayView = new OverlayView(getContext());
        addView(overlayView, getChildCount());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(w, h) / 5;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        drawRipple(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void drawRipple(MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            mDownX = ev.getX();
            mDownY = ev.getY();
            if(animator == null){
                animator = ValueAnimator.ofInt(mRadius, Math.max(mWidth, mHeight));
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator){
                        mCurrentRadius = (int) valueAnimator.getAnimatedValue();
                        overlayView.drawRipple(mDownX, mDownY, mCurrentRadius);
                    }
                });
                animator.addListener(new Animator.AnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator animator){
                    }

                    @Override
                    public void onAnimationEnd(Animator animator){
                        mCurrentRadius = 0;
                        overlayView.drawRipple(0, 0, mCurrentRadius);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator){
                        mCurrentRadius = 0;
                        overlayView.drawRipple(0, 0, mCurrentRadius);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator){
                    }
                });
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(800);
            }
            if(!animator.isRunning()){
                animator.cancel();
                animator.start();
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
    }

    private static class OverlayView extends View{
        private float x;
        private float y;
        private float r;
        private Paint paint;

        public OverlayView(Context context){
            super(context);
            setLayerType(LAYER_TYPE_NONE, null);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(Color.GRAY);
            paint.setAlpha(100);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh){
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            canvas.drawCircle(x, y, r, paint);
        }

        public void drawRipple(float x, float y, float r){
            this.x = x;
            this.y = y;
            this.r = r;
            invalidate();
        }
    }
}
