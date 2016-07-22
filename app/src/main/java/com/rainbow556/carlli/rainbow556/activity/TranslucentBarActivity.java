package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.StatusBarUtils;

public class TranslucentBarActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent_bar);
        int color = getResources().getColor(R.color.colorPrimaryDark);
        color = 0xffff0000;
//        StatusBarUtils.setColor(this, color);
        StatusBarUtils.setTransparent(this);
    }

    public void changeColor(View view){
        StatusBarUtils.setColor(this, 0xff00ff00);
    }
}
