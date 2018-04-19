/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a.txt copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.pool2;

/**
 * Provides the possible states that a.txt {@link PooledObject} may be in.
 *
 * @version $Revision: $
 *
 * @since 2.0
 */
public enum PooledObjectState {
    /**
     * 空闲中，在队列中，但是没有在使用.
     */
    IDLE,

    /**
     * 使用中.
     */
    ALLOCATED,

    /**
     * 在队列中，正在被检查.
     */
    EVICTION,

    /**
     * 不在队列 中，检查结束后放回头部
     */
    EVICTION_RETURN_TO_HEAD,

    /**
     * 在队列中，正在被检查.
     */
    VALIDATION,

    /**
     * 验证结束要被分配
     */
    VALIDATION_PREALLOCATED,

    /**
     * 检查结束要放回队列头部
     */
    VALIDATION_RETURN_TO_HEAD,

    /**
     * 不可用，即将或已经被销毁
     */
    INVALID,

    /**
     * 被遗弃，即将不可用.
     */
    ABANDONED,

    /**
     * 返回对象池.
     */
    RETURNING
}
