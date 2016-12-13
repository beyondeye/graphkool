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

import io.vertx.core.json.JsonObject

import java.util.Objects
import java.util.Optional

/**
 * Configuration options for [DataLoader] instances.

 * @author [Arnold Schrijver](https://github.com/aschrijver/)
 */
class DataLoaderOptions {

    private var batchingEnabled: Boolean = false
    private var cachingEnabled: Boolean = false
    private var cacheKeyFunction: CacheKey<JsonObject>? = null
    private var cacheMap: CacheMap<*, *>? = null

    /**
     * Creates a new data loader options with default settings.
     */
    constructor() {
        batchingEnabled = true
        cachingEnabled = true
    }

    /**
     * Clones the provided data loader options.
     * @param other the other options instance
     */
    constructor(other: DataLoaderOptions) {
        Objects.requireNonNull(other, "Other data loader options cannot be null")
        this.batchingEnabled = other.batchingEnabled
        this.cachingEnabled = other.cachingEnabled
        this.cacheKeyFunction = other.cacheKeyFunction
        this.cacheMap = other.cacheMap
    }

    /**
     * Creates a new data loader options with values provided as JSON.
     *
     *
     * Note that only json-serializable options can be set with this constructor. Others,
     * like [DataLoaderOptions.cacheKeyFunction] must be set manually after creation.
     *
     *
     * Note also that this makes it incompatible with true Vert.x data objects, so beware if you use it that way.
     * @param json the serialized data loader options to set
     */
    constructor(json: JsonObject) {
        Objects.requireNonNull(json, "Json cannot be null")
        this.batchingEnabled = json.getBoolean("batchingEnabled")!!
        this.batchingEnabled = json.getBoolean("cachingEnabled")!!
    }

    /**
     * Option that determines whether to use batching (the default), or not.
     * @return `true` when batching is enabled, `false` otherwise
     */
    fun batchingEnabled(): Boolean {
        return batchingEnabled
    }

    /**
     * Sets the option that determines whether batch loading is enabled.
     * @param batchingEnabled `true` to enable batch loading, `false` otherwise
     * *
     * @return the data loader options for fluent coding
     */
    fun setBatchingEnabled(batchingEnabled: Boolean): DataLoaderOptions {
        this.batchingEnabled = batchingEnabled
        return this
    }

    /**
     * Option that determines whether to use caching of futures (the default), or not.
     * @return `true` when caching is enabled, `false` otherwise
     */
    fun cachingEnabled(): Boolean {
        return cachingEnabled
    }

    /**
     * Sets the option that determines whether caching is enabled.
     * @param cachingEnabled `true` to enable caching, `false` otherwise
     * *
     * @return the data loader options for fluent coding
     */
    fun setCachingEnabled(cachingEnabled: Boolean): DataLoaderOptions {
        this.cachingEnabled = cachingEnabled
        return this
    }

    /**
     * Gets an (optional) function to invoke for creation of the cache key, if caching is enabled.
     *
     *
     * If missing the cache key defaults to the `key` type parameter of the data loader of type `K`.
     * @return an optional with the function, or empty optional
     */
    fun cacheKeyFunction(): CacheKey<JsonObject>? {
        return cacheKeyFunction
    }

    /**
     * Sets the function to use for creating the cache key, if caching is enabled.
     * @param cacheKeyFunction the cache key function to use
     * *
     * @return the data loader options for fluent coding
     */
    fun setCacheKeyFunction(cacheKeyFunction: CacheKey<JsonObject>): DataLoaderOptions {
        this.cacheKeyFunction = cacheKeyFunction
        return this
    }

    /**
     * Gets the (optional) cache map implementation that is used for caching, if caching is enabled.
     *
     *
     * If missing a standard [java.util.LinkedHashMap] will be used as the cache implementation.
     * @return an optional with the cache map instance, or empty
     */
    fun cacheMap(): Optional<CacheMap<*, *>> {
        return Optional.ofNullable<CacheMap<*, *>>(cacheMap)
    }

    /**
     * Sets the cache map implementation to use for caching, if caching is enabled.
     * @param cacheMap the cache map instance
     * *
     * @return the data loader options for fluent coding
     */
    fun setCacheMap(cacheMap: CacheMap<*, *>): DataLoaderOptions {
        this.cacheMap = cacheMap
        return this
    }

    companion object {

        fun create(): DataLoaderOptions {
            return DataLoaderOptions()
        }
    }
}
