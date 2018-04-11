package com.csx.pool.impl;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public class GenericKeyedObjectPoolConfig implements Cloneable {
    public final static boolean DEFAULT_LIFO = true;
    public final static boolean DEFAULT_FAIRNESS=true;
    public final static long DEFAULT_MAX_WAIT_MILLIS=-1L;
}
