原生redis提供如下连接池：
JedisPool：是非分片连接池，连接单机
ShardedJedisPool：是分片式连接集群，基于一致性哈希算法实现的分布式Redis集群客户端
JedisSentinelPool：是非分片式Sentinel连接池

扩展：
ShardedJedisSentinelPool：分片式Sentinel连接池

Sentinel：
Sentinel是Redis2.6版开始加入的另一组独立运行的节点, 提供自动Failover的支持。Redis Sentinel具有的功能包括：
(1)监控：定时检查Redis的Master和Slave服务器是否正常；
(2)通知：在发生异常时通知系统管理员和其他程序；
(3)自动Failover：如果Master不可用，Sentinel会自动执行Failover，选择一个Slave提升为Master，其他的Salve会重新设置使用新的Master。当应用程序重新连接时会被通知连接新的Master。
Redis Sentinel 是一个分布式系统， 你可以在架构中运行多个 Sentinel 进程， 这些 Sentinel 进程通过相互通讯来判断一个主服务器是否断线， 以及是否应该执行故障转移。
虽然Redis Sentinel为一个单独的可执行文件 redis-sentinel，但实际上它只是一个运行在特殊模式下的 Redis 服务器， 你可以在启动一个普通 Redis 服务器时通过给定 –sentinel 选项来启动 Redis Sentinel
JedisSentinelPool是jedis对Redis Sentinel的客户端连接实现。