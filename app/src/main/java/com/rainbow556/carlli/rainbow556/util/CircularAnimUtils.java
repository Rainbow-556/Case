package com.rainbow556.carlli.rainbow556.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Method;

/**
 * 对ViewAnimationUtils.createCircularReveal()方法的封装.
 */
public class CircularAnimUtils{

    private static final long PERFECT_MILLS = 500;

    /**
     * 向四周伸张，直到完成显示
     * @param view
     * @param startRadius
     * @param duration
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void show(View view, float startRadius, long duration){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            view.setVisibility(View.VISIBLE);
            return;
        }
        int w = view.getWidth();
        int h = view.getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int finalRadius = Math.max(cx, cy);//(int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * 由两边向中间收缩，直到隐藏
     * @param view
     * @param endRadius
     * @param duration
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hide(final View view, float endRadius, long duration){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            view.setVisibility(View.INVISIBLE);
            return;
        }
        int w = view.getWidth();
        int h = view.getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int initialRadius = Math.max(cx, cy);//(int) Math.sqrt(w * w + h * h) + 1;
        //cx,cy是相对于view的坐标
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, endRadius);
        anim.setDuration(duration);
        anim.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    /**
     * 从指定View开始向四周伸张(伸张颜色或图片为colorOrImageRes), 然后进入另一个Activity,
     * 返回至 @thisActivity 后显示收缩动画。
     */
    @SuppressLint("NewApi")
    public static void startActivityForResult(final Activity thisActivity, final Intent intent, final int requestCode, final Bundle bundle,
                                              final View triggerView, int colorOrImageRes, final long durationMills){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){
            thisActivity.startActivity(intent);
            return;
        }
        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(thisActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(colorOrImageRes);
        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight() - getNavigationBarHeight(thisActivity);
        decorView.addView(view, w, h);
        final int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(durationMills);
        anim.addListener(new AnimatorListenerAdapter(){
            @Override
            public void onAnimationEnd(Animator animation){
                super.onAnimationEnd(animation);
                if(requestCode == 0)
                    thisActivity.startActivity(intent);
                else if(bundle == null)
                    thisActivity.startActivityForResult(intent, requestCode);
                else
                    thisActivity.startActivityForResult(intent, requestCode, bundle);
                // 默认渐隐过渡动画.
                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                // 默认显示返回至当前Activity的动画
                //postDelayed()的runnable run方法会在用户回到thisActivity时再执行，不知道为什么？？？
                triggerView.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                        anim.setDuration(durationMills);
                        anim.addListener(new AnimatorListenerAdapter(){
                            @Override
                            public void onAnimationEnd(Animator animation){
                                super.onAnimationEnd(animation);
                                try{
                                    decorView.removeView(view);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        anim.start();
                    }
                }, 100);
            }
        });
        anim.start();
    }


    /*下面的方法全是重载，用简化上面方法的构建*/

    public static void startActivityForResult(Activity thisActivity, Intent intent, Integer requestCode, View triggerView, int colorOrImageRes){
        startActivityForResult(thisActivity, intent, requestCode, null, triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static void startActivity(Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes, long durationMills){
        startActivityForResult(thisActivity, intent, 0, null, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivity(Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes){
        startActivity(thisActivity, intent, triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static void startActivity(Activity thisActivity, Class<?> targetClass, View triggerView, int colorOrImageRes){
        startActivity(thisActivity, new Intent(thisActivity, targetClass), triggerView, colorOrImageRes, PERFECT_MILLS);
    }

    public static int getNavigationBarHeight(Activity context){
        if(!checkDeviceHasNavigationBar(context)){
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context context){
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if(id > 0){
            hasNavigationBar = rs.getBoolean(id);
        }
        try{
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if("1".equals(navBarOverride)){
                hasNavigationBar = false;
            }else if("0".equals(navBarOverride)){
                hasNavigationBar = true;
            }
        }catch(Exception e){
        }
        return hasNavigationBar;
    }
}