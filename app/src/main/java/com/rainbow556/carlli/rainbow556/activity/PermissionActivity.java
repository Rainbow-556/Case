package com.rainbow556.carlli.rainbow556.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.JLog;

public class PermissionActivity extends BaseActivity{
    private String PERMISSION_1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private String PERMISSION_2 = Manifest.permission.RECORD_AUDIO;
    private int code = 1;
    private RelativeLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        int h = getWindowManager().getDefaultDisplay().getHeight();
        JLog.e("height: "+h);
        content = findView(R.id.content);
    }

    public void check(View view){
//        boolean result = PermissionsChecker.lacksPermissions(this, code, PERMISSION_1, PERMISSION_2);
//        JLog.e(result);
        View test = LayoutInflater.from(this).inflate(R.layout.test, content, false);
        test = View.inflate(this, R.layout.test, null);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)test.getLayoutParams();
        if(params == null){
            JLog.e("null");
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        content.addView(test, params);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode != code){
            return;
        }
        // If request is cancelled, the result arrays are empty.
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
        }else{
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
    }
}
