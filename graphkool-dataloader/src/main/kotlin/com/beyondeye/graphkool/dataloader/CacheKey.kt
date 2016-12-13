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

/**
 * Function that is invoked on input keys of type `K` to derive keys that are required by the [CacheMap]
 * implementation.
 * @param <K>   type parameter indicating the type of the input key
 * *
 * @author [Arnold Schrijver](https://github.com/aschrijver/)
</K> */
@FunctionalInterface
interface CacheKey<K: Any> {

    /**
     * Returns the cache key that is created from the provided input key.
     * @param input the input key
     * *
     * @return the cache key
     */
    fun getKey(input: K): Any
}
