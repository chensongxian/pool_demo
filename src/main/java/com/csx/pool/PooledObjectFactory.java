package com.csx.pool;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/10 0010
 */
public interface PooledObjectFactory<T> {
    PooledObject<T> makeObject() throws Exception;


    void destroyObject(PooledObject<T> p) throws Exception;

    boolean validateObject(PooledObject<T> p) throws Exception;

    void activateObject(PooledObject<T> p) throws Exception;

    void passivateObject(PooledObject<T> p) throws Exception;
}
