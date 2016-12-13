/*
 * Copyright (c) 2016 The original author or authors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *      The Eclipse Public License is available at
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *      The Apache License v2.0 is available at
 *      http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */

package com.beyondeye.graphkool.dataloader

import java.util.HashMap

/**
 * Default implementation of [CacheMap] that is based on a regular [java.util.LinkedHashMap].

 * @param <U> type parameter indicating the type of the cache keys
 * *
 * @param <V> type parameter indicating the type of the data that is cached
 * *
 * *
 * @author [Arnold Schrijver](https://github.com/aschrijver/)
</V></U> */
class DefaultCacheMap<U, V> : CacheMap<U, V> {

    private val cache: MutableMap<U, V>

    /**
     * Default constructor
     */
    init {
        cache = HashMap<U, V>()
    }

    /**
     * {@inheritDoc}
     */
    override fun containsKey(key: U): Boolean {
        return cache.containsKey(key)
    }

    /**
     * {@inheritDoc}
     */
    override fun get(key: U): V? {
        return cache[key]
    }

    /**
     * {@inheritDoc}
     */
    override fun set(key: U, value: V): CacheMap<U, V> {
        cache.put(key, value)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun delete(key: U): CacheMap<U, V> {
        cache.remove(key)
        return this
    }

    /**
     * {@inheritDoc}
     */
    override fun clear(): CacheMap<U, V> {
        cache.clear()
        return this
    }
}
