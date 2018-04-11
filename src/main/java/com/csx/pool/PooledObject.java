package com.csx.pool;

import sun.reflect.generics.tree.VoidDescriptor;

import java.io.PrintWriter;
import java.util.Deque;

/**
 * @author csx
 * @Package com.csx.pool
 * @Description:
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

    long getBorrowedCount();

    @Override
    int compareTo(PooledObject<T> o);

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();

    @Override
    String toString();

    boolean startEvictionTest();

    boolean endEvictionTest(Deque<PooledObject<T>> idleQueue);

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
