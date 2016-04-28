package com.rainbow556.carlli.rainbow556.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rainbow556.carlli.rainbow556.R;

public class RippleActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);
        findView(R.id.tv).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(RippleActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
