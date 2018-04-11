package com.csx.pool.proxy;

import com.csx.pool.UsageTracking;

/**
 * @author csx
 * @Package com.csx.pool.proxy
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public interface ProxySource<T> {
    T createProxy(T pooledObject, UsageTracking<T> usageTracking);

    T resolveProxy(T proxy);

}
