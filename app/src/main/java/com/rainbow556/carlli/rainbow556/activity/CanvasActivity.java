package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.view.RingView;

public class CanvasActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
//        final BrokenLineView line = (BrokenLineView) findViewById(R.id.v_line);
//        line.postDelayed(new Runnable(){
//            @Override
//            public void run(){
//                line.anim();
//            }
//        }, 1000);
        RingView ringView = (RingView) findViewById(R.id.v_ring);
        ringView.anim();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
