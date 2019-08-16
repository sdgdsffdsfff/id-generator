package com.wenjifeng.idgenerator.enums;

/**
 * @Description 基于redis生成id
 * @className RedisIdEnum
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 15:07
 * @Version 1.0
 **/
public enum RedisIdEnum {
    REDIS_ID("redis_id","1000")
    ;

    RedisIdEnum(String key, String seqIncrement) {
        this.key = key;
        this.seqIncrement = seqIncrement;
    }

    public String getKey() {
        return key;
    }

    public String getSeqIncrement() {
        return seqIncrement;
    }

    private String key;
    private String seqIncrement;
}
