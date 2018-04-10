package com.csx.pool;

import sun.reflect.generics.tree.VoidDescriptor;

import java.io.PrintWriter;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description: TODO
 * @date 2018/4/10 0010
 */
public interface PooledObject<T> extends Comparable<PooledObject<T>>{
    T getObject();

    long getCreateTime();

    long getActiveTimeMillis();

    long getIdleTimeMillis();

    long getLastBorrowTime();

    long getLastReturnTime();

    long getLastUsedTime();

    @Override
    int compareTo(PooledObject<T> o);

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

    boolean startEvictionTest();

    boolean endEvictionTest();

    boolean allocate();

    boolean deallocate();

    void invalidate();

    void setLogAbandoned(boolean logAbandoned);

    void use();

    void printStackTrace(PrintWriter writer);

    PooledObjectState getState();

    void markAbandoned();

    void markReturning();
}
