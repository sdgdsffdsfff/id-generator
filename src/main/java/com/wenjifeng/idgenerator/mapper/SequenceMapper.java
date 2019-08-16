package com.wenjifeng.idgenerator.mapper;

import java.math.BigInteger;

/**
 * @Description 基于数据库序列方式生成id的Mapper
 * @className SequenceMapper
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 10:10
 * @Version 1.0
 **/
public interface SequenceMapper {
     /**
      * @Description 根据序列名称取出当前序列值（每取一次当前值增加一个步长值）
      * @Author wen_jf@suixingpay.com
      * @Date 2019/8/16 10:11
      **/
     Long currentValue (String name);
}
