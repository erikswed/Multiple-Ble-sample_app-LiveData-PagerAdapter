package com.ble.multiple.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;

import java.util.Locale;
import java.util.regex.Pattern;

public class BaseUtil {
    public static DisplayMetrics getWindowMetrics(Activity context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics;
    }

    public static boolean isNull(String s) {
        if ("".equals(s) || s == null) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        if (isNull(str)) {
            return false;
        }
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static void setBluetoothAdapter(Context context) {
        context.startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
    }

    public static byte[] getHexData(String SData) {
        int bytelen;
        int datalen = SData.getBytes().length;
        if (datalen % 2 == 0) {
            bytelen = datalen / 2;
        } else {
            bytelen = (datalen / 2) + 1;
        }
        byte[] sBuffer = new byte[bytelen];
        int i = 0;
        int j = 0;
        while (i < datalen) {
            while (i >= 0 && !CharInRange(SData.charAt(i))) {
                i++;
            }
            if (i + 1 >= datalen || !CharInRange(SData.charAt(i + 1))) {
                sBuffer[j] = StrToByte(String.valueOf(SData.charAt(i)));
                j++;
            } else {
                sBuffer[j] = StrToByte(SData.substring(i, i + 2));
                j++;
            }
            i += 2;
        }
        return sBuffer;
    }

    private static byte StrToByte(String s) {
        return Integer.valueOf(String.valueOf(Integer.parseInt(s, 16))).byteValue();
    }

    private static boolean CharInRange(char c) {
        boolean result = false;
        if (c >= '0' && c <= '9') {
            result = true;
        }
        if (c >= 'a' && c <= 'f') {
            result = true;
        }
        if (c < 'A' || c > 'F') {
            return result;
        }
        return true;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (byte b2 : b) {
            String hex = Integer.toHexString(b2 & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret = ret + hex.toUpperCase(Locale.getDefault());
        }
        return ret;
    }

    public static String getVersionName(Context context) {
        if (context == null) {
            return "1.0";
        }
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}
