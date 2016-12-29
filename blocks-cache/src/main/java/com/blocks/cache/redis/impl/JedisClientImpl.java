package com.blocks.cache.redis.impl;

import com.blocks.cache.redis.AbstractJedisPool;
import com.blocks.cache.redis.JedisAction;
import com.blocks.cache.redis.JedisClient;
import com.blocks.cache.utils.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by lotus on 2016/12/26.
 */
//@Component("JedisClientImpl")
public class JedisClientImpl implements JedisClient{
    private final Logger logger = Logger.getLogger(getClass().getName());

    //@Autowired
    private JedisPool jedisPool;

    public JedisClientImpl(){}

    public JedisClientImpl(JedisPool jedisPool) {this.jedisPool = jedisPool;}

    public void destory() {
        if (null != jedisPool){
            jedisPool.destroy();
        }
    }

    public <T> T execute(JedisAction<T> action) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return action.doAction(jedis);
        } catch (JedisConnectionException e) {
            if (null != e){
                throw e;
            }
        } finally {
            if (null != jedis){
                try {
                    jedisPool.returnResource(jedis);
                } catch(Exception ex) {
                    logger.warning("Can not return resource." + ex.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * 将序列化字符串对象值value关联到key，如果key已经持有其他值，SET就覆旧值
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    /**
     * 将序列化字符串对象值value关联到key，如果key已经持有其他值，SET就覆旧值，有效时间为expire秒
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String set(final String key, final String value, final int expire) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                /*String result = jedis.set(key, value);
                jedis.expire(key, expire);
                return result;*/
                return jedis.setex(key, expire, value);
            }
        });
    }

    public String get(final String key) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }



    /**
     * 在key存在时删除key，如果键被删除成功，命令执行后输出 1，否则将输出 0
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    public long del(final byte[] key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    /**
     * 删除缓存的对象
     *
     * @param key
     * @return
     */
    public long delObject(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.del(key.getBytes());
            }
        });
    }

    /**
     * 保存序列化的对象
     *
     * @param key
     * @param value
     * @return
     */
    public String setObject(final String key, final Object value) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.set(key.getBytes(), SimpleUtils.serialize(value));
            }
        });
    }

    /**
     * 保存序列化的对象（带有效时间）,有效时间为expire秒
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public String setObject(final String key, final Object value, final int expire) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                /*String result = jedis.set(key.getBytes(), SimpleUtils.serialize(value));
                jedis.expire(key, expire);
                return result;*/
                return jedis.setex(key.getBytes(), expire, SimpleUtils.serialize(value));
            }
        });
    }

    public String set(final byte[] key, final Object value) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.set(key, SimpleUtils.serialize(value));
            }
        });
    }

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long hset(final String key, final String field, final String value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.hset(key, field, value);
            }
        });
    }

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 如果域field已经存在于哈希表中，旧值将被覆盖，expire秒后失效
     *
     * @param key
     * @param field
     * @param value
     * @param expire
     * @return
     */
    public long hset(final String key, final String field, final String value, final int expire) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                Long result = jedis.hset(key, field, value);
                jedis.expire(key, expire);
                return result;
            }
        });
    }

    public long hsetObject(final String key, final String field, final Object value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.hset(key.getBytes(), field.getBytes(), SimpleUtils.serialize(value));
            }
        });
    }

    public long hsetObject(final String key, final String field, final Object value, final int expire) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                Long result = jedis.hset(key.getBytes(), field.getBytes(), SimpleUtils.serialize(value));
                jedis.expire(key.getBytes(), expire);
                return result;
            }
        });
    }

    /**
     * 返回哈希表key中给定域field的值。
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(final String key, final String field) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    public Object hgetObject(final String key, final String field) {
        return this.execute(new JedisAction<Object>() {
            public Object doAction(Jedis jedis) {
                return SimpleUtils.unserialize(jedis.hget(key.getBytes(), field.getBytes()));
            }
        });
    }

    /**
     * 返回哈希表 key 中，所有的域和值。
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(final String key) {
        return this.execute(new JedisAction<Map<String, String>>() {
            public Map<String, String> doAction(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public Map<String, Object> hgetAllObject(final String key) {
        return this.execute(new JedisAction<Map<String, Object>>() {
            public Map<String, Object> doAction(Jedis jedis) {
                Map<byte[], byte[]> byteMap = jedis.hgetAll(key.getBytes());
                Map<String, Object> objMap = new HashMap<String, Object>();
                for (Map.Entry<byte[], byte[]> entry : byteMap.entrySet()){
                    objMap.put(SimpleUtils.unserialize(entry.getKey()).toString(), SimpleUtils.unserialize(entry.getValue()));
                }
                return objMap;
            }
        });
    }

    /**
     * 同时将多个域-值对设置到哈希表key中。如果key不存在，一个空哈希表被创建并执行HMSET操作。
     *
     * @param key
     * @param hash
     * @return
     */
    public String hmset(final String key, final Map<String, String> hash) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.hmset(key, hash);
            }
        });
    }

    /**
     * 同时将多个域-值对设置到哈希表key中。如果key不存在，一个空哈希表被创建并执行HMSET操作。expire秒后失效
     *
     * @param key
     * @param hash
     * @param expire
     * @return
     */
    public String hmset(final String key, final Map<String, String> hash, final int expire) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                String result = jedis.hmset(key, hash);
                jedis.expire(key, expire);
                return result;
            }
        });
    }

    /**
     * 删除哈希表key中的指定域，不存在的域将被忽略。
     *
     * @param key
     * @param field
     * @return
     */
    public long hdel(final String key, final String... field) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.hdel(key, field);
            }
        });
    }

    /**
     * 在指定Key所关联的List
     * Value的头部插入参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的头部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。
     *
     * @param key
     * @param value
     * @return
     */
    public Long lpush(final String key, final Object value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.lpush(key.getBytes(), SimpleUtils.serialize(value));
            }
        });
    }

    /**
     * 在指定Key所关联的List
     * Value的尾部插入参数中给出的所有Values。如果该Key不存在，该命令将在插入之前创建一个与该Key关联的空链表，
     * 之后再将数据从链表的尾部插入。如果该键的Value不是链表类型，该命令将返回相关的错误信息。
     *
     * @param key
     * @param value
     * @return
     */
    public Long rpush(final String key, final Object value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.rpush(key.getBytes(), SimpleUtils.serialize(value));
            }
        });
    }

    /**
     * 返回指定Key关联的链表中元素的数量，如果该Key不存在，则返回0。如果与该Key关联的Value的类型不是链表，则返回相关的错误信息。
     *
     * @param key
     * @return
     */
    public Long llen(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.llen(key.getBytes());
            }
        });
    }

    /**
     * 返回并弹出指定Key关联的链表中的第一个元素，即头部元素。如果该Key不存，返回null。
     *
     * @param key
     * @return
     */
    public Object lpop(final String key) {
        return this.execute(new JedisAction<Object>() {
            public Object doAction(Jedis jedis) {
                return SimpleUtils.unserialize(jedis.lpop(key.getBytes()));
            }
        });
    }

    /**
     * 返回并弹出指定Key关联的链表中的最后一个元素，即尾部元素。如果该Key不存，返回nil。
     *
     * @param key
     * @return
     */
    public Object rpop(final String key) {
        return this.execute(new JedisAction<Object>() {
            public Object doAction(Jedis jedis) {
                return SimpleUtils.unserialize(jedis.rpop(key.getBytes()));
            }
        });
    }

    /**
     * 将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     *
     * @param key
     * @return
     */
    public Long incr(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    /**
     * 带有效期的递增计数器
     *
     * @param key
     * @param expire
     * @return
     */
    public Long incr(final String key, final int expire) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                Long result = jedis.incr(key);
                jedis.expire(key, expire);
                return result;
            }
        });
    }

    /**
     * 将 key 中储存的数字加上指定的增量值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long incrBy(final String key, final long integer) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.incrBy(key, integer);
            }
        });
    }

    /**
     * 将 key 中储存的数字值减一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     *
     * @param key
     * @return
     */
    public Long decr(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    /**
     * 带有效期的递减计数器
     *
     * @param key
     * @param expire
     * @return
     */
    public Long decr(final String key, final int expire) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                Long result = jedis.decr(key);
                jedis.expire(key, expire);
                return result;
            }
        });
    }

    /**
     * key 所储存的值减去给定的减量值
     *
     * @param key
     * @param integer
     * @return
     */
    public Long decrBy(final String key, final long integer) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.decrBy(key, integer);
            }
        });
    }

    /**
     * 为指定的 key 追加值
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
     * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
     *
     * @param key
     * @param value
     * @return
     */
    public Long append(final String key, final String value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.append(key, value);
            }
        });
    }

    /**
     * 为哈希表key 中的域field 的值加上增量increment。增量也可以为负数，相当于对给定域进行减法操作。 如果key
     * 不存在，一个新的哈希表被创建并执行HINCRBY 命令。 如果域field 不存在，那么在执行命令前，域的值被初始化为0 。
     * 对一个储存字符串值的域field 执行HINCRBY 命令将造成一个错误。
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hincrBy(final String key, final String field, final long value) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.hincrBy(key, field, value);
            }
        });
    }


    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return this.execute(new JedisAction<Boolean>() {
            public Boolean doAction(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    /**
     * 给定 key 设置过期时间，expire秒后过期
     *
     * @param key
     * @param expire
     * @return
     */
    public Long expire(final String key, final int expire) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.expire(key, expire);
            }
        });
    }

    /**
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     *
     * @param key
     * @param unixTimestamp
     * @return
     */
    public Long expireAt(final String key, final Long unixTimestamp) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.expireAt(key, unixTimestamp);
            }
        });
    }

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param key
     * @return 符合给定模式的 key 列表 (Array)
     */
    public Set<String> keys(final String key) {
        return this.execute(new JedisAction<Set<String>>() {
            public Set<String> doAction(Jedis jedis) {
                return jedis.keys(key);
            }
        });
    }

    public Set<byte[]> bKeys(final String key) {
        return this.execute(new JedisAction<Set<byte[]>>() {
            public Set<byte[]> doAction(Jedis jedis) {
                return jedis.keys(key.getBytes());
            }
        });
    }

    /**
     * 以秒为单位返回 key 的剩余过期时间 (TTL, time to live)
     * 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
     *
     * @param key
     * @return
     */
    public Long ttl(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.ttl(key);
            }
        });
    }

    /**
     * 修改 key 的名称
     * 改名成功时提示 OK ，失败时候返回一个错误。
     *
     * @param key
     * @return 改名成功时提示 OK ，失败时候返回一个错误。
     */
    public String rename(final String key, final String newKey) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.rename(key, newKey);
            }
        });
    }

    /**
     * 返回 key 所储存的值的类型
     * 返回 key 的数据类型，数据类型有：
     * none (key不存在)
     * string (字符串)
     * list (列表)
     * set (集合)
     * zset (有序集)
     * hash (哈希表)
     *
     * @param key
     * @return
     */
    public String type(final String key) {
        return this.execute(new JedisAction<String>() {
            public String doAction(Jedis jedis) {
                return jedis.type(key);
            }
        });
    }

    /**
     * 将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     * 假如集合 key 不存在，则创建一个只包含添加的元素作成员的集合。
     *
     * @param key
     * @param members
     * @return 当集合 key 不是集合类型时，返回一个错误。
     */
    public Long sadd(final String key, final String... members) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.sadd(key, members);
            }
        });
    }

    /**
     * 移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param key
     * @param members
     * @return 被成功移除的元素的数量，不包括被忽略的元素
     */
    public Long srem(final String key, final String... members) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.srem(key, members);
            }
        });
    }

    /**
     * 判断成员元素是否是集合的成员
     *
     * @param key
     * @param member
     * @return 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
     */
    public Boolean sismember(final String key, final String member) {
        return this.execute(new JedisAction<Boolean>() {
            public Boolean doAction(Jedis jedis) {
                return jedis.sismember(key, member);
            }
        });
    }

    /**
     * 返回集合中的所有的成员
     * 不存在的集合 key 被视为空集合
     *
     * @param key
     * @return 集合中的所有成员
     */
    public Set<String> smembers(final String key) {
        return this.execute(new JedisAction<Set<String>>() {
            public Set<String> doAction(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
    }

    /**
     * 返回集合中元素的数量
     *
     * @param key
     * @return 集合的数量。 当集合 key 不存在时，返回 0
     */
    public Long scard(final String key) {
        return this.execute(new JedisAction<Long>() {
            public Long doAction(Jedis jedis) {
                return jedis.scard(key);
            }
        });
    }
}
