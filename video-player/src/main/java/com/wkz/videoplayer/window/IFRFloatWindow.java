package com.wkz.videoplayer.window;

import android.view.View;


public abstract class IFRFloatWindow {

    public abstract void show();

    public abstract void hide();

    public abstract int getX();

    public abstract int getY();

    public abstract void updateX(int x);

    public abstract void updateX(@FRWindowScreen.screenType int screenType, float ratio);

    public abstract void updateY(int y);

    public abstract void updateY(@FRWindowScreen.screenType int screenType, float ratio);

    public abstract View getView();

    abstract void dismiss();
}
