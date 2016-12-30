package com.blocks.cache.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * Created by lotus on 2016/12/30.
 */
public interface ShardedJedisAction<T> {
    public T doAction(ShardedJedis shardedJedis);
}
