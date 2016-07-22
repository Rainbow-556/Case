package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rainbow556.carlli.rainbow556.R;

public class CanvasActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
