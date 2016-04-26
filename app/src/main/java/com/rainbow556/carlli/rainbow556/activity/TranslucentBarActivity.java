package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.StatusBarUtils;

public class TranslucentBarActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        setContentView(R.layout.activity_translucent_bar);
        //让toolbar同actionbar一样使用,include自定义的topbar时注释到下面两句
//        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(toolbar);
//        StatusBarCompat.compat(this, 0xffff0000);
        StatusBarUtils.setColor(this, 0xffff0000);
    }
}
