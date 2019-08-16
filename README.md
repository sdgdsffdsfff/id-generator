# id-generator
总结分布式id生成策略（基于数据库序列生成id、基于redis生成id、基于雪花算法生成分布式id）


1：基于数据库序列生成分布式id<br/>
    1）参考代码：com.wenjifeng.idgenerator.util.db.SequenceUtil<br/>
    2）参考初始化脚本：sql/sequence.sql<br/>
    3）参考测试类：com.wenjifeng.idgenerator.controller.db.SequenceController和com.wenjifeng.idgenerator.controller.db.ScheduledComponent<br/>

2：基于redis生成分布式id<br/>
    1）参考代码：com.wenjifeng.idgenerator.util.redis.RedisIdUtil<br/>
    2）参考测试类：com.wenjifeng.idgenerator.controller.redis.RedisIdController和com.wenjifeng.idgenerator.controller.redis.RedisScheduledComponent<br/>
    3）此程序redis是基于redis集群方式连接，请根据需要，修改redis集群地址或者使用单机版的redis<br/>

3：基于snowflake雪花算法生成分布式id<br/>
    1）参考代码：com.wenjifeng.idgenerator.util.snowflake.IdWorker<br/>
    2）参考测试类：com.wenjifeng.idgenerator.controller.snowflake.SnowflakeController和com.wenjifeng.idgenerator.controller.snowflake.SnowflakeScheduledComponent<br/>
    3）基于雪花算法代码逻辑是参考百度，只做了简单的测试<br/>
  
注：所有的代码只是用于自己练习学习使用，若用于生产，请多多测试和验证！
