package com.csx.pool.impl;

import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: TODO
 * @date 2018/4/13 0013
 */
public class InterruptibleReentrantLock extends ReentrantLock {
    public InterruptibleReentrantLock(boolean fair) {
        super(fair);
    }

    public void interruptWaiters(Condition condition){
        Collection<Thread> threads=getWaitingThreads(condition);
        for(Thread thread:threads){
            thread.interrupt();
        }
    }
}
