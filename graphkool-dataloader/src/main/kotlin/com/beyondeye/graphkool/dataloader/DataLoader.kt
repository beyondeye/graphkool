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

import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.json.JsonObject

import java.util.Collections
import java.util.LinkedHashMap
import java.util.Objects
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

/**
 * Data loader is a utility class that allows batch loading of data that is identified by a set of unique keys. For
 * each key that is loaded a separate [Future] is returned, that completes as the batch function completes.
 * Besides individual futures a [CompositeFuture] of the batch is available as well.
 *
 *
 * With batching enabled the execution will start after calling [DataLoader.dispatch], causing the queue of
 * loaded keys to be sent to the batch function, clears the queue, and returns the [CompositeFuture].
 *
 *
 * As batch functions are executed the resulting futures are cached using a cache implementation of choice, so they
 * will only execute once. Individual cache keys can be cleared, so they will be re-fetched when referred to again.
 * It is also possible to clear the cache entirely, and prime it with values before they are used.
 *
 *
 * Both caching and batching can be disabled. Configuration of the data loader is done by providing a
 * [DataLoaderOptions] instance on creation.

 * @param <K> type parameter indicating the type of the data load keys
 * *
 * @param <V> type parameter indicating the type of the data that is returned
 * *
 * *
 * @author [Arnold Schrijver](https://github.com/aschrijver/)
</V></K> */
class DataLoader<K:Any, V>
/**
 * Creates a new data loader with the provided batch load function and options.

 * @param batchLoadFunction the batch load function to use
 * *
 * @param options           the batch load options
 */
(private val batchLoadFunction: BatchLoader<K>, options: DataLoaderOptions?) {
    private val loaderOptions: DataLoaderOptions
    private val futureCache: CacheMap<Any, Future<V>>
    private val loaderQueue: LinkedHashMap<K, Future<V>>

    /**
     * Creates a new data loader with the provided batch load function, and default options.

     * @param batchLoadFunction the batch load function to use
     */
    constructor(batchLoadFunction: BatchLoader<K>) : this(batchLoadFunction, null) {
    }

    init {
        Objects.requireNonNull(batchLoadFunction, "Batch load function cannot be null")
        this.loaderOptions = options ?: DataLoaderOptions()
        this.futureCache = if (loaderOptions.cacheMap().isPresent) loaderOptions.cacheMap().get() as CacheMap<Any, Future<V>> else CacheMap.simpleMap<Any, V>()
        this.loaderQueue = LinkedHashMap<K, Future<V>>()
    }

    /**
     * Requests to load the data with the specified key asynchronously, and returns a future of the resulting value.
     *
     *
     * If batching is enabled (the default), you'll have to call [DataLoader.dispatch] at a later stage to
     * start batch execution. If you forget this call the future will never be completed (unless already completed,
     * and returned from cache).

     * @param key the key to load
     * *
     * @return the future of the value
     */
    fun load(key: K): Future<V> {
        Objects.requireNonNull(key, "Key cannot be null")
        val cacheKey = getCacheKey(key)
        if (loaderOptions.cachingEnabled() && futureCache.containsKey(cacheKey)) {
            return futureCache[cacheKey] ?: Future.failedFuture("key not found")
        }

        val future = Future.future<V>()
        if (loaderOptions.batchingEnabled()) {
            loaderQueue.put(key, future)
        } else {
            val compositeFuture = batchLoadFunction.load(setOf(key))
            if (compositeFuture.succeeded()) {
                future.complete(compositeFuture.result().resultAt<V>(0))
            } else {
                future.fail(compositeFuture.cause())
            }
        }
        if (loaderOptions.cachingEnabled()) {
            futureCache[cacheKey] = future
        }
        return future
    }

    /**
     * Requests to load the list of data provided by the specified keys asynchronously, and returns a composite future
     * of the resulting values.
     *
     *
     * If batching is enabled (the default), you'll have to call [DataLoader.dispatch] at a later stage to
     * start batch execution. If you forget this call the future will never be completed (unless already completed,
     * and returned from cache).

     * @param keys the list of keys to load
     * *
     * @return the composite future of the list of values
     */
    fun loadMany(keys: List<K>): CompositeFuture {
        return CompositeFuture.all(keys.map { this.load(it) })
    }

    /**
     * Dispatches the queued load requests to the batch execution function and returns a composite future of the result.
     *
     *
     * If batching is disabled, or there are no queued requests, then a succeeded composite future is returned.

     * @return the composite future of the queued load requests
     */
    fun dispatch(): CompositeFuture {
        if (!loaderOptions.batchingEnabled() || loaderQueue.size == 0) {
            return CompositeFuture.all(emptyList<Future<*>>())
        }
        val batch = batchLoadFunction.load(loaderQueue.keys)
        val index = AtomicInteger(0)
        loaderQueue.forEach { key, future ->
            if (batch.succeeded(index.get())) {
                future.complete(batch.resultAt<V>(index.get()))
            } else {
                future.fail(batch.cause(index.get()))
            }
            index.incrementAndGet()
        }
        loaderQueue.clear()
        return batch
    }

    /**
     * Clears the future with the specified key from the cache, if caching is enabled, so it will be re-fetched
     * on the next load request.

     * @param key the key to remove
     * *
     * @return the data loader for fluent coding
     */
    fun clear(key: K): DataLoader<K, V> {
        val cacheKey = getCacheKey(key)
        futureCache.delete(cacheKey)
        return this
    }

    /**
     * Clears the entire cache map of the loader.

     * @return the data loader for fluent coding
     */
    fun clearAll(): DataLoader<K, V> {
        futureCache.clear()
        return this
    }

    /**
     * Primes the cache with the given key and value.

     * @param key   the key
     * *
     * @param value the value
     * *
     * @return the data loader for fluent coding
     */
    fun prime(key: K, value: V): DataLoader<K, V> {
        val cacheKey = getCacheKey(key)
        if (!futureCache.containsKey(cacheKey)) {
            futureCache[cacheKey] = Future.succeededFuture(value)
        }
        return this
    }

    /**
     * Primes the cache with the given key and error.

     * @param key   the key
     * *
     * @param error the exception to prime instead of a value
     * *
     * @return the data loader for fluent coding
     */
    fun prime(key: K, error: Exception): DataLoader<K, V> {
        val cacheKey = getCacheKey(key)
        if (!futureCache.containsKey(cacheKey)) {
            futureCache[cacheKey] = Future.failedFuture<V>(error)
        }
        return this
    }

    /**
     * Gets the object that is used in the internal cache map as key, by applying the cache key function to
     * the provided key.
     *
     *
     * If no cache key function is present in [DataLoaderOptions], then the returned value equals the input key.

     * @param key the input key
     * *
     * @return the cache key after the input is transformed with the cache key function
     */
    fun getCacheKey(key: K): Any {
        return loaderOptions.cacheKeyFunction()?.let { cacheKeyFn->
            (key as? JsonObject)?.let {
               cacheKeyFn.getKey(it)
            }
        }?:key
    }
}
