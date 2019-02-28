package com.wkz.okserver.task;

import java.io.Serializable;

/**
 * 具有优先级对象的公共类
 */
public class OkPriorityObject<E> implements Serializable {

    private static final long serialVersionUID = 6913138600224913719L;

    public final int priority;
    public final E obj;

    public OkPriorityObject(int priority, E obj) {
        this.priority = priority;
        this.obj = obj;
    }
}
