# Blocks MQ
## MetaQ
MetaQ（全称Metamorphosis）是一个高性能、高可用、可扩展的分布式消息中间件，思路起源于LinkedIn的Kafka，但并不是Kafka的一个Copy。MetaQ具有消息存储顺序写、吞吐量大和支持本地和XA事务等特性，适用于大吞吐量、顺序消息、广播和日志数据传输等场景，目前在淘宝和支付宝有着广泛的应用。

MetaQ适合的场景？
日志传输，高吞吐量的日志传输，这本来也是kafka的强项。
消息广播功能，如广播缓存配置失效。
数据的顺序同步功能，如MySQL binlog复制。
分布式环境下（broker、producer、consumer都为集群）的消息路由，对顺序和可靠性有极高要求的场景。
作为一般MQ来使用的其他功能。

使用MetaQ应注意哪些事项？
MetaQ作为一个分布式的消息中间件，需要依赖zookeeper，对于一些规模不大、单机应用的场景，我个人并不是特别支持尝试用MetaQ，因为多一个依赖系统，其实就是多一份风险，在这些简单场景下，可能类似memcacheq、kestrel甚至redis等轻量级MQ就非常合适。而MetaQ一开始就是为大规模分布式系统设计的，如果不当使用，可能没有带来好处，反而多出一堆问题。开发者需要根据自己面对的场景，团队的技术能力，做出一个合适的选择。

http://www.iteye.com/magazines/107

## RocketMQ
RocketMQ的前身是Metaq，当 Metaq 3.0发布时，产品名称改为 RocketMQ。MetaQ2.x版本由于依赖了alibaba公司内部其他系统，对于公司外部用户使用不够友好，推荐使用3.0版本。

### 订阅
RocketMQ消息订阅有两种模式，一种是Push模式，即MQServer主动向消费端推送；另外一种是Pull模式，即消费端在需要时，主动到MQServer拉取。但在具体实现时，Push和Pull模式都是采用消费端主动拉取的方式。

项目地址： https://github.com/alibaba/RocketMQ

## rabbitMQ
