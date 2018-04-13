package com.csx.pool.impl;

import sun.reflect.generics.tree.VoidDescriptor;

import java.awt.*;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;

/**
 * @author csx
 * @Package com.csx.pool.impl
 * @Description: 阻塞的双端队列
 * @date 2018/4/13 0013
 */
public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements Deque<E>, Serializable {

    private static final long serialVersionUID = 6668491495081873355L;

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    /**
     * 双端列表节点
     *
     * @param <E>
     */
    public static final class Node<E> {
        /**
         * 列表元素
         */
        E item;

        /**
         * 前置节点
         */
        Node<E> prev;

        /**
         * 后置节点
         */
        Node<E> next;

        /**
         * 创建一个新的节点
         *
         * @param x
         * @param p
         * @param n
         */
        public Node(E x, Node<E> p, Node<E> n) {
            this.item = x;
            this.prev = p;
            this.next = n;
        }
    }

    /**
     * 列表头节点
     */
    private transient Node<E> first;

    /**
     * 列表尾节点
     */
    private transient Node<E> last;

    /**
     * 列表元素数量
     */
    private transient int count;

    /**
     * 列表容量
     */
    private final int capacity;

    /**
     * 锁
     */
    private final InterruptibleReentrantLock lock;

    /**
     * 为了等待获取的condition
     */
    private Condition notEmpty;

    /**
     * 为了等待放的condition
     */
    private Condition notFull;


    public LinkedBlockingDeque() {
        this(Integer.MAX_VALUE);
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    public LinkedBlockingDeque(boolean fairness) {
        this(Integer.MAX_VALUE, fairness);
    }

    public LinkedBlockingDeque(int capacity) {
        this(capacity, false);
    }

    public LinkedBlockingDeque(int capacity, boolean fairness) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        lock = new InterruptibleReentrantLock(fairness);
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    /**
     * 传入一个集合构建一个阻塞的双端队列
     *
     * @param c
     */
    public LinkedBlockingDeque(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        lock.lock();
        try {
            for (E e : c) {
                if (e == null) {
                    throw new NullPointerException();
                }

                if (!linkLast(e)) {
                    throw new IllegalStateException("Deque full");
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 把元素作为头节点插入
     *
     * @param e
     * @return
     */
    private boolean linkFirst(E e) {
        if (count >= capacity) {
            return false;
        }
        Node<E> f = first;
        Node<E> x = new Node<E>(e, null, f);
        first = x;
        if (last == null) {
            last = x;
        } else {
            f.prev = x;
        }
        ++count;
        notEmpty.signal();
        return true;
    }

    /**
     * 把元素作为尾节点插入
     *
     * @param e
     * @return
     */
    private boolean linkLast(E e) {
        if (count >= capacity) {
            return false;
        }
        Node<E> l = last;
        Node<E> x = new Node<E>(e, last, null);
        last = x;
        if (first == null) {
            first = x;
        } else {
            l.next = x;
        }
        ++count;
        notEmpty.signal();
        return true;
    }

    /**
     * 移除并获取头节点
     *
     * @return
     */
    private E unlinkFirst() {
        Node<E> f = first;
        if (f == null) {
            return null;
        }
        Node<E> n = f.next;
        E item = f.item;
        f.item = null;
        f.next = f;
        first = n;
        if (n == null) {
            last = null;
        } else {
            n.prev = null;
        }
        --count;
        notFull.signal();
        return item;
    }

    /**
     * 移除并获取尾节点
     *
     * @return
     */
    private E unlinkLast() {
        Node<E> l = last;
        if (l == null) {
            return null;
        }
        Node<E> p = l.prev;
        E item = l.item;
        l.item = null;
        l.prev = l;
        last = p;
        if (p == null) {
            first = null;
        } else {
            p.next = null;
        }
        --count;
        notFull.signal();
        return item;
    }

    /**
     * 移除提供的节点
     *
     * @param x
     */
    private void unlink(Node<E> x) {
        Node<E> p = x.prev;
        Node<E> n = x.next;
        if (p == null) {
            unlinkFirst();
        } else if (n == null) {
            unlinkLast();
        } else {
            p.next = n;
            n.prev = p;
            x.item = null;
            --count;
            notFull.signal();
        }
    }

    @Override
    public void addFirst(E e) {
        if (!offerFirst(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    @Override
    public void addLast(E e) {
        if (!offerLast(e)) {
            throw new IllegalStateException("Deque full");
        }
    }

    @Override
    public boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        lock.lock();
        try {
            return linkFirst(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        lock.lock();
        try {
            return linkLast(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E getLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    public void putFirst(E e) throws InterruptedException {
        if(e==null){
            throw new NullPointerException();
        }
        lock.lock();
        try {
            while (!linkFirst(e)){
                notFull.await();
            }
        }finally {
            lock.unlock();
        }
    }
}
