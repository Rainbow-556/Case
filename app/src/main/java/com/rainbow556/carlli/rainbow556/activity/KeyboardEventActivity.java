package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.util.Log;

import com.rainbow556.carlli.rainbow556.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class KeyboardEventActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_event);
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener(){
            @Override
            public void onVisibilityChanged(boolean isOpen){
                // some code depending on keyboard visiblity status
                Log.e("lx", isOpen + "");
            }
        });
    }

    @Override
    public void onBackPressed(){
        setResult(222);
        super.onBackPressed();
    }
}
