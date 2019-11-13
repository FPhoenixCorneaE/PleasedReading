//package com.wkz.pleasedreading.application;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.util.Log;
//
//import com.qihoo360.replugin.PluginDexClassLoader;
//import com.qihoo360.replugin.RePlugin;
//import com.qihoo360.replugin.RePluginCallbacks;
//import com.qihoo360.replugin.RePluginConfig;
//import com.qihoo360.replugin.RePluginEventCallbacks;
//import com.qihoo360.replugin.model.PluginInfo;
//import com.qihoo360.replugin.utils.Dex2OatUtils;
//import com.qihoo360.replugin.utils.InterpretDex2OatHelper;
//import com.wkz.framework.FRApplication;
//import com.wkz.pleasedreading.BuildConfig;
//
//import java.io.File;
//
//public class PRHostRePluginApplication extends FRApplication {
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        RePluginConfig rePluginConfig = createConfig();
//        RePluginCallbacks rePluginCallbacks = new HostCallbacks(this);
//        rePluginConfig.setCallbacks(rePluginCallbacks);
//        RePlugin.App.attachBaseContext(this, rePluginConfig);
//        // FIXME 允许接收rpRunPlugin等Gradle Task，发布时请务必关掉，以免出现问题
//        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        RePlugin.App.onCreate();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onLowMemory();
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onTrimMemory(level);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration config) {
//        super.onConfigurationChanged(config);
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onConfigurationChanged(config);
//    }
//
//    /**
//     * RePlugin允许提供各种“自定义”的行为，让您“无需修改源代码”，即可实现相应的功能
//     */
//    private RePluginConfig createConfig() {
//        RePluginConfig c = new RePluginConfig();
//
//        // 允许“插件使用宿主类”。默认为“关闭”
//        c.setUseHostClassIfNotFound(true);
//
//        // FIXME RePlugin默认会对安装的外置插件进行签名校验，这里先关掉，避免调试时出现签名错误
//        c.setVerifySign(!BuildConfig.DEBUG);
//
//        // 针对“安装失败”等情况来做进一步的事件处理
//        c.setEventCallbacks(new HostEventCallbacks(this));
//
//        // FIXME 若宿主为Release，则此处应加上您认为"合法"的插件的签名，例如，可以写上"宿主"自己的。
//        RePlugin.addCertSignature("AAAAAAAAA");
//
//        // 在Art上，优化第一次loadDex的速度
//        c.setOptimizeArtLoadDex(true);
//        return c;
//    }
//
//    /**
//     * 宿主针对RePlugin的自定义行为
//     */
//    private static class HostCallbacks extends RePluginCallbacks {
//
//        private static final String TAG = "HostCallbacks";
//
//        private HostCallbacks(Context context) {
//            super(context);
//        }
//
//        @Override
//        public boolean onPluginNotExistsForActivity(Context context, String plugin, Intent intent, int process) {
//            // FIXME 当插件"没有安装"时触发此逻辑，可打开您的"下载对话框"并开始下载。
//            // FIXME 其中"intent"需传递到"对话框"内，这样可在下载完成后，打开这个插件的Activity
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "onPluginNotExistsForActivity: Start download... p=" + plugin + "; i=" + intent);
//            }
//            return super.onPluginNotExistsForActivity(context, plugin, intent, process);
//        }
//
//        @Override
//        public PluginDexClassLoader createPluginClassLoader(PluginInfo pi, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
//            String odexName = pi.makeInstalledFileName() + ".dex";
//            if (RePlugin.getConfig().isOptimizeArtLoadDex()) {
//                Dex2OatUtils.injectLoadDex(dexPath, optimizedDirectory, odexName);
//            }
//
//            long being = System.currentTimeMillis();
//            PluginDexClassLoader pluginDexClassLoader = super.createPluginClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent);
//
//            if (BuildConfig.DEBUG) {
//                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader use:" + (System.currentTimeMillis() - being));
//                String odexAbsolutePath = (optimizedDirectory + File.separator + odexName);
//                Log.d(Dex2OatUtils.TAG, "createPluginClassLoader odexSize:" + InterpretDex2OatHelper.getOdexSize(odexAbsolutePath));
//            }
//
//            return pluginDexClassLoader;
//        }
//    }
//
//    private static class HostEventCallbacks extends RePluginEventCallbacks {
//
//        private static final String TAG = "HostEventCallbacks";
//
//        public HostEventCallbacks(Context context) {
//            super(context);
//        }
//
//        @Override
//        public void onInstallPluginFailed(String path, InstallResult code) {
//            // FIXME 当插件安装失败时触发此逻辑。您可以在此处做“打点统计”，也可以针对安装失败情况做“特殊处理”
//            // 大部分可以通过RePlugin.install的返回值来判断是否成功
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "onInstallPluginFailed: Failed! path=" + path + "; r=" + code);
//            }
//            super.onInstallPluginFailed(path, code);
//        }
//
//        @Override
//        public void onStartActivityCompleted(String plugin, String activity, boolean result) {
//            // FIXME 当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
//            super.onStartActivityCompleted(plugin, activity, result);
//        }
//    }
//}
