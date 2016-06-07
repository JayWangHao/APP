package com.cattsoft.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 当前网络是否可用
 * Created by admin_local on 15/8/30.
 */
public class NetworkDetector {

    public static boolean detect(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }
}
