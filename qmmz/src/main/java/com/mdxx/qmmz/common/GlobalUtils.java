package com.mdxx.qmmz.common;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 国际版工具类
 *
 * Created by linchaolong on 2016/9/8.
 */
public class GlobalUtils {

    /**
     * 手机号校验
     */
    public final static boolean isPhone(CharSequence phone) {
        if (phone == null) {
            return false;
        } else {
            if (phone.length() < 6 || phone.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        }
    }
    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }
    /**
     * 获取手机IMSI号
     */
    public static String getIMSI(Context context){
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();

        return imsi ;
    }

    /**
     * 获取手机mac
     * @param context
     * @return
     */
    public static String getMacAddress(Context context){
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }
}
