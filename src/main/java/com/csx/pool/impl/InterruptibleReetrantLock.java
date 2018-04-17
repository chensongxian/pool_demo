package com.csx.pool.impl;

import sun.reflect.generics.tree.VoidDescriptor;

import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @author: csx
 * @Date: 2018-04-12
 */
public class InterruptibleReetrantLock extends ReentrantLock{
    private static final long serialVersionUID = 1944106427288464565L;

    public InterruptibleReetrantLock(boolean fair) {
        super(fair);
    }

    public void interruptWaiters(Condition condition){
        Collection<Thread> threads=getWaitingThreads(condition);
        for(Thread thread:threads){
            thread.interrupt();
        }
    }
}
