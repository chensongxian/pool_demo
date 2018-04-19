package com.csx.demo;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author csx
 * @Package com.csx.demo
 * @Description: TODO
 * @date 2018/4/19 0019
 */
public class StringBufferFactory extends BasePooledObjectFactory<StringBuffer> {
    @Override
    public StringBuffer create() throws Exception {
        return new StringBuffer();
    }

    @Override
    public PooledObject<StringBuffer> wrap(StringBuffer obj) {
        return new DefaultPooledObject<StringBuffer>(obj);
    }


    @Override
    public void passivateObject(PooledObject<StringBuffer> p) throws Exception {
        p.getObject().setLength(0);
    }
}
