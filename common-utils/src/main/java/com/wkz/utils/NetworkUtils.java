package com.wkz.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * 网络信息工具类
 */
public class NetworkUtils {

    private static final String ETHERNET = "eth0";
    private static final String WLAN = "wlan0";
    private static final String DNS1 = "[net.dns1]";
    private static final String DNS2 = "[net.dns2]";
    private static final String ETHERNET_GATEWAY = "[dhcp.eth0.gateway]";
    private static final String WLAN_GATEWAY = "[dhcp.wlan0.gateway]";
    private static final String ETHERNET_MASK = "[dhcp.eth0.mask]";
    private static final String WLAN_MASK = "[dhcp.wlan0.mask]";

    private NetworkUtils() {
        throw new UnsupportedOperationException("U can't initialize me...");
    }

    /**
     * 判断网络是否连接
     * Judge whether current network is connected
     */
    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = null;
            if (connectivityManager != null) {
                networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            NetworkInfo info = null;
            if (connectivityManager != null) {
                info = connectivityManager.getActiveNetworkInfo();
            }
            return info != null && info.isConnected();
        }
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = null;
            if (connectivityManager != null) {
                networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected();
        }
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = null;
            if (connectivityManager != null) {
                networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            return networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        } else {
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected();
        }
    }

    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ContextUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected() ? networkInfo.getType() : -1;
    }

    /**
     * Get local ipv4 address
     */
    public static String getLocalIPv4() {
        return getLocalIp(true);
    }

    /**
     * Get local ipv6 address
     */
    public static String getLocalIPv6() {
        return getLocalIp(false);
    }

    /**
     * Get local ip address
     */
    private static String getLocalIp(boolean useIPv4) {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                while (inet.hasMoreElements()) {
                    InetAddress addr = inet.nextElement();
                    if (!addr.isLoopbackAddress()) {
                        String ip = addr.getHostAddress().toUpperCase(Locale.getDefault());
                        boolean isIPv4 = addr instanceof Inet4Address;
                        if (useIPv4) {
                            if (isIPv4) {
                                return ip;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = ip.indexOf('%');
                                return delim < 0 ? ip : ip.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            com.orhanobut.logger.Logger.e(e.toString());
        }
        return "";
    }

    /**
     * Get wlan mac address
     */
    public static String getWlanMacAddress() {
        return getMacAddress(WLAN);
    }

    /**
     * Get ethernet mac address
     */
    public static String getEthernetMacAddress() {
        return getMacAddress(ETHERNET);
    }

    /**
     * Get mac address
     */
    private static String getMacAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                        continue;
                    }
                }
                byte[] mac;
                mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (SocketException e) {
            com.orhanobut.logger.Logger.e(e.toString());
        }
        return "";
    }

    /**
     * Get dns1
     */
    public static String getDNS1() {
        return getPropInfo(DNS1);
    }

    /**
     * Get dns2
     */
    public static String getDNS2() {
        return getPropInfo(DNS2);
    }

    /**
     * Get ethernet gateway
     */
    public static String getEthernetGateway() {
        return getPropInfo(ETHERNET_GATEWAY);
    }

    /**
     * Get wlan gateway
     */
    public static String getWlanGateway() {
        return getPropInfo(WLAN_GATEWAY);
    }

    /**
     * Get ethernet mask
     */
    public static String getEthernetMask() {
        return getPropInfo(ETHERNET_MASK);
    }

    /**
     * Get wlan mask
     */
    public static String getWlanMask() {
        return getPropInfo(WLAN_MASK);
    }

    /**
     * Get prop information by different interface name
     */
    private static String getPropInfo(String interfaceName) {
        String re = "";
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            Properties pr = new Properties();
            pr.load(process.getInputStream());
            re = pr.getProperty(interfaceName, "");
            if (!TextUtils.isEmpty(re) && re.length() > 6) {
                re = re.substring(1, re.length() - 1);
                return re;
            }
        } catch (IOException e) {
            com.orhanobut.logger.Logger.e(e.toString());
        }
        return re;
    }
}
