package com.rainbow556.carlli.rainbow556.util;
/**
 * Created by Carl.li on 2016/3/15.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 状态栏工具类
 * Created by Carl.li on 16/4/27.
 */
public class StatusBarUtils{

    /**
     * 设置状态栏颜色
     * @param activity
     * @param color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setColor(Activity activity, int color){
        int sdk = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        if(sdk >= Build.VERSION_CODES.KITKAT && sdk < Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View statusView = createStatusBarView(activity, color);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            rootView.setFitsSystemWindows(false);
            rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);
        }else if(sdk >= Build.VERSION_CODES.LOLLIPOP){
            rootView.setFitsSystemWindows(true);
            window.setStatusBarColor(color);
        }
    }

    /**
     * 使状态栏透明
     * <p/>
     * 适用于图片作为背景的界面,此时图片会填充到状态栏
     * @param activity 需要设置的activity
     */
    public static void setTransparent(Activity activity){
        int sdk = Build.VERSION.SDK_INT;
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        if(sdk >= Build.VERSION_CODES.KITKAT && sdk < Build.VERSION_CODES.LOLLIPOP){
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }else if(sdk >= Build.VERSION_CODES.LOLLIPOP){
            setFullTransparentAboveLollipop(activity);
        }
    }

    /**
     * 实现5.0及以上版本状态栏完全透明，原本是半透明
     * @param activity
     */
    private static void setFullTransparentAboveLollipop(Activity activity){
        //5.0以上实现状态栏完全透明核心代码
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     * @param color        状态栏颜色值
     */
    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color){
        int sdk = Build.VERSION.SDK_INT;
        if(sdk >= Build.VERSION_CODES.KITKAT){
            drawerLayout.setFitsSystemWindows(false);
            View statusBarView = createStatusBarView(activity, color);
            ViewGroup content = (ViewGroup) drawerLayout.getChildAt(0);
            content.addView(statusBarView, 0);
            //内容布局不是 LinearLayout 时,设置padding top
            if(!(content instanceof LinearLayout) && content.getChildAt(1) != null){
                content.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            content.setFitsSystemWindows(false);
            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawer.setFitsSystemWindows(false);
            //如果抽屉布局设置了背景，则让背景延伸到状态栏，否则只是给抽屉布局添加一个色块子view
            if(drawer.getBackground() == null){
                statusBarView = createStatusBarView(activity, color);
                drawer.addView(statusBarView, 0);
                if(!(drawer instanceof LinearLayout) && drawer.getChildAt(1) != null){
                    drawer.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
                }
            }else{
                drawer.setPadding(0, getStatusBarHeight(activity), 0, 0);
            }
            setFullTransparentAboveLollipop(activity);
        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    public static View createStatusBarView(Activity activity, int color){
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context){
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     * @param activity     需要设置的activity
     * @param drawerLayout DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 设置状态栏透明
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置内容布局属性
            ViewGroup content = (ViewGroup) drawerLayout.getChildAt(0);
            content.setFitsSystemWindows(true);
            content.setClipToPadding(true);
            // 设置抽屉布局属性
            ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
            drawer.setFitsSystemWindows(false);
            // 设置 DrawerLayout 属性
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    /*网上其他解决5.0以上状态栏完全透明的方案，先保留*/
    private static void transparentStatusBarAboveLollipop(Activity activity){
        //make full transparent statusBar
        if(Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if(Build.VERSION.SDK_INT >= 19){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if(Build.VERSION.SDK_INT >= 21){
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on){
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if(on){
            winParams.flags |= bits;
        }else{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /*网上其他解决5.0以上状态栏完全透明的方案，先保留*/
}
