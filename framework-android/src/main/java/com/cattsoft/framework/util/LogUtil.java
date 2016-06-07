package com.cattsoft.framework.util;

import android.util.Log;

/**
 * Created by Baixd on 2015/4/20 020.
 */
public class LogUtil {

    public static boolean isEnableLog = true;

    public static void v(String TAG, String msg){
        if(isEnableLog){
            Log.v(TAG, msg);
        }
    }

    public static void v(String TAG, String msg, Throwable throwable){
        if(isEnableLog){
            Log.v(TAG, msg, throwable);
        }
    }

    public static void d(String TAG,String msg){
        if(isEnableLog){
            Log.d(TAG, msg);
        }
    }

    public static void d(String TAG, String msg, Throwable throwable){
        if(isEnableLog){
            Log.d(TAG, msg, throwable);
        }
    }

    public static void i(String TAG, String msg){
        if(isEnableLog){
            Log.i(TAG, msg);
        }
    }

    public static void i(String TAG, String msg, Throwable throwable){
        if(isEnableLog){
            Log.i(TAG, msg, throwable);
        }
    }

    public static void w(String TAG, String msg){
        if(isEnableLog){
            Log.w(TAG, msg);
        }
    }

    public static void w(String TAG, String msg, Throwable throwable){
        if(isEnableLog){
            Log.w(TAG, msg, throwable);
        }
    }

    public static void w(String TAG, Throwable throwable){
        if(isEnableLog){
            Log.w(TAG, throwable);
        }
    }

    public static void e(String TAG, String msg){
        if(isEnableLog){
            Log.e(TAG, msg);
        }
    }

    public static void e(String TAG, String msg, Throwable throwable){
        if(isEnableLog){
            Log.e(TAG, msg, throwable);
        }
    }




}
