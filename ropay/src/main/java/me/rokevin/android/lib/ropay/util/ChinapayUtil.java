package me.rokevin.android.lib.ropay.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.chinapay.cppaysdk.util.LogUtils;

/**
 * Created by luokaiwen on 17/2/28.
 */

public class ChinapayUtil {

    /**
     * addPermission方法.
     */
    public static void addPermission(Activity activity) {

        int PERMISSION_REQUEST_READ_PHONE_STATE = 1234;

        if (isMarshmallow()) {
            LogUtils.i("动态添加权限");
//            if (activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                activity.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_READ_PHONE_STATE);
//            }
        }
    }

    /**
     * isMarshmallow方法.
     *
     * @return 是否是安卓23以上
     */
    @TargetApi(23)
    private static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }
}
