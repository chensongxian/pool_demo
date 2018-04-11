package com.csx.pool;

import java.util.NoSuchElementException;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public abstract class BaseObjectPool<T> implements ObjectPool<T> {
    @Override
    public abstract T borrowObject() throws Exception;
    @Override
    public abstract void returnObject(T obj) throws Exception;

    @Override
    public abstract void invalidateObject(T obj) throws Exception;

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumIdle() {
        return -1;
    }

    @Override
    public int getNumActive() {
        return -1;
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {

    }

    @Override
    public void close() {
        closed=true;
    }


    public final boolean isClosed(){
        return closed;
    }

    protected final void assertOpen()throws IllegalStateException{
        if(isClosed()){
            throw new IllegalStateException("pool not open");
        }
    }
    private volatile boolean closed=false;
}
