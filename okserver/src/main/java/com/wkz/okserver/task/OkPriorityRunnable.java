package com.wkz.okserver.task;

/**
 * Runnable对象的优先级封装
 */
public class OkPriorityRunnable extends OkPriorityObject<Runnable> implements Runnable {

    public OkPriorityRunnable(int priority, Runnable obj) {
        super(priority, obj);
    }

    @Override
    public void run() {
        this.obj.run();
    }
}
