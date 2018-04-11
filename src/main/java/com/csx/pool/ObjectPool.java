package com.csx.pool;

import sun.reflect.generics.tree.VoidDescriptor;

import java.util.NoSuchElementException;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public interface ObjectPool<T> {
    /**
     * 从对象池中借出一个对象实例
     *
     * @return
     * @throws Exception
     * @throws NoSuchElementException
     * @throws IllegalStateException
     */
    T borrowObject() throws Exception, NoSuchElementException, IllegalStateException;

    /**
     * 返回一个对象实例到对象池中
     *
     * @param obj
     * @throws Exception
     */
    void returnObject(T obj) throws Exception;

    /**
     * 校验一个来自对象池的对象
     *
     * @param obj
     * @throws Exception
     */
    void invalidateObject(T obj) throws Exception;

    /**
     * 使用工厂创建一个对象，钝化并且将它放入空闲对象池
     * @throws Exception
     * @throws IllegalStateException
     * @throws UnsupportedOperationException
     */
    void addObject() throws Exception, IllegalStateException,
            UnsupportedOperationException;

    /**
     *返回池中空闲的对象数量。有可能是池中可供借出对象的近似值。如果这个信息无效，返回一个负数
     * @return
     */
    int getNumIdle();

    /**
     * 返回从借出的对象数量。如果这个信息不可用，返回一个负数
     * @return
     */
    int getNumActive();

    /**
     * 清除池中的所有空闲对象，释放其关联的资源（可选）。
     * 清除空闲对象必须使用PooledObjectFactory.destroyObject方法
     * @throws Exception
     * @throws UnsupportedOperationException
     */
    void clear() throws Exception, UnsupportedOperationException;

    /**
     * 关闭池并释放关联资源
     */
    void close();
}
