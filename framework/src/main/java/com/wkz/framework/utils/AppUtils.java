package com.wkz.framework.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;

import com.orhanobut.logger.Logger;
import com.wkz.framework.FRApplication;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.security.auth.x500.X500Principal;

/**
 * App 相关信息，包括版本名称、版本号、包名等等
 */
public class AppUtils {

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    /**
     * Get version name
     */
    public static String getVersionName() {
        Context context = FRApplication.getContext();
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return "";
    }

    /**
     * Get version code
     */
    public static int getVersionCode() {
        Context context = FRApplication.getContext();
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return 0;
    }

    /**
     * Get package name
     */
    public static String getPackageName() {
        Context context = FRApplication.getContext();
        return context.getPackageName();
    }

    /**
     * Get icon
     */
    public static Drawable getAppIcon() {
        return getAppIcon(getPackageName());
    }

    /**
     * Get app icon
     */
    public static Drawable getAppIcon(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Get app version name
     */
    public static String getVersionName(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Get app version code
     */
    public static int getVersionCode(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return -1;
    }

    /**
     * Get app name
     */
    public static String getAppName(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Get app name
     */
    public static String getAppName() {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Get app permission
     */
    public static String[] getAppPermission(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            return packageInfo.requestedPermissions;
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Get app signature
     */
    public static String getAppSignature(String packageName) {
        Context context = FRApplication.getContext();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return packageInfo.signatures[0].toCharsString();
        } catch (NameNotFoundException e) {
            Logger.e(e.toString());
        }
        return null;
    }

    /**
     * Judge whether an app is dubuggable
     */
    public static boolean isDebuggable() {
        Context context = FRApplication.getContext();
        boolean debuggable = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = packageInfo.signatures;
            for (Signature signature : signatures) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf
                        .generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }

        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return debuggable;
    }

    /**
     * Judge whether an app is in background
     */
    public static boolean isAppInBackground() {
        Context context = FRApplication.getContext();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = null;
        if (am != null) {
            taskList = am.getRunningTasks(1);
        }
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            return topActivity != null
                    && !topActivity.getPackageName().equals(
                    context.getPackageName());
        }
        return false;
    }
}
