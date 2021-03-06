package com.rainbow556.carlli.rainbow556.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rainbow556.carlli.rainbow556.R;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
    }

    public void go(View view){
        Class cls = getTargetClass(view.getId());
        if(cls != null){
            Intent intent = new Intent(this, cls);
            startActivity(intent);
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
            case R.id.btn_banner:
                cls = BannerLayoutActivity.class;
                break;
            case R.id.btn_auto_load:
                cls = RecyclerViewActivity.class;
                break;
            case R.id.btn_p:
                cls = PermissionActivity.class;
                break;
            case R.id.btn_status_bar:
                cls = TranslucentBarActivity.class;
                break;
            case R.id.btn_ripple:
                cls = RippleActivity.class;
                break;
            case R.id.btn_video:
                cls = VideoWebActivity.class;
                break;
            case R.id.btn_practice:
                cls = CanvasActivity.class;
                break;
            case R.id.btn_gesture:
                cls = GestureLockActivity.class;
                break;
            case R.id.btn_life:
                cls = FragmentLifecycleActivity.class;
                break;
            case R.id.btn_3d:
                cls = Animation3DActivity.class;
                break;
            case R.id.btn_drag:
                cls = ViewDragHelperActivity.class;
                break;
        }
        return cls;
    }
}
