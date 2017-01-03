package com.blocks.cache.redis.impl;

import com.blocks.cache.redis.ShardedJedisAction;
import com.blocks.cache.redis.ShardedJedisClient;
import com.blocks.cache.redis.pubsub.JedisMessageListener;
import com.blocks.cache.utils.SimpleUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 分片式连接redis集群
 * Created by lotus on 2016/12/30.
 */
public class ShardedJedisClientImpl implements ShardedJedisClient {
    private final Logger logger = Logger.getLogger(getClass().getName());

    /**
     * 分片连接池：连接redis集群，通过一致性哈希算法决定把数据存到哪台redis上
     * ShardedJedisPool是非分片式Sentinel连接池
     */
    //@Autowired
    private ShardedJedisPool sharedJedisPool;

    public ShardedJedisClientImpl(){}

    public ShardedJedisClientImpl(ShardedJedisPool sharedJedisPool) {this.sharedJedisPool = sharedJedisPool;}

    public void destory() {
        if (null != sharedJedisPool){
            sharedJedisPool.destroy();
        }
    }

    public <T> T execute(ShardedJedisAction<T> action) {
        ShardedJedis jedis = null;
        try{
            jedis = sharedJedisPool.getResource();
            return action.doAction(jedis);
        } catch (JedisConnectionException e) {
            if (null != e){
                throw e;
            }
        } finally {
            if (null != jedis){
                try {
                    sharedJedisPool.returnResource(jedis);
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                /*String result = jedis.set(key, value);
                jedis.expire(key, expire);
                return result;*/
                return shardedJedis.setex(key, expire, value);
            }
        });
    }

    public String get(final String key) {
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }



