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

-dontwarn com.wkz.videoplayer.**
-keep class com.wkz.videoplayer.constant.**{*;}
-keep class com.wkz.videoplayer.controller.**{
    public <methods>;
}
-keep class com.wkz.videoplayer.inter.**{*;}
-keep class com.wkz.videoplayer.manager.FRVideoPlayerManager{
    public <methods>;
}
-keep class com.wkz.videoplayer.player.FRVideoPlayer{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRFloatPlayerView{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRFloatPlayerView$*{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRFloatWindow{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRFloatWindow$*{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRWindowUtils{
    public <methods>;
}
-keep class com.wkz.videoplayer.window.FRMoveType{*;}
-keep class com.wkz.videoplayer.window.FRWindowScreen{*;}
-keep class com.wkz.videoplayer.window.IFRFloatWindow{*;}

# 本地代码通过反射调用其他的类，但是经过了混淆之后，就会出现异常：ClassNotFoundException,NoSuchMethodError,InvocationTargetException...
# 调用了JNI之后，C或者C++和java代码进行交互的时候找不到java的类或者方法，导致发生了异常......等等，还有好多
# 只需要将被调用的java类标注为不混淆即可
-keep class tv.danmaku.ijk.media.**{*;}



