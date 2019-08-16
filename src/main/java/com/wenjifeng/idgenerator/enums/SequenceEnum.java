package com.wenjifeng.idgenerator.enums;


/**
 * @Description 基于数据库序列方式生成id枚举
 * @className SequenceEnum
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 10:43
 * @Version 1.0
 **/
public enum SequenceEnum {
    USER("user","1000")
    ;
    private String seqName;
    private String seqIncrement;

    SequenceEnum(String seqName, String seqIncrement) {
        this.seqName = seqName;
        this.seqIncrement = seqIncrement;
    }

    public String getSeqName() {
        return seqName;
    }

    public String getSeqIncrement() {
        return seqIncrement;
    }
}
