package com.rainbow556.carlli.rainbow556.activity;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

import com.rainbow556.carlli.rainbow556.R;

public class Animation3DActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation3_d);
    }

    public void start(View view){
        View target = findViewById(R.id.view);
        target.clearAnimation();
//        FlipAnimation anim = new FlipAnimation(target.getWidth()/2, target.getHeight()/2);
        FlipAnimation anim = new FlipAnimation();
        anim.setDuration(1000);
        anim.setInterpolator(new BounceInterpolator());
//        anim.setFillAfter(true);
        target.startAnimation(anim);
    }

    public static class FlipAnimation extends Animation{
        /**
         * Camera坐标系：
         * x轴为屏幕水平方向，原点右侧为正，左侧为负
         * y轴为屏幕竖直方向，原点上侧为正，下侧为负
         * z轴为垂直于屏幕方向，原点向内为正，外侧为负
         */
        private Camera mCamera;
        private int centerX, centerY;

        public FlipAnimation(){}

        public FlipAnimation(int centerX, int centerY){
            this.centerX = centerX;
            this.centerY = centerY;
        }

        /**
         * @param width 执行动画的view的width
         * @param height 执行动画的view的height
         * @param parentWidth
         * @param parentHeight
         */
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight){
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            //原点默认为view的中心点
            if(centerX == 0){
                centerX = width / 2;
            }
            if(centerY == 0){
                centerY = height / 2;
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t){
            super.applyTransformation(interpolatedTime, t);
            Matrix matrix = t.getMatrix();
            mCamera.save();

            float endValue = 180f;
            float currentValue = interpolatedTime*endValue;
            //当时围绕着x或y轴旋转时，角度等于90或-90度时，内容是处于看不到的状态
            if(currentValue > 90 || currentValue < -90){
                if(currentValue > 0){
                    currentValue = 270 + currentValue - 90;
                }else if(currentValue < 0){
                    currentValue = -270 + (currentValue + 90);
                }
            }
            mCamera.rotateX(currentValue);
//            mCamera.rotateY(currentValue);
//            mCamera.rotateZ(currentValue);
//            mCamera.rotate(currentValue, currentValue, currentValue);
            //translate方法的x，y参数只是移动view内容，而z参数是对view进行缩放，正数缩小，负数放大
//            mCamera.translate(0, 0, -currentValue);
            //把camera计算后的matrix赋值给t.getMatrix()
            mCamera.getMatrix(matrix);
            mCamera.restore();
            //设置原点
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
