package com.csx.pool.impl;

import com.csx.pool.PooledObject;
import com.csx.pool.PooledObjectState;
import com.csx.pool.TrackedUse;

import java.awt.*;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: TODO
 * @date 2018/4/11 0011
 */
public class DefaultPooledObject<T> implements PooledObject<T> {

    private final T object;
    private PooledObjectState state = PooledObjectState.IDLE;
    private final long createTime = System.currentTimeMillis();
    private volatile long lastBorrowTime = createTime;
    private volatile long lastUseTime = createTime;
    private volatile long lastReturnTime = createTime;
    private volatile boolean logAbandoned = false;
    private volatile Exception borrowedBy = null;
    private volatile Exception usedBy = null;
    private volatile long borrowedCount = 0;

    public DefaultPooledObject(T object) {
        this.object = object;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getActiveTimeMillis() {
        long rTime = lastReturnTime;
        long bTime = lastBorrowTime;

        if (rTime > bTime) {
            return rTime - bTime;
        } else {
            return System.currentTimeMillis() - bTime;
        }
    }

    @Override
    public long getIdleTimeMillis() {
        final long elapsed = System.currentTimeMillis() - lastReturnTime;
        return elapsed > 0 ? elapsed : 0;
    }

    @Override
    public long getLastBorrowTime() {
        return lastBorrowTime;
    }

    @Override
    public long getLastReturnTime() {
        return lastReturnTime;
    }

    @Override
    public long getLastUsedTime() {
        if (object instanceof TrackedUse) {
            return Math.max(((TrackedUse) object).getLastUsed(), lastUseTime);
        } else {
            return lastUseTime;
        }
    }

    @Override
    public long getBorrowedCount(){
        return borrowedCount;
    }

    @Override
    public int compareTo(PooledObject<T> other) {
        final long lastActiveDiff=this.lastReturnTime-other.getLastReturnTime();
        if(lastActiveDiff==0){
            return System.identityHashCode(this)-System.identityHashCode(other);
        }
        return (int)Math.min(Math.max(lastActiveDiff,Integer.MIN_VALUE),Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Object: ");
        result.append(object.toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(state.toString());
        }
        return result.toString();
    }

    @Override
    public synchronized boolean startEvictionTest() {
        if(state==PooledObjectState.IDLE){
            state=PooledObjectState.EVICTION;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean endEvictionTest(Deque<PooledObject<T>> idleQueue) {
        if(state==PooledObjectState.EVICTION){
            state=PooledObjectState.IDLE;
            return true;
        }else if(state==PooledObjectState.EVICTION_RETURN_TO_HEAD){
            state=PooledObjectState.IDLE;
            if(!idleQueue.offerFirst(this)){

            }
        }
        return false;
    }

    @Override
    public synchronized boolean allocate() {
        if(state==PooledObjectState.IDLE){
            state=PooledObjectState.ALLOCATED;
            lastBorrowTime=System.currentTimeMillis();
            lastReturnTime=lastBorrowTime;
            borrowedCount++;
            if(logAbandoned){
                borrowedBy=new AbandonedObjectCreatedException();
            }
            return true;
        }else if(state==PooledObjectState.EVICTION){
            state=PooledObjectState.EVICTION_RETURN_TO_HEAD;
            return false;
        }
        return false;
    }

    @Override
    public synchronized boolean deallocate() {
        if(state==PooledObjectState.ALLOCATED||state==PooledObjectState.RETURNING){
            state=PooledObjectState.IDLE;
            lastReturnTime=System.currentTimeMillis();
            borrowedBy=null;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void invalidate() {
        state=PooledObjectState.INVALID;
    }

    @Override
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned=logAbandoned;
    }

    @Override
    public void use() {
        lastUseTime=System.currentTimeMillis();
        borrowedBy=new Exception("The last code to use this object was:");
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        boolean written=false;
        Exception borrowedByCopy=this.borrowedBy;
        if(borrowedByCopy!=null){
            borrowedByCopy.printStackTrace(writer);
            written=true;
        }
        Exception usedByCopy=this.usedBy;
        if(usedByCopy!=null){
            usedByCopy.printStackTrace(writer);
            written=true;
        }
    }

    @Override
    public synchronized PooledObjectState getState() {
        return state;
    }

    @Override
    public void markAbandoned() {
        state=PooledObjectState.ABANDONED;
    }

    @Override
    public void markReturning() {
        state=PooledObjectState.RETURNING;
    }

    static class AbandonedObjectCreatedException extends Exception{

        private static final long serialVersionUID = -3626088038065399208L;

        private static final SimpleDateFormat format = new SimpleDateFormat
                ("'Pooled object created' yyyy-MM-dd HH:mm:ss Z " +
                        "'by the following code has not been returned to the pool:'");

        private final long _createdTime;

        public AbandonedObjectCreatedException() {
            super();
            this._createdTime = System.currentTimeMillis();
        }

        @Override
        public String getMessage() {
            String msg;
            synchronized (format){
                msg=format.format(new Date(_createdTime));
            }
            return msg;
        }
    }
}
