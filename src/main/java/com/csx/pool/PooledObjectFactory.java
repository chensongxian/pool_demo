package com.csx.pool;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: 定义了操作PooledObject实例生命周期的一些方法
 * @date 2018/4/10 0010
 */
public interface PooledObjectFactory<T> {
    /**
     * 生成一个新的PoolObject实例
     * @return
     * @throws Exception
     */
    PooledObject<T> makeObject() throws Exception;

    /**
     * 在池中销毁一个不再使用的对象实例
     * @param p
     * @throws Exception
     */
    void destroyObject(PooledObject<T> p) throws Exception;

    /**
     * 可能用于从池中借出对象时，对处于激活（activated）状态的ObjectPool实例进行测试确保它是有效的。
     * 也有可能在ObjectPool实例返还池中进行钝化前调用进行测试是否有效。它只对处于激活状态的实例调用
     * @param p
     * @return
     * @throws Exception
     */
    boolean validateObject(PooledObject<T> p) throws Exception;

    /**
     * 把重新初始化的对象返回到对象池
     * @param p
     * @throws Exception
     */
    void activateObject(PooledObject<T> p) throws Exception;

    /**
     * 把未初始化的对象返回到空闲的对象池
     * @param p
     * @throws Exception
     */
    void passivateObject(PooledObject<T> p) throws Exception;
}
