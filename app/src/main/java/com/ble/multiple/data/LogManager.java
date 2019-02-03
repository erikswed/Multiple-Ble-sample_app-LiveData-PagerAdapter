/*
 * Copyright (C) 2016 CWDO Systems Ltd  All Rights Reserved.
 * Unauthorized copying in any way of this file via any medium
 * is strictly prohibited Proprietary and confidential.
 *  <portplayers@gmail.com>, October 2016
 *
 * FURTHERMORE CWDO CONFIDENTIAL
 * __________________
 *
 * [2015] - [2030] CWDO Systems Ltd
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of CWDO Systems Ltd and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to CWDO Systems Ltd
 * and its suppliers and may be covered by patents, patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from CWDO Systems Ltd.
 */

package com.ble.multiple.data;


import android.app.Application;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Manager for log and event reporting.
 */
public class LogManager {

    private static final boolean log = true;

    private final static LogManager instance;

    static {
        instance = new LogManager(BleApplication.getInstance());
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LogManager getInstance() {
        return instance;
    }

    private LogManager(Application application) {
    }

    private static int dString(String tag, String msg) {
        if (log) {
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    private static int eString(String tag, String msg) {
        if (log) {
            return Log.e(tag, msg);
        } else {
            return 0;
        }
    }

    private static int iString(String tag, String msg) {
        if (log) {
            return Log.i(tag, msg);
        } else {
            return 0;
        }
    }

    private static int wString(String tag, String msg) {
        if (log) {
            return Log.w(tag, msg);
        } else {
            return 0;
        }
    }

    private static int vString(String tag, String msg) {
        if (log) {
            return Log.v(tag, msg);
        } else {
            return 0;
        }
    }

    /**
     * D int.
     *
     * @param obj the obj
     * @param msg the msg
     * @return the int
     */
    static public int d(Object obj, String msg) {
        return dString(obj.toString(), msg);
    }

    /**
     * E int.
     *
     * @param obj the obj
     * @param msg the msg
     * @return the int
     */
    static public int e(Object obj, String msg) {
        return eString(obj.toString(), msg);
    }

    /**
     * int.
     *
     * @param obj the obj
     * @param msg the msg
     * @return the int
     */
    static public int i(Object obj, String msg) {
        return iString(obj.toString(), msg);
    }

    /**
     * W int.
     *
     * @param obj the obj
     * @param msg the msg
     * @return the int
     */
    static public int w(Object obj, String msg) {
        return wString(obj.toString(), msg);
    }

    /**
     * V int.
     *
     * @param obj the obj
     * @param msg the msg
     * @return the int
     */
    static public int v(Object obj, String msg) {
        return vString(obj.toString(), msg);
    }

    /**
     * Firebase Crash Reporting.
     *
     * @param exception the Exception
     */
/*    public static void report(Exception exception) {
        FirebaseCrash.report(exception);
    }*/

    /**
     * Print stack trace if log is enabled.
     *
     * @param obj       the obj
     * @param exception the exception
     */
    public static void exception(Object obj, Exception exception) {
        if (!log) {
            return;
        }
        forceException(obj, exception);
    }

    /**
     * Print stack trace even if log is disabled.
     *
     * @param obj       the obj
     * @param exception the exception
     */
    public static void forceException(Object obj, Exception exception) {
        System.err.println(obj.toString());
        System.err.println(getStackTrace(exception));
    }

    /**
     * @param exception the exception
     * @return String
     */
    private static String getStackTrace(Exception exception) {
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * Is debugable boolean.
     *
     * @return the boolean
     */
/*    public static boolean isDebugable() {
        return debugable && SettingsManager.debugLog();
    }*/
}