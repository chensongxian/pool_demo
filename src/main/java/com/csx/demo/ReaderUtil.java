package com.csx.demo;

import org.apache.commons.pool2.ObjectPool;

import java.io.IOException;
import java.io.Reader;

/**
 * @author csx
 * @Package com.csx.demo
 * @Description: TODO
 * @date 2018/4/19 0019
 */
public class ReaderUtil {
    private ObjectPool<StringBuffer> pool;

    public ReaderUtil(ObjectPool<StringBuffer> pool) {
        this.pool = pool;
    }

    public String readToString(Reader in) throws IOException {
        StringBuffer buf=null;
        try {
            buf=pool.borrowObject();
            for(int c = in.read(); c != -1; c = in.read()) {
                buf.append((char)c);
            }
            System.out.println(pool.getNumIdle()+"---"+pool.getNumActive());
            return buf.toString();
        } catch(IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
        } finally {
            try {
                in.close();
            } catch(Exception e) {
                // ignored
            }
            try {
                pool.returnObject(buf);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
