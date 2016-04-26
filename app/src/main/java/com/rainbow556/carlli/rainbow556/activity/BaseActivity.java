package com.rainbow556.carlli.rainbow556.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Carl.li on 2016/4/18.
 */
public class BaseActivity extends AppCompatActivity{
    public static final int INVALID_REQUEST_CODE = -1;
    private ActivityResultCallback mActivityResultCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(mActivityResultCallback != null){
            mActivityResultCallback.onActivityResultCallback(requestCode, resultCode, data);
            mActivityResultCallback = null;
        }
    }

    protected void changeUI(Intent i){
        changeUI(i, null);
    }

    protected void changeUI(Intent i, ActivityResultCallback callback){
        if(callback == null || callback.getRequestCode() == INVALID_REQUEST_CODE){
            startActivity(i);
            return;
        }
        mActivityResultCallback = callback;
        startActivityForResult(i, callback.getRequestCode());
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mActivityResultCallback = null;
    }

    protected <T extends View> T findView(int id){
        return (T)findViewById(id);
    }

    public abstract class ActivityResultCallback{
        private int requestCode;

        public ActivityResultCallback(){}

        public ActivityResultCallback(int requestCode){
            this.requestCode = requestCode;
        }

        public int getRequestCode(){
            return requestCode;
        }

        public abstract void onActivityResultCallback(int requestCode, int resultCode, Intent data);
    }
}
