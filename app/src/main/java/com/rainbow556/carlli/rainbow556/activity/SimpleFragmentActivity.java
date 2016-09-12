package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.rainbow556.carlli.rainbow556.R;

public class SimpleFragmentActivity extends FragmentActivity{
    public static final String FRAGMENT_NAME = "fragment_name";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FrameLayout contentView = new FrameLayout(this);
        contentView.setId(R.id.id_01);
        setContentView(contentView);
        addFragment();
    }

    private void addFragment(){
        try{
            String name = getIntent().getStringExtra(FRAGMENT_NAME);
            Class fragmentClass = Class.forName(name);
            Object obj = fragmentClass.newInstance();
            if(obj instanceof Fragment){
                getSupportFragmentManager().beginTransaction().add(R.id.id_01, (Fragment) obj, "f").commit();
            }
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
