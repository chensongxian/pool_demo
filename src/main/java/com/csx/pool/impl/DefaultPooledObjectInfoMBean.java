package com.csx.pool.impl;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public interface DefaultPooledObjectInfoMBean {
    long getCreateTime();
    String getCreateTimeFormatted();
    long getLastBorrowTime();
    String getLastBorrowTimeFormatted();
    String getLastBorrowTrace();
    long getLastReturnTime();
    String getLastReturnTimeFormatted();
    String getPooledObjectType();
    String getPooledObjectToString();
    long getBorrowedCount();
}
