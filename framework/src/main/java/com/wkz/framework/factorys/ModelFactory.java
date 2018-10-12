package com.wkz.framework.factorys;

import com.wkz.framework.base.BaseModel;

public class ModelFactory {

    /**
     * 实例化Model对象
     */
    public static <T extends BaseModel> T createModel(final Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
