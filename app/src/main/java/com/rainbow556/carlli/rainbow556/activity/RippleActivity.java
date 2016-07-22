package com.rainbow556.carlli.rainbow556.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.RippleDrawableUtils;

public class RippleActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
        LinearLayout linearLayout = findView(R.id.ll);
        Drawable rippleD = RippleDrawableUtils.getSelectableDrawable(0xffffffff);
        linearLayout.setBackgroundDrawable(rippleD);
    }
}
