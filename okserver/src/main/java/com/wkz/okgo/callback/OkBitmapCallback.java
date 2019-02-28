package com.wkz.okgo.callback;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wkz.okgo.converter.OkBitmapConverter;

import okhttp3.Response;

/**
 * 返回图片的Bitmap，这里没有进行图片的缩放，可能会发生 OOM
 */
public abstract class OkBitmapCallback extends OkAbsCallback<Bitmap> {

    private OkBitmapConverter convert;

    public OkBitmapCallback() {
        convert = new OkBitmapConverter();
    }

    public OkBitmapCallback(int maxWidth, int maxHeight) {
        convert = new OkBitmapConverter(maxWidth, maxHeight);
    }

    public OkBitmapCallback(int maxWidth, int maxHeight, Bitmap.Config decodeConfig, ImageView.ScaleType scaleType) {
        convert = new OkBitmapConverter(maxWidth, maxHeight, decodeConfig, scaleType);
    }

    @Override
    public Bitmap convertResponse(Response response) throws Throwable {
        Bitmap bitmap = convert.convertResponse(response);
        response.close();
        return bitmap;
    }
}
