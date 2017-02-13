package com.urv.NetworkUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络工具
 *
 * @author gzejia、fence
 * @date 2016/9/21
 */
public class NetworkUtil {

    public static String TYPE_WIFI = "WiFi";
    public static String TYPE_2G = "2G";
    public static String TYPE_3G = "3G";
    public static String TYPE_4G = "4G";
    public static String TYPE_UNKNOWN = "unknown";

    private NetworkUtil() {
        throw new UnsupportedOperationException("NetworkUtil cannot be instantiated");
    }

    /**
     * 获取网路类型
     *
     * @return 手机网路类型
     */
    public static String getNetworkType(Context context) {
        String netType = TYPE_UNKNOWN;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String subTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();

                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = TYPE_2G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = TYPE_3G;
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = TYPE_4G;
                        break;

                    // 中国移动、联通、电信三种3G制式
                    default:
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA")
                            || subTypeName.equalsIgnoreCase("WCDMA")
                            || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = TYPE_3G;
                        } else {
                            netType = subTypeName;
                        }
                        break;
                }
            }
        }
        return netType;
    }

    /**
     * 网络是否连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是WiFi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        return connectivity.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openNetworkSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
