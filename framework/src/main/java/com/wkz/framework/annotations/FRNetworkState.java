package com.wkz.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wkz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface FRNetworkState {
    /**
     * 无网络
     */
    int Unavailable = 0;
    /**
     * Wifi网络连接
     */
    int WifiActive = 1;
    /**
     * 手机网络连接
     */
    int MobileActive = 2;
}
