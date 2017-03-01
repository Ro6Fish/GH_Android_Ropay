package me.rokevin.android.lib.ropay.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luokaiwen on 15/4/28.
 * <p>
 * 日志工具类
 */
public class RoPayLog {

    private static final boolean LOGABLE = true;

    private static List<String> blockTags = new ArrayList<String>();

    static {
        blockTags.add("LruBitmapImageCache");
        blockTags.add("DiskCache");
    }

    public static void log(String tag, String msg) {

        if (LOGABLE && !blockTags.contains(tag) && !TextUtils.isEmpty(msg)) {
            if (null == msg) {
                msg = "tag msg is null";
            }

            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {

        if (LOGABLE && !blockTags.contains(tag) && !TextUtils.isEmpty(msg)) {
            if (null == msg) {
                msg = "tag msg is null";
            }

            Log.e(tag, msg);
        }
    }
}
