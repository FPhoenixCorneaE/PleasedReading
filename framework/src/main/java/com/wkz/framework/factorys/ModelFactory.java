package com.wkz.framework.factorys;

import com.orhanobut.logger.Logger;
import com.wkz.framework.base.IFRBaseModel;

public class ModelFactory {

    /**
     * 实例化Model对象
     */
    public static <T extends IFRBaseModel> T createModel(final Class<T> tClass) {
        try {
            //class.newInstance()是通过无参构造函数实例化的，一个对象默认是有一个无参构造函数，
            //如果有一个有参构造函数，无参构造函数就不存在了，在通过反射获得对象会抛出 java.lang.InstantiationException 异常。
            return tClass.newInstance();
        } catch (IllegalAccessException e) {
            Logger.e(e.toString());
        } catch (InstantiationException e) {
            Logger.e(e.toString());
        }
        return null;
    }
}
