# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-ignorewarnings

#---------------------------------------------------------------------------
# WebView
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-keepclassmembers class fqcn.of.javascript.interface.for.Webview { public *; }
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
#----------------------------------------------------------------------------


#######################     常用第三方模块的混淆选项         ###################################

#----------------------------------------------------------------------------
# okhttp3
-keep class okhttp3.internal.**{*;}
-dontwarn okhttp3.**
-dontwarn okio.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# nineoldandroids-2.4.0.jar
-keep public class com.nineoldandroids.** {*;}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# Retrofit2
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn retrofit2.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# universal-image-loader
-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn com.nostra13.universalimageloader.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# butterknife
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-dontwarn butterknife.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
    public void xxxxxx(**);
}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepattributes EnclosingMethod
-dontwarn com.google.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# picasso
-keep class com.squareup.picasso.** {*; }
-dontwarn com.squareup.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# GreenDao
-keep class de.greenrobot.dao.** {*;}
-keep class **$Properties
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}
-dontwarn org.greenrobot.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# rxjava
-dontwarn rx.*
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# RxAndroid
-dontwarn sun.misc.**

#解决在6.0系统出现java.lang.InternalError
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# AndroidEventBus
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# RxCache
-keep class io.rx_cache.internal.Record { *; }
-keep class io.rx_cache.Source { *; }
-dontwarn io.rx_cache.internal.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# BigImageView
-dontwarn com.github.piasy.**
#----------------------------------------------------------------------------

#----------------------------------------------------------------------------
# rebound-core.jar
#-libraryjars   libs/rebound-core.jar
-keep class com.facebook.rebound.** { *; }
-keep interface com.facebook.rebound.** { *; }
-dontwarn com.facebook.rebound.**
#----------------------------------------------------------------------------