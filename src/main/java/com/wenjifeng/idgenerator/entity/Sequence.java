package com.wenjifeng.idgenerator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 基于数据库序列方式生成id
 * @className Sequence
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 10:07
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
public class Sequence {
    /**
     * 序列名称（唯一）
     */
    private String name;
    /**
     * 当前值
     */
    private Long currentValue;
    /**
     * 序列步长（每次取值量）
     */
    private Long incrementValue;
}
