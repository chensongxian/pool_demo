package com.csx.pool;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public interface UsageTracking<T> {
    void use(T pooledObject);
}
