package com.wkz.videoplayer.window;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import android.view.WindowManager;


public class FRWindowUtils {


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Context context) {
        return Settings.canDrawOverlays(context);
    }


    private static Point sPoint;

    static int getScreenWidth(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.getDefaultDisplay().getSize(sPoint);
            }
        }
        return sPoint.x;
    }

    static int getScreenHeight(Context context) {
        if (sPoint == null) {
            sPoint = new Point();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.getDefaultDisplay().getSize(sPoint);
            }
        }
        return sPoint.y;
    }
}
