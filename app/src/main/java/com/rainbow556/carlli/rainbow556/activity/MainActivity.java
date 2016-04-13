package com.rainbow556.carlli.rainbow556.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
    }

    public void go(View view){
        Class cls = getTargetClass(view.getId());
        if(cls != null){
            startActivity(new Intent(this, cls));
        }
    }


    private Class getTargetClass(int id){
        Class cls = null;
        switch(id){
            case R.id.btn_count_down_listview:
                cls = CountDownListViewActivity.class;
                break;
            case R.id.btn_keyboard_event:
                cls = KeyboardEventActivity.class;
                break;
        }
        return cls;
    }
}
