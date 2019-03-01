package com.wkz.framework.listeners;

public interface OnFRHomeKeyListener {
    /**
     * 按Home按键
     */
    void onClickHome();

    /**
     * 最近任务键也就是菜单键
     */
    void onClickRecents();

    /**
     * 长按Home按键
     */
    void onLongClickHome();

    /**
     * 锁屏
     */
    void onLockScreen();
}
