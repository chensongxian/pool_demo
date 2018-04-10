package com.csx.pool;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/10 0010
 */
public enum PooledObjectState {
    /**
     * 在队列中，但是当前不在使用中
     */
    IDLE,
    /**
     * 在使用中
     */
    ALLOCATED,
    /**
     * 在队列中，目前正在测试是否可能被驱逐
     */
    EVICTION,
    /**
     * 不在队列中，目前正在测试，可能被驱逐。当尝试从池中借出对象时，需要从队列中移除并进行测试。
     * 一旦驱逐测试完成，需要把对象放回到队列的头部
     */
    EVICTION_RETURN_TO_HEAD,
    /**
     * 在队列中正在进行验证
     */
    VALIDATION,
    /**
     * 不在队列中，当前正在验证。当对象从池中被借出，在配置了testOnBorrow的情况下，对像从队列移除和进行预分配的时候会进行验证
     */
    VALIDATION_PREALLOCATED,
    /**
     * 不在队列中，正在进行验证。从池中借出对象时，从队列移除对象时会先进行测试。
     * 返回到队列头部的时候应该做一次完整的验证
     */
    VALIDATION_RETURN_TO_HEAD,
    /**
     * 回收或验证失败，将销毁
     */
    INVALID,
    /**
     * 即将无效
     */
    ABANDONED,
    /**
     * 返回池中
     */
    RETURNING
}
