package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.StatusBarUtils;

public class TranslucentBarActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent_bar);
        int color = getResources().getColor(R.color.colorPrimaryDark);
        StatusBarUtils.setColor(this, color);
    }
}
