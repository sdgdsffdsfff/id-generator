package com.wenjifeng.idgenerator.util.redis;

import com.wenjifeng.idgenerator.entity.RedisCounter;
import com.wenjifeng.idgenerator.enums.RedisIdEnum;
import com.wenjifeng.idgenerator.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 基于redis生成序列号id
 * @className RedisIdUtil
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 14:45
 * @Version 1.0
 **/
@Slf4j
public class RedisIdUtil {

    public static Map<String, RedisCounter> map = new HashMap<>();

    public static Long getId(RedisIdEnum redisIdEnum) {

        synchronized (redisIdEnum) {
            RedisCounter redisCounter = map.get(redisIdEnum.getKey());
            if (null == redisCounter) {
                redisCounter = new RedisCounter();
                map.put(redisIdEnum.getKey(),redisCounter);
            }
            BigInteger nextvals = null;
            BigInteger step = BigInteger.ONE;// 每次增量
            BigInteger seqIncrement = new BigInteger(redisIdEnum.getSeqIncrement());// 序列步长值
            // 1）如果CurveKey为空，则表示即将进行第一次获取序列
            // 2) 如果当前容器值与当前容器最大值相等，则表示当前内存容器已经取完
            if (null == redisCounter.CurveKey || redisCounter.MaxKey.compareTo(redisCounter.CurveKey) == 0) {
                nextvals = getNextvalsByRedis(redisIdEnum.getKey(),seqIncrement);
                redisCounter.CurveKey = nextvals;// 容器当前值
                redisCounter.MaxKey = nextvals.add(seqIncrement).subtract(step);// 容器最大值 == 当前值 + 序列步长值 - 增量
            } else {
                nextvals = redisCounter.CurveKey.add(step);
                redisCounter.CurveKey = nextvals;
            }
            return nextvals.longValue();
        }
    }

    private static BigInteger getNextvalsByRedis(String key,BigInteger seqIncrement) {
        JedisCluster jedisCluster = ApplicationContextUtil.getBean(JedisCluster.class);
        Long id = jedisCluster.incrBy(key,seqIncrement.longValue());
        return new BigInteger(id.toString());
    }
}
