package com.wkz.framework.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wkz.framework.FRApplication;
import com.wkz.framework.constants.FRConstant;
import com.wkz.framework.models.FRActivityAnimator;

import java.io.File;

/**
 * Intent操作
 */
public class IntentUtils {

    /**
     * 显示意图，不传参
     */
    public static void startActivity(Context context, Class<?> className) {
        startActivity(context, className, null);
    }

    /**
     * 显示意图,传参,不带动画
     */
    public static void startActivity(Context context, Class<?> className, Bundle bundle) {
        startActivity(context, className, bundle, FRActivityAnimator.Animator.PULL_RIGHT_PUSH_LEFT);
    }

    /**
     * 显示意图,传参,切换动画
     */
    public static void startActivity(Context context, Class<?> className, Bundle bundle, int enterAnim, int exitAnim) {
        startActivityForResult(context, className, Integer.MIN_VALUE, bundle, enterAnim, exitAnim);
    }

    /**
     * 显示意图,传参,切换动画
     */
    public static void startActivity(Context context, Class<?> className, Bundle bundle, String animation) {
        startActivityForResult(context, className, Integer.MIN_VALUE, bundle, animation);
    }

    /**
     * 显示意图,附带请求码
     */
    public static void startActivityForResult(Context context, Class<?> className, int requestCode) {
        startActivityForResult(context, className, requestCode, null);
    }

    /**
     * 显示意图，附带请求码，传参,不带动画
     */
    public static void startActivityForResult(Context context, Class<?> className, int requestCode, Bundle bundle) {
        startActivityForResult(context, className, requestCode, bundle, null);
    }

    /**
     * 显示意图，附带请求码，传参,切换动画
     */
    public static void startActivityForResult(Context context, Class<?> className, int requestCode, Bundle bundle, int enterAnim, int exitAnim) {
        if (context == null) return;

        if (context instanceof Activity) {
            Intent intent = new Intent();
            intent.setClass(context, className);
            if (bundle != null) intent.putExtras(bundle);

            if (Integer.MIN_VALUE == requestCode) {
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }
    }

    /**
     * 显示意图，附带请求码，传参,切换动画
     */
    public static void startActivityForResult(Context context, Class<?> className, int requestCode, Bundle bundle, String animation) {
        if (context == null) return;

        if (context instanceof Activity) {
            Intent intent = new Intent();
            intent.setClass(context, className);
            if (bundle == null) bundle = new Bundle();
            bundle.putString(FRConstant.ACTIVITY_ANIMATION, animation);
            intent.putExtras(bundle);

            if (Integer.MIN_VALUE == requestCode) {
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
            //进入动画
            try {
                if (!TextUtils.isEmpty(animation)) {
                    FRActivityAnimator anim = new FRActivityAnimator();
                    anim.getClass().getMethod(animation, Activity.class).invoke(anim, context);
                }
            } catch (Exception e) {
                Logger.e(e.toString());
            }
        }
    }

    /**
     * 隐式意图，不传参
     */
    public static void startImplicitActivity(Context context, String action) {
        startImplicitActivity(context, action, null);
    }

    /**
     * 隐式意图，传参
     */
    public static void startImplicitActivity(Context context, String action, Bundle bundle) {
        if (context == null) return;

        Intent intent = new Intent(action);
        if (bundle != null) intent.putExtras(bundle);

        context.startActivity(intent);
    }

    /**
     * 隐示意图,附带请求码
     */
    public static void startImplicitActivityForResult(Context context, String action, int requestCode) {
        startImplicitActivityForResult(context, action, requestCode, null);
    }

    /**
     * 隐示意图，附带请求码，传参
     */
    public static void startImplicitActivityForResult(Context context, String action, int requestCode, Bundle bundle) {
        if (context == null) return;

        Intent intent = new Intent(action);
        if (bundle != null) intent.putExtras(bundle);

        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 根据包名打开软件
     */
    public static void startApplication(Context context, String appPackageName) {
        if (null == context || null == appPackageName || appPackageName.isEmpty()) return;

        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 启动服务
     *
     * @param context     上下文
     * @param serviceName 服务名字
     */
    public static void startService(Context context, Class<?> serviceName) {
        startService(context, serviceName, null);
    }

    /**
     * 启动服务
     *
     * @param context     上下文
     * @param serviceName 服务名字
     * @param bundle      捆绑的数据
     */
    public static void startService(Context context, Class<?> serviceName, Bundle bundle) {
        if (context == null) return;

        Intent intent = new Intent();
        intent.setClass(context, serviceName);
        if (bundle != null) intent.putExtras(bundle);

        context.startService(intent);
    }

    /**
     * Search a word in a browser
     */
    public static void openBrowserSearch(Context context, String string) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, string);
        context.startActivity(intent);
    }

    /**
     * Open url in a browser
     */
    public static void openBrowser(Context context, String url) {
        if (context == null || url == null || url.isEmpty()) return;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * Open map in a map app
     */
    public static void openMap(Context context, String parh) {
        if (context == null || parh == null || parh.isEmpty()) return;
        Uri uri = Uri.parse(parh);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * Open dial
     */
    public static void openDial(Context context) {
        if (context == null) return;
        Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
        context.startActivity(intent);
    }

    /**
     * Open dial with a number
     */
    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * Call up, requires Permission "android.permission.CALL_PHONE"
     */
    @SuppressLint("MissingPermission")
    public static void openCall(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            return;

        context.startActivity(intent);
    }

    /**
     * Send message
     */
    public static void sendMessage(Context context, String sendNo, String sendContent) {
        Uri uri = Uri.parse("smsto:" + sendNo);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", sendContent);
        context.startActivity(intent);
    }

    /**
     * Open contact person
     */
    public static void openContacts(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        context.startActivity(intent);
    }

    /**
     * Open system settings
     */
    public static void openSettings(Context context) {
        openSettings(context, Settings.ACTION_SETTINGS);
    }

    /**
     * Open system settings
     *
     * @param action The action contains global system-level device preferences.
     */
    public static void openSettings(Context context, String action) {
        if (null == context) return;

        if (!TextUtils.isEmpty(action)) {
            Intent intent = new Intent(action);
            context.startActivity(intent);
        } else {
            openSettings(context);
        }
    }

    /**
     * Open camera
     */
    public static void openCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivity(intent);
    }

    /**
     * Take camera, this photo data will be returned in onActivityResult()
     */
    public static void openCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Choose photo, this photo data will be returned in onActivityResult()
     */
    public static void openPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Open App Detail page
     */
    public static void openAppDetail(String packageName, Context context) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
        } else {
            final String appPkgName = (apiLevel == 8 ? "pkg" : "com.android.settings.ApplicationPkgName");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param apkPath apk路径
     */
    public static void installApk(String apkPath) {
        //下载完成安装,安装完成后返回显示启动
        File apkFile = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        //安卓版本大于7.0适配-应用之间共享文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //此处的authority的值必须与Manifest中provider组件设置的authorities值一致
            String authority = AppUtils.getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(FRApplication.getContext(), authority, apkFile);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        FRApplication.getContext().startActivity(intent);
    }
}
