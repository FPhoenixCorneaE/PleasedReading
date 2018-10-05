package com.wkz.framework.utils;

import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.support.v4.content.ContextCompat;

import com.wkz.framework.PRApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtils {

    public static int getLayoutId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "layout",
                PRApplication.getContext().getPackageName());
    }

    public static int getStringId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "string",
                PRApplication.getContext().getPackageName());
    }

    public static int getDrawableId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "drawable",
                PRApplication.getContext().getPackageName());
    }

    public static int getMipmapId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "mipmap",
                PRApplication.getContext().getPackageName());
    }

    public static int getStyleId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "style",
                PRApplication.getContext().getPackageName());
    }

    public static int getId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "id",
                PRApplication.getContext().getPackageName());
    }

    public static int getColorId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "color",
                PRApplication.getContext().getPackageName());
    }

    public static int getArrayId(String name) {
        return PRApplication.getContext().getResources().getIdentifier(name, "array",
                PRApplication.getContext().getPackageName());
    }

    /**
     * Get raw file, ui/raw/file
     */
    public static InputStream getRaw(@RawRes int resId) {
        return PRApplication.getContext().getResources().openRawResource(resId);
    }

    /**
     * Get raw file descriptor, ui/raw/file. This function only works for resources that are stored in the package as
     * uncompressed data, which typically includes things like mp3 files and png images.
     */
    public static AssetFileDescriptor getRawFd(@RawRes int resId) {
        return PRApplication.getContext().getResources().openRawResourceFd(resId);
    }

    /**
     * Get raw text file, ui/raw/text
     */
    public String getRawText(@RawRes int resId) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getRaw(resId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get xml file, ui/xml/file
     */
    public static XmlResourceParser getXml(@XmlRes int resId) {
        return PRApplication.getContext().getResources().getXml(resId);
    }

    /**
     * Get drawable, ui/drawable/file
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(PRApplication.getContext(), resId);
    }

    /**
     * Get string, ui/values/__picker_strings.xml
     */
    public static String getString(@StringRes int resId) {
        return PRApplication.getContext().getResources().getString(resId);
    }

    /**
     * Get string array, ui/values/__picker_strings.xml
     */
    public static String[] getStringArray(@ArrayRes int resId) {
        return PRApplication.getContext().getResources().getStringArray(resId);
    }

    /**
     * Get int array, ui/values/__picker_strings.xml
     */
    public static int[] getIntArray(@ArrayRes int resId) {
        return PRApplication.getContext().getResources().getIntArray(resId);
    }

    /**
     * Get color, ui/values/__picker_colors.xml
     */
    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(PRApplication.getContext(), resId);
    }

    /**
     * Get color state list, ui/values/__picker_colors.xml
     */
    public static ColorStateList getColorStateList(@ColorRes int resId) {
        return ContextCompat.getColorStateList(PRApplication.getContext(), resId);
    }

    /**
     * Get dimension, ui/values/dimens.xml
     *
     * @return View dimension value multiplied by the appropriate metric.
     * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘   返回float
     */
    public static float getDimension(@DimenRes int resId) {
        return PRApplication.getContext().getResources().getDimension(resId);
    }

    /**
     * Get dimension, ui/values/dimens.xml
     *
     * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
     * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘  返回int
     */
    public static int getDimensionPixelOffset(@DimenRes int resId) {
        return PRApplication.getContext().getResources().getDimensionPixelOffset(resId);
    }

    /**
     * Get dimension, ui/values/dimens.xml
     *
     * @return View dimension value multiplied by the appropriate metric and truncated to integer pixels.
     * 不管写的是dp还是sp还是px,都会乘以density.
     */
    public static int getDimensionPixelSize(@DimenRes int resId) {
        return PRApplication.getContext().getResources().getDimensionPixelSize(resId);
    }
}