package com.blocks.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by lotus on 2016/12/26.
 */
public interface JedisAction<T> {

    public T doAction(Jedis jedis);
}
