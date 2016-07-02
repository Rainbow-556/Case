package com.rainbow556.carlli.rainbow556.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Carl.li on 2016/6/29.
 */
public class PracticeView extends View{
    private int width, height;
    private Path path;
    private Paint paint;
    private float cPoint1X, cPoint1Y;


    public PracticeView(Context context){
        super(context);
        init();
    }

    public PracticeView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public PracticeView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas){
//        translateCanvas(canvas);
        quadTo(canvas);
    }

    private void quadTo(Canvas canvas){
//        canvas.save();
        paint.setColor(0xffff0000);
        paint.setStrokeWidth(10);
//        canvas.translate(width/2, height/2);
        path.reset();
        path.moveTo(200, height/2);
        cPoint1X = width/2;
        path.quadTo(cPoint1X, cPoint1Y, width-200, height/2);
        canvas.drawPath(path, paint);
        canvas.drawPoint(cPoint1X, cPoint1Y, paint);

//        canvas.restore();
    }

    private void translateCanvas(Canvas canvas){
        canvas.save();
        //把canvas的原点移动到中心
        canvas.translate(width/2, height/2);
        paint.setStrokeWidth(15);
        paint.setColor(0xffff0000);
        canvas.drawPoint(0, 0, paint);
        //正数为顺时针，负数反之
//        canvas.rotate(20);

        //起始点默认为canvas原点
        path.moveTo(-width/2, 0);
        path.lineTo(width/2, 0);
        path.moveTo(0, -height/2);
        path.lineTo(0, height/2);

//        path.addArc(-width/2, );

        paint.setStrokeWidth(3);
        paint.setColor(0xff00ff00);
        canvas.drawPath(path, paint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
//        cPoint1X = event.getX();
//        cPoint1Y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                animateControlPoint();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
//        invalidate();

        return true;
    }

    public void animateControlPoint(){
        ValueAnimator animator = ValueAnimator.ofFloat(height/2f+300, height/2f-200);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator){
                cPoint1Y = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }
}
