package com.wkz.dialog.widget;

import android.content.Context;
import android.view.View;

import com.wkz.dialog.base.popup.CoolBaseBubblePopup;

/**
 * Use dialog to realize bubble style popup(利用Dialog实现泡泡样式的弹窗)
 * thanks https://github.com/michaelye/EasyDialog
 */
public class CoolBubblePopup extends CoolBaseBubblePopup<CoolBubblePopup> {

    public CoolBubblePopup(Context context, View wrappedView) {
        super(context, wrappedView);
    }

    @Override
    public View onCreateBubbleView() {
        return null;
    }
}
