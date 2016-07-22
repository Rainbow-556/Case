package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.view.DashboardView;
import com.rainbow556.carlli.rainbow556.view.NumberProgressBar;

public class GestureLockActivity extends AppCompatActivity{

    private DashboardView dashboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
    }

    private void testCustomView(){
        final NumberProgressBar pb = (NumberProgressBar) findViewById(R.id.pb);
        pb.postDelayed(new Runnable(){
            @Override
            public void run(){
                pb.start();
            }
        }, 1000);
    }

    public void start(View view){
        dashboardView = (DashboardView) findViewById(R.id.dash);
        dashboardView.start(80, 2000);
    }
}
