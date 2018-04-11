package com.csx.pool.impl;

import com.csx.pool.PooledObject;
import com.csx.pool.PooledObjectState;

import java.awt.*;
import java.io.PrintWriter;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public class DefaultPooledObject<T> implements PooledObject<T> {

    private final T object;
    private PooledObjectState state=PooledObjectState.IDLE;
    private final long createTime=System.currentTimeMillis();
    private volatile long lastBorrowTime = createTime;
    private volatile long lastUseTime = createTime;
    private volatile long lastReturnTime = createTime;
    private volatile boolean logAbandoned = false;
    private volatile Exception borrowedBy = null;
    private volatile Exception usedBy = null;
    private volatile long borrowedCount = 0;

    public DefaultPooledObject(T object) {
        this.object = object;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getActiveTimeMillis() {
        long rTime=lastReturnTime;
        long bTime=lastBorrowTime;

        if(rTime>bTime){
            return rTime-bTime;
        }else{
            return System.currentTimeMillis()-bTime;
        }
    }

    @Override
    public long getIdleTimeMillis() {
        final long elapsed=System.currentTimeMillis()-lastReturnTime;
        return elapsed>0?elapsed:0;
    }

    @Override
    public long getLastBorrowTime() {
        return lastBorrowTime;
    }

    @Override
    public long getLastReturnTime() {
        return lastReturnTime;
    }

    @Override
    public long getLastUsedTime() {
        return lastUseTime;
    }

    @Override
    public int compareTo(PooledObject<T> o) {
        return 0;
    }

    @Override
    public boolean startEvictionTest() {
        return false;
    }

    @Override
    public boolean endEvictionTest() {
        return false;
    }

    @Override
    public boolean allocate() {
        return false;
    }

    @Override
    public boolean deallocate() {
        return false;
    }

    @Override
    public void invalidate() {

    }

    @Override
    public void setLogAbandoned(boolean logAbandoned) {

    }

    @Override
    public void use() {

    }

    @Override
    public void printStackTrace(PrintWriter writer) {

    }

    @Override
    public PooledObjectState getState() {
        return null;
    }

    @Override
    public void markAbandoned() {

    }

    @Override
    public void markReturning() {

    }
}
