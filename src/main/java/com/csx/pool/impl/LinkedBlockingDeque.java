package com.csx.pool.impl;

import sun.reflect.generics.tree.VoidDescriptor;

import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.tools.JavaCompiler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.file.FileStore;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;

/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @author: csx
 * @Date: 2018-04-12
 */
public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements Deque<E>,Serializable{
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
     * @param <E>
     */
    private static final class Node<E>{
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
         * 创建一个新的列表节点
         * @param x 列表元素
         * @param p 前置节点
         * @param n 后置节点
         */
        Node(E x,Node<E> p,Node<E> n){
            item=x;
            prev=p;
            next=n;
        }
    }

    /**
     * 列表的第一个节点
     */
    private transient Node<E> first;

    /**
     * 列表最后一个节点
     */
    private transient Node<E> last;

    /**
     * 双端列表节点数量
     */
    private transient int count;

    /**
     * 双端列表最大容量
     */
    private final int capacity;

    /**
     * 锁
     */
    private final InterruptibleReetrantLock lock;
    /**
     * 为了等待获取的condition
     */
    private final Condition notEmpty;

    /**
     * 为了等待放入的condition
     */
    private final Condition notFull;


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

    private abstract class AbstractItr implements Iterator<E>{
        /**
         * 下一个节点
         */
        Node<E> next;
        /**
         * 下一个节点详情
         */
        E nextItem;

        private Node<E> lastRet;

        abstract Node<E> firstNode();

        abstract Node<E> nextNode(Node<E> N);

        AbstractItr() {
            lock.lock();
            try {
                next=firstNode();
                nextItem=next==null?null:next.item;
            } finally {
                lock.unlock();
            }
        }

        private Node<E> succ(Node<E> n){
            for(;;){
                Node<E> s=nextNode(n);
                if(s==null){
                    return null;
                }else if(s.item!=null){
                    return s;
                }else if(s==n){
                    return firstNode();
                }else {
                    n=s;
                }
            }
        }

        void advance(){
            lock.lock();
            try {
                next=succ(next);
                nextItem=next==null?null:next.item;
            }finally {
                lock.unlock();
            }
        }

        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public E next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            lastRet=next;
            E x=nextItem;
            advance();
            return x;
        }

        @Override
        public void remove() {
            Node<E> n=lastRet;
            if(n==null){
                throw new IllegalStateException();
            }
            lastRet=null;
            lock.lock();
            try {
                if(n.item!=null){
                    unlink(n);
                }
            }finally {
                lock.unlock();
            }
        }
    }

    private class Itr extends AbstractItr{

        @Override
        Node<E> firstNode() {
            return first;
        }

        @Override
        Node<E> nextNode(Node<E> n) {
            return n.next;
        }
    }


    @Override
    public void addFirst(E e) {

    }

    @Override
    public void addLast(E e) {

    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
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

    @Override
    public int size() {
        lock.lock();
        try {
            return count;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        if(o==null){
            return false;
        }
        lock.lock();
        try {
            for(Node<E> p=first;p!=null;p=p.next){
                if(o.equals(p.item)){
                    return true;
                }
            }
            return false;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.lock();
        try {
            Object[] a=new Object[count];
            int k=0;
            for(Node<E> p = first;p!=null;p=p.next){
                a[k++] = p.item;
            }
            return a;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        lock.lock();
        try {
            if(a.length<count){
                a=(T[]) Array.newInstance(a.getClass().getComponentType(),count);
            }
            int k=0;
            for(Node<E> p=first;p!=null;p=p.next){
                a[k++]= (T) p.item;
            }
            if(a.length>k){
                a[k]=null;
            }
            return a;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return super.toString();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            for(Node<E> f=first;f!=null;){
                f.item=null;
                Node<E> n=f.next;
                f.prev=null;
                f.next=null;
                f=n;
            }
            first=last=null;
            count=0;
            notFull.signalAll();
        }finally {
            lock.unlock();
        }
    }



    public LinkedBlockingDeque(boolean fairness) {
        this(Integer.MAX_VALUE,fairness);
    }

    public LinkedBlockingDeque(int capacity) {
        this(capacity,false);
    }

    public LinkedBlockingDeque(int capacity, boolean fairness) {
        if(capacity<=0){
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        lock=new InterruptibleReetrantLock(fairness);
        notEmpty=lock.newCondition();
        notFull=lock.newCondition();
    }

    public LinkedBlockingDeque(Collection<? extends E> c){
        this(Integer.MAX_VALUE);
        lock.lock();
        try {
            for(E e:c){
                if(e==null){
                    throw new NullPointerException();
                }
                if(!linkLast(e)){
                    throw new IllegalStateException("Deque full");
                }
            }
        }finally {
            lock.unlock();
        }
    }

    private boolean linkFirst(E e){
        if(count>=capacity){
            return false;
        }
        Node<E> f=first;
        Node<E> x=new Node<E>(e,null,f);
        first=x;
        if(last==null){
            last=x;
        }else{
            f.prev=x;
        }
        ++count;
        notEmpty.signal();
        return true;
    }

    private boolean linkLast(E e){
        if(count>=capacity){
            return false;
        }

        Node<E> l=last;
        Node<E> x=new Node<E>(e,l,null);
        last=x;
        if(first==null){
            first=x;
        }else{
            l.next=x;
        }
        ++count;
        notEmpty.signal();
        return true;
    }


    private E unlinkFirst(){
        Node<E> f=first;
        if(f==null){
            return null;
        }
        Node<E> n=f.next;
        E item=f.item;
        f.item=null;
        f.next=f;
        first=n;
        if(n==null){
            last=null;
        }else{
            n.prev=null;
        }
        --count;
        notFull.signal();
        return item;
    }

    private E unlinkLast(){
        Node<E> l=last;
        if(l==null){
            return null;
        }
        Node<E> p=l.prev;
        E item=l.item;
        l.item=null;
        l.prev=l;
        last=p;
        if(p==null){
            first=null;
        }else{
            p.next=null;
        }
        --count;
        notFull.signal();
        return item;
    }

    private void unlink(Node<E> x){
        Node<E> p=x.prev;
        Node<E> n=x.next;
        if(p==null){
            unlinkFirst();
        }else if(n==null){
            unlinkLast();
        }else{
            p.next=n;
            n.prev=p;
            x.item=null;
            --count;
            notFull.signal();
        }
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        lock.lock();
        try {
            s.defaultWriteObject();;
            for(Node<E> p = first;p!=null;p=p.next){
                s.writeObject(p.item);
            }
            s.writeObject(null);
        }finally {
            lock.unlock();
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        count=0;
        first=null;
        last=null;
        for (;;){
            E item= (E) s.readObject();
            if(item==null){
                break;
            }
            add(item);
        }
    }

    public int getTakeQueueLength(){
        lock.lock();
        try {
            return lock.getWaitQueueLength(notEmpty);
        }finally {
            lock.unlock();
        }
    }

    public void interuptTakeWaiters(){
        lock.lock();
        try {
            lock.interruptWaiters(notEmpty);
        }finally {
            lock.unlock();
        }
    }
}