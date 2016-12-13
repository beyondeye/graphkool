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

import io.vertx.core.Future

/**
 * Cache map interface for data loaders that use caching.
 *
 *
 * The default implementation used by the data loader is based on a [java.util.LinkedHashMap]. Note that the
 * implementation could also have used a regular [java.util.Map] instead of this [CacheMap], but
 * this aligns better to the reference data loader implementation provided by Facebook
 *
 *
 * Also it doesn't require you to implement the full set of map overloads, just the required methods.

 * @param <U> type parameter indicating the type of the cache keys
 * *
 * @param <V> type parameter indicating the type of the data that is cached
 * *
 * *
 * @author [Arnold Schrijver](https://github.com/aschrijver/)
</V></U> */
interface CacheMap<U, V> {

    /**
     * Checks whether the specified key is contained in the cach map.
     * @param key the key to check
     * *
     * @return `true` if the cache contains the key, `false` otherwise
     */
    fun containsKey(key: U): Boolean

    /**
     * Gets the specified key from the cache map.
     *
     *
     * May throw an exception if the key does not exists, depending on the cache map implementation that is used,
     * so be sure to check [CacheMap.containsKey] first.
     * @param key the key to retrieve
     * *
     * @return the cached value, or `null` if not found (depends on cache implementation)
     */
    operator fun get(key: U): V?

    /**
     * Creates a new cache map entry with the specified key and value, or updates the value if the key already exists.
     * @param key   the key to cache
     * *
     * @param value the value to cache
     * *
     * @return the cache map for fluent coding
     */
    operator fun set(key: U, value: V): CacheMap<U, V>

    /**
     * Deletes the entry with the specified key from the cache map, if it exists.

     * @param key the key to delete
     * *
     * @return the cache map for fluent coding
     */
    fun delete(key: U): CacheMap<U, V>

    /**
     * Clears all entries of the cache map
     * @return the cache map for fluent coding
     */
    fun clear(): CacheMap<U, V>

    companion object {

        /**
         * Creates a new cache map, using the default implementation that is based on a [java.util.LinkedHashMap].
         * @param <U> type parameter indicating the type of the cache keys
         * *
         * @param <V> type parameter indicating the type of the data that is cached
         * *
         * @return the cache map
        </V></U> */
        fun <U, V> simpleMap(): CacheMap<U, Future<V>> {
            return DefaultCacheMap()
        }
    }
}
