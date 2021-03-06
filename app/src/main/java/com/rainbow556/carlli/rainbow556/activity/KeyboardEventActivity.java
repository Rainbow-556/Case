package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.JLog;
import com.rainbow556.carlli.rainbow556.util.KeyboardVisibilityCatcher;

public class KeyboardEventActivity extends BaseActivity{
    private KeyboardVisibilityCatcher catcher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_event);
        catcher = new KeyboardVisibilityCatcher(this);
        catcher.listen(new KeyboardVisibilityCatcher.OnKeyboardVisibilityChangeListener(){
            @Override
            public void onKeyboardShown(int keyboardHeight){
                JLog.e("show: " + keyboardHeight);
            }

            @Override
            public void onKeyboardHidden(){
                JLog.e("hidden");

            }
        });
    }

    @Override
    public void onBackPressed(){
        setResult(222);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        catcher.listen(null);
    }
}
