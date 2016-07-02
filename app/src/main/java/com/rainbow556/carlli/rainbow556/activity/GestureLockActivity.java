package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.view.NumberProgressBar;

public class GestureLockActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        final NumberProgressBar pb = (NumberProgressBar) findViewById(R.id.pb);
        pb.postDelayed(new Runnable(){
            @Override
            public void run(){
                pb.start();
            }
        }, 1000);
    }
}
