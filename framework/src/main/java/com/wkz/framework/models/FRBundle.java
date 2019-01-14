package com.wkz.framework.models;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;

public class FRBundle implements Cloneable, Parcelable {

    private Bundle mBundle;

    public FRBundle() {
        mBundle = new Bundle();
    }

    protected FRBundle(Parcel in) {
        mBundle = in.readParcelable(Bundle.class.getClassLoader());
    }

    public static final Creator<FRBundle> CREATOR = new Creator<FRBundle>() {
        @Override
        public FRBundle createFromParcel(Parcel in) {
            return new FRBundle(in);
        }

        @Override
        public FRBundle[] newArray(int size) {
            return new FRBundle[size];
        }
    };

    public FRBundle putBoolean(String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    public FRBundle putByte(String key, byte value) {
        mBundle.putByte(key, value);
        return this;
    }

    public FRBundle putChar(String key, char value) {
        mBundle.putChar(key, value);
        return this;
    }

    public FRBundle putShort(String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }

    public FRBundle putInt(String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    public FRBundle putLong(String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    public FRBundle putFloat(String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }

    public FRBundle putDouble(String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    public FRBundle putString(String key, String value) {
        mBundle.putString(key, value);
        return this;
    }

    public FRBundle putCharSequence(String key, CharSequence value) {
        mBundle.putCharSequence(key, value);
        return this;
    }

    public FRBundle putParcelable(String key, Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    public FRBundle putSize(String key, Size value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBundle.putSize(key, value);
        }
        return this;
    }

    public FRBundle putSizeF(String key, SizeF value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBundle.putSizeF(key, value);
        }
        return this;
    }

    public FRBundle putParcelableArray(String key, Parcelable[] value) {
        mBundle.putParcelableArray(key, value);
        return this;
    }

    public FRBundle putParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public FRBundle putSparseParcelableArray(String key, SparseArray<? extends Parcelable> value) {
        mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public FRBundle putIntegerArrayList(String key, ArrayList<Integer> value) {
        mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public FRBundle putStringArrayList(String key, ArrayList<String> value) {
        mBundle.putStringArrayList(key, value);
        return this;
    }

    public FRBundle putCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        mBundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public FRBundle putSerializable(String key, Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    public FRBundle putByteArray(String key, byte[] value) {
        mBundle.putByteArray(key, value);
        return this;
    }

    public FRBundle putShortArray(String key, short[] value) {
        mBundle.putShortArray(key, value);
        return this;
    }

    public FRBundle putCharArray(String key, char[] value) {
        mBundle.putCharArray(key, value);
        return this;
    }

    public FRBundle putFloatArray(String key, float[] value) {
        mBundle.putFloatArray(key, value);
        return this;
    }

    public FRBundle putCharSequenceArray(String key, CharSequence[] value) {
        mBundle.putCharSequenceArray(key, value);
        return this;
    }

    public FRBundle putBundle(String key, Bundle value) {
        mBundle.putBundle(key, value);
        return this;
    }

    public FRBundle putBinder(String key, IBinder value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBundle.putBinder(key, value);
        }
        return this;
    }

    public Bundle create() {
        return mBundle;
    }

    @Override
    protected Object clone() {
        return new FRBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mBundle, flags);
    }
}
