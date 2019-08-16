package com.wenjifeng.idgenerator.util.snowflake;

/**
 * @Description 基于snowflake雪花算法生成分布式id
 * @className SnowflakeUtil
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 17:35
 * @Version 1.0
 **/
public class SnowflakeUtil {
    private static IdWorker idWorker = new IdWorker(1);

    public static long getNextId() {
        return idWorker.nextId();
    }
}
