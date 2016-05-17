package com.rainbow556.carlli.rainbow556.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Carl.li on 2016/5/17.
 */
public class KeyboardVisibilityCatcher{
    private Activity activity;
    private View decorView;
    private OnKeyboardVisibilityChangeListener listener;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener(){
        // Threshold for minimal keyboard height.
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        private final Rect windowVisibleDisplayFrame = new Rect();
        private int lastVisibleDecorViewHeight;

        @Override
        public void onGlobalLayout(){
            // Retrieve visible rectangle inside window.
            decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
            final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();
            // Decide whether keyboard is visible from changing decor view height.
            if(lastVisibleDecorViewHeight != 0){
                if(lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX){
                    // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                    int currentKeyboardHeight = decorView.getHeight() - windowVisibleDisplayFrame.bottom;
                    // Notify listener about keyboard being shown.
                    KeyboardVisibilityCatcher.this.listener.onKeyboardShown(currentKeyboardHeight);
                }else if(lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight){
                    // Notify listener about keyboard being hidden.
                    KeyboardVisibilityCatcher.this.listener.onKeyboardHidden();
                }
            }
            // Save current decor view height for the next call.
            lastVisibleDecorViewHeight = visibleDecorViewHeight;
        }
    };

    public KeyboardVisibilityCatcher(Activity activity){
        this.activity = activity;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void listen(OnKeyboardVisibilityChangeListener listener){
        if(listener == null){
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            this.listener = null;
            onGlobalLayoutListener = null;
            activity = null;
            decorView = null;
            return;
        }
        this.listener = listener;
        // Top-level window decor view.
        decorView = activity.getWindow().getDecorView();
        // Register global layout listener.
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public interface OnKeyboardVisibilityChangeListener{

        void onKeyboardShown(int keyboardHeight);

        void onKeyboardHidden();
    }
}
