package com.rainbow556.carlli.rainbow556.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.fragment.LifecycleFragment;
import com.rainbow556.carlli.rainbow556.util.JLog;

public class FragmentLifecycleActivity extends AppCompatActivity{
    private LifecycleFragment fragmentA, fragmentB;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_lifecycle);
        if(savedInstanceState != null){
            fragmentA = (LifecycleFragment) getSupportFragmentManager().findFragmentByTag("A");
//            fragmentB = (LifecycleFragment) getSupportFragmentManager().findFragmentByTag("B");
//            getSupportFragmentManager().beginTransaction().hide(fragmentB).commit();
        }
    }

    public void add(View view){
        if(fragmentA == null){
            fragmentA = new LifecycleFragment();
            Bundle args = new Bundle();
            args.putString("text", "A");
            fragmentA.setArguments(args);
        }
        if(fragmentB == null){
            fragmentB = new LifecycleFragment();
            Bundle args = new Bundle();
            args.putString("text", "    B");
            fragmentB.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.rl_container, fragmentA, "A").commit();
        getSupportFragmentManager().beginTransaction().add(R.id.rl_container, fragmentB, "B").commit();
        getSupportFragmentManager().beginTransaction().remove(fragmentB).commit();
    }

    public void remove(View view){
        if(fragmentA != null){
            getSupportFragmentManager().beginTransaction().remove(fragmentA).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        JLog.e("onConfigurationChanged");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState){
        outState.putString("tag", "A");
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
