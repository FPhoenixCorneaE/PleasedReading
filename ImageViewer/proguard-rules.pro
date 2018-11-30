# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.wkz.viewer.**
-keep class com.wkz.viewer.dragger.OnImageDraggerStateListener
-keep class com.wkz.viewer.widget.IImageViewer
-keep class com.wkz.viewer.IImageLoader
-keep class com.wkz.viewer.listener.**
# 保持自定义控件类不被混淆
-keep  class com.wkz.viewer.widget.FRImageViewerAttacher {
    public <methods>;
}

