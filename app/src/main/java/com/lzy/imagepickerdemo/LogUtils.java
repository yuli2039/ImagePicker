package com.lzy.imagepickerdemo;

import android.util.Log;

/**
 * Log工具类，可控制Log输出开关、保存Log到文件、过滤输出等级
 */
public class LogUtils {

    private static final String LOG_TAG = "@ImagePicker"; // 默认的tag
    private static final int LOG_LEVEL = LogLevel.INFO; // 日志等级过滤，参照logcat，例如i表示输出i以上的log

    private interface LogLevel {
        int VERBOSE = 0;
        int DEBUG = 1;
        int INFO = 2;
        int WARN = 3;
        int ERROR = 4;
        int NONE = 5; // 不输出日志
    }

    /*************************** Error ********************************/
    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        log(tag, msg.toString(), LogLevel.ERROR);
    }

    /**************************** Warn *********************************/
    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        log(tag, msg.toString(), LogLevel.WARN);
    }

    /**************************** Info *********************************/
    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), LogLevel.INFO);
    }

    /*************************** Debug ********************************/
    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), LogLevel.DEBUG);
    }

    /************************** Verbose ********************************/
    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), LogLevel.VERBOSE);
    }

    /**
     * 根据tag, msg和等级，输出日志
     */
    private static void log(String tag, String msg, int level) {
        if (LogLevel.NONE == LOG_LEVEL) return;

        if (LogLevel.ERROR == level && LOG_LEVEL <= LogLevel.ERROR) {
            Log.e(tag, msg);
        } else if (LogLevel.WARN == level && LOG_LEVEL <= LogLevel.WARN) {
            Log.w(tag, msg);
        } else if (LogLevel.INFO == level && LOG_LEVEL <= LogLevel.INFO) {
            Log.i(tag, msg);
        } else if (LogLevel.DEBUG == level && LOG_LEVEL <= LogLevel.DEBUG) {
            Log.d(tag, msg);
        } else {
            Log.v(tag, msg);
        }
    }

}
