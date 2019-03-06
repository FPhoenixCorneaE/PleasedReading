package com.wkz.dialog.entity;

import java.io.Serializable;

public class CoolDialogMenuItem implements Serializable {
    public String mOperName;
    public int mResId;

    public CoolDialogMenuItem(String operName, int resId) {
        mOperName = operName;
        mResId = resId;
    }
}
