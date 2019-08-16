package com.wenjifeng.idgenerator.service;

import com.wenjifeng.idgenerator.mapper.SequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @Description 基于数据库序列方式生成id的service
 * @className SequenceService
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 10:15
 * @Version 1.0
 **/
@Service
public class SequenceService {
    @Autowired
    private SequenceMapper sequenceMapper;

    public BigInteger currentValue (String name) {
        Long sequence = this.sequenceMapper.currentValue(name);
        return BigInteger.valueOf(sequence);
    }
}