    /**
     * 在key存在时删除key，如果键被删除成功，命令执行后输出 1，否则将输出 0
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }
        });
    }

    public long del(final byte[] key) {
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.del(key.getBytes());
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.set(key.getBytes(), SimpleUtils.serialize(value));
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                /*String result = jedis.set(key.getBytes(), SimpleUtils.serialize(value));
                jedis.expire(key, expire);
                return result;*/
                return shardedJedis.setex(key.getBytes(), expire, SimpleUtils.serialize(value));
            }
        });
    }

    public String set(final byte[] key, final Object value) {
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, SimpleUtils.serialize(value));
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hset(key, field, value);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                Long result = shardedJedis.hset(key, field, value);
                shardedJedis.expire(key, expire);
                return result;
            }
        });
    }

    public long hsetObject(final String key, final String field, final Object value) {
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hset(key.getBytes(), field.getBytes(), SimpleUtils.serialize(value));
            }
        });
    }

    public long hsetObject(final String key, final String field, final Object value, final int expire) {
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                Long result = shardedJedis.hset(key.getBytes(), field.getBytes(), SimpleUtils.serialize(value));
                shardedJedis.expire(key.getBytes(), expire);
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hget(key, field);
            }
        });
    }

    public Object hgetObject(final String key, final String field) {
        return this.execute(new ShardedJedisAction<Object>() {
            public Object doAction(ShardedJedis shardedJedis) {
                return SimpleUtils.unserialize(shardedJedis.hget(key.getBytes(), field.getBytes()));
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
        return this.execute(new ShardedJedisAction<Map<String, String>>() {
            public Map<String, String> doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hgetAll(key);
            }
        });
    }

    public Map<String, Object> hgetAllObject(final String key) {
        return this.execute(new ShardedJedisAction<Map<String, Object>>() {
            public Map<String, Object> doAction(ShardedJedis shardedJedis) {
                Map<byte[], byte[]> byteMap = shardedJedis.hgetAll(key.getBytes());
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hmset(key, hash);
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                String result = shardedJedis.hmset(key, hash);
                shardedJedis.expire(key, expire);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hdel(key, field);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.lpush(key.getBytes(), SimpleUtils.serialize(value));
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.rpush(key.getBytes(), SimpleUtils.serialize(value));
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.llen(key.getBytes());
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
        return this.execute(new ShardedJedisAction<Object>() {
            public Object doAction(ShardedJedis shardedJedis) {
                return SimpleUtils.unserialize(shardedJedis.lpop(key.getBytes()));
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
        return this.execute(new ShardedJedisAction<Object>() {
            public Object doAction(ShardedJedis shardedJedis) {
                return SimpleUtils.unserialize(shardedJedis.rpop(key.getBytes()));
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.incr(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                Long result = shardedJedis.incr(key);
                shardedJedis.expire(key, expire);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.incrBy(key, integer);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.decr(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                Long result = shardedJedis.decr(key);
                shardedJedis.expire(key, expire);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.decrBy(key, integer);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.append(key, value);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hincrBy(key, field, value);
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
        return this.execute(new ShardedJedisAction<Boolean>() {
            public Boolean doAction(ShardedJedis shardedJedis) {
                return shardedJedis.exists(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, expire);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.expireAt(key, unixTimestamp);
            }
        });
    }

    /**
     * 查找所有符合给定模式 pattern 的 key
     * @param key
     * @return 符合给定模式的 key 列表 (Array)
     */
    public Set<String> keys(final String key) {
        return this.execute(new ShardedJedisAction<Set<String>>() {
            public Set<String> doAction(ShardedJedis shardedJedis) {
                //ShardedJedis不存在方法keys,具体原因详见：https://github.com/xetorthio/jedis/issues/325
                return shardedJedis.hkeys(key);
                //return jedis.keys(key);
            }
        });
    }

    public Set<byte[]> bKeys(final String key) {
        return this.execute(new ShardedJedisAction<Set<byte[]>>() {
            public Set<byte[]> doAction(ShardedJedis shardedJedis) {
                return shardedJedis.hkeys(key.getBytes());
                //return jedis.keys(key.getBytes());
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.ttl(key);
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
        return this.execute(new ShardedJedisAction<String>() {
            public String doAction(ShardedJedis shardedJedis) {
                return shardedJedis.type(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.sadd(key, members);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.srem(key, members);
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
        return this.execute(new ShardedJedisAction<Boolean>() {
            public Boolean doAction(ShardedJedis shardedJedis) {
                return shardedJedis.sismember(key, member);
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
        return this.execute(new ShardedJedisAction<Set<String>>() {
            public Set<String> doAction(ShardedJedis shardedJedis) {
                return shardedJedis.smembers(key);
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
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                return shardedJedis.scard(key);
            }
        });
    }

    /**
     * 订阅给定的一个或多个频道的信息
     *
     * @param pubSubListener
     * @param channels
     */
    public void subscrible(final JedisMessageListener pubSubListener, final String... channels) {
        new Thread(new Thread(){
            /**
             * If this thread was constructed using a separate
             * <code>Runnable</code> run object, then that
             * <code>Runnable</code> object's <code>run</code> method is called;
             * otherwise, this method does nothing and returns.
             * <p/>
             * Subclasses of <code>Thread</code> should override this method.
             *
             * @see #start()
             * @see #stop()
             * @see #Thread(ThreadGroup, Runnable, String)
             */
            @Override
            public void run() {
                //注：ShardedJedis没有发布和订阅功能，需要还原为Jedis，获取方式如下(完全可以重新配置一份JedisPool)：
                //参考http://blog.csdn.net/javaloveiphone/article/details/53259853
                ShardedJedis shardedJedis = sharedJedisPool.getResource();
                Jedis[] jedises = new Jedis[]{};
                jedises = shardedJedis.getAllShards().toArray(jedises);
                Jedis jedis = jedises[0];

                jedis.subscribe(pubSubListener, channels);
                //subscribe是一个阻塞的方法，在取消订阅该频道前，会一直阻塞在这，只有当取消了订阅才会执行下面的other code
                //other code
            }
        }).start();
    }

    /**
     * 向频道发布消息
     *
     * @param channel
     * @param message
     * @return 接收到信息的订阅者数量
     */
    public Long publish(final String channel, final String message) {
        return this.execute(new ShardedJedisAction<Long>() {
            public Long doAction(ShardedJedis shardedJedis) {
                //注：ShardedJedis没有发布和订阅功能，需要还原为Jedis，获取方式如下(完全可以重新配置一份JedisPool)：
                //参考http://blog.csdn.net/javaloveiphone/article/details/53259853
                Jedis[] jedises = new Jedis[]{};
                jedises = shardedJedis.getAllShards().toArray(jedises);
                Jedis jedis = jedises[0];

                return jedis.publish(channel, message);
            }
        });
    }
}
