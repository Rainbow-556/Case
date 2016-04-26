package com.rainbow556.carlli.rainbow556.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carl.li on 2016/4/25.
 */
public class PermissionsChecker{

    // 判断权限集合
    public static boolean lacksPermissions(Activity activity, int requestCode, String... permissions){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        List<String> denyPermissions = findDeniedPermissions(activity, permissions);
        if(denyPermissions.size() == 0){
            return false;
        }
        ActivityCompat.requestPermissions(activity, denyPermissions.toArray(new String[denyPermissions.size()]), requestCode);
        return true;
    }

    private static List<String> findDeniedPermissions(Context context, String... permission){
        List<String> denyPermissions = new ArrayList<>();
        for(String value : permission){
            if(ContextCompat.checkSelfPermission(context, value) != PackageManager.PERMISSION_GRANTED){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }
}
