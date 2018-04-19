package com.csx.demo;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.proxy.CglibProxySource;
import org.apache.commons.pool2.proxy.JdkProxySource;
import org.apache.commons.pool2.proxy.ProxiedObjectPool;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

/**
 * @author csx
 * @Package com.csx.demo
 * @Description: TODO
 * @date 2018/4/19 0019
 */
public class PoolTest {
    @Test
    public void testPool() {
        GenericObjectPool<StringBuffer> objectPool = new GenericObjectPool<StringBuffer>(new StringBufferFactory());
        ReaderUtil readerUtil = new ReaderUtil(objectPool);
        ExecutorService executor = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String test = readerUtil.readToString(new InputStreamReader(new FileInputStream("E:\\myproject\\pool_demo\\src\\main\\resources\\a.txt")));
                    System.out.println(test);
                } catch (Exception e) {
                }

            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    String test = readerUtil.readToString(new InputStreamReader(new FileInputStream("E:\\myproject\\pool_demo\\src\\main\\resources\\a.txt")));
                    System.out.println(test);
                } catch (Exception e) {
                }

            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String test = readerUtil.readToString(new InputStreamReader(new FileInputStream("E:\\myproject\\pool_demo\\src\\main\\resources\\a.txt")));
                    System.out.println(test);
                } catch (Exception e) {
                }

            }
        });

    }
}
