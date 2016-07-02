package com.rainbow556.carlli.rainbow556.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.JLog;
import com.rainbow556.carlli.rainbow556.util.KeyboardVisibilityCatcher;

import java.io.InputStream;

public class KeyboardEventActivity extends BaseActivity{
    //556
    private KeyboardVisibilityCatcher catcher;
    private Bitmap mBgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_event);
//        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener(){
//            @Override
//            public void onVisibilityChanged(boolean isOpen){
//                // some code depending on keyboard visiblity status
//                Log.e("lx", isOpen + "");
//            }
//        });
        catcher = new KeyboardVisibilityCatcher(this);
        catcher.listen(new KeyboardVisibilityCatcher.OnKeyboardVisibilityChangeListener(){
            @Override
            public void onKeyboardShown(int keyboardHeight){
                JLog.e("show: "+keyboardHeight);
            }

            @Override
            public void onKeyboardHidden(){
                JLog.e("hidden");
            }
        });
//        initBg();
    }

    @Override
    public void onBackPressed(){
        setResult(222);
        super.onBackPressed();
    }

    private void initBg(){
        InputStream bgIn = getResources().openRawResource(R.mipmap.bg_main_login);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //        options.inPreferredConfig = Bitmap.Config.RGB_565;
        mBgBitmap = BitmapFactory.decodeStream(bgIn, null, options);
        JLog.e("size: "+mBgBitmap.getWidth()+", "+mBgBitmap.getHeight());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        catcher.listen(null);
    }
}
