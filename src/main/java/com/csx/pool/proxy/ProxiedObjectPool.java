package com.csx.pool.proxy;


import com.csx.pool.ObjectPool;

import java.util.NoSuchElementException;

/**
 * @author csx
 * @Package com.csx.pool.proxy
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public class ProxiedObjectPool<T> implements ObjectPool<T> {

    private final ObjectPool<T> pool;
    private final ProxySource<T> proxySource;

    public ProxiedObjectPool(ObjectPool<T> pool, ProxySource<T> proxySource) {
        this.pool = pool;
        this.proxySource = proxySource;
    }

    @Override
    public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        return null;
    }

    @Override
    public void returnObject(T obj) throws Exception {

    }

    @Override
    public void invalidateObject(T obj) throws Exception {

    }

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {

    }

    @Override
    public int getNumIdle() {
        return 0;
    }

    @Override
    public int getNumActive() {
        return 0;
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {

    }

    @Override
    public void close() {

    }
}
