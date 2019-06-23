package com.wkz.videoplayer.window;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/2/10
 *     desc  : 用于在内部自动申请权限
 *     revise:
 * </pre>
 */
public class FRPermissionActivity extends AppCompatActivity {

    private static List<OnPermissionListener> mPermissionListenerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23){
            requestAlertWindowPermission();
        }
    }

    @RequiresApi(api = 23)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);
    }


    @RequiresApi(api = 23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 23){
            //用23以上编译即可出现canDrawOverlays
            if (FRWindowUtils.hasPermission(this)) {
                mPermissionListener.onSuccess();
            } else {
                mPermissionListener.onFail();
            }
        }
        finish();
    }

    static synchronized void request(Context context, OnPermissionListener permissionListener) {
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList<>();
            mPermissionListener = new OnPermissionListener() {
                @Override
                public void onSuccess() {
                    for (OnPermissionListener listener : mPermissionListenerList) {
                        listener.onSuccess();
                    }
                }

                @Override
                public void onFail() {
                    for (OnPermissionListener listener : mPermissionListenerList) {
                        listener.onFail();
                    }
                }
            };
            Intent intent = new Intent(context, FRPermissionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        mPermissionListenerList.add(permissionListener);
    }


    private static OnPermissionListener mPermissionListener;


}
