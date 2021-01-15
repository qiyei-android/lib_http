package com.qiyei.android.http.common;

import android.util.Log;

public class HttpLog {

    private static int sLevel = Log.INFO;

    public static void setLevel(int level) {
        sLevel = level;
    }

    public static void v(String tag, String msg){
        if (sLevel <= Log.VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if (sLevel <= Log.DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if (sLevel <= Log.INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if (sLevel <= Log.WARN){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if (sLevel <= Log.ERROR){
            Log.e(tag,msg);
        }
    }

}
