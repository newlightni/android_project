package com.bs.teachassistant.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by limh on 2017/4/7.
 * 格式化工具类
 */

public class FormatUtils {
    static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd", Locale.CANADA);
    static SimpleDateFormat sf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CANADA);
    static SimpleDateFormat sf2 = new SimpleDateFormat("MM/dd HH:mm", Locale.CANADA);
    static SimpleDateFormat sf3 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CANADA);

    public static String getDate() {
        return sf.format(new Date());
    }
    public static String getDateTime() {
        return sf3.format(new Date());
    }

    public static String getTime() {
        return sf2.format(new Date());
    }


    public static long getLongTime(String time) {
        long longtime = 0;
        try {
            Date date = sf1.parse(time);
            longtime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longtime;
    }

    public static String getStrTime(Date time) {
        return sf1.format(time);
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 格式化日期时间  小于0则在前面加0
     *
     * @param num 需要格式化的数字
     * @return 返回格式化后的值
     */
    public static String formatSuff(int num) {
        return num < 10 ? ("0" + num) : "" + num;
    }

    /**
     * @return
     */
    public static String formatWeek(int week) {
        String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return weeks[week];
    }

    /**
     * 获取sdk版本
     * @return 版本号
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
