package com.wenjifeng.idgenerator.util.db;

import com.wenjifeng.idgenerator.entity.DBCounter;
import com.wenjifeng.idgenerator.enums.SequenceEnum;
import com.wenjifeng.idgenerator.service.SequenceService;
import com.wenjifeng.idgenerator.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 基于数据库序列方式生成id工具类
 * @className SequenceUtil
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 10:20
 * @Version 1.0
 **/
@Slf4j
public class SequenceUtil {
    private static Map<String,DBCounter> map = new HashMap<>();

    public static Long getSequence (SequenceEnum sequenceEnum) {
        synchronized (sequenceEnum) {
            DBCounter DBCounter = map.get(sequenceEnum.getSeqName());
            if (null == DBCounter) {
                DBCounter = new DBCounter();
                map.put(sequenceEnum.getSeqName(), DBCounter);
            }
            BigInteger nextvals = null;
            BigInteger step = BigInteger.ONE;// 每次增量
            BigInteger seqIncrement = new BigInteger(sequenceEnum.getSeqIncrement());// 序列步长值
            // 1）如果CurveKey为空，则表示即将进行第一次获取序列
            // 2) 如果当前容器值与当前容器最大值相等，则表示当前内存容器已经取完
            if (null == DBCounter.CurveKey || DBCounter.MaxKey.compareTo(DBCounter.CurveKey) == 0) {
                nextvals = getSequenceVal(sequenceEnum.getSeqName());
                DBCounter.CurveKey = nextvals;
                // 容器最大值 == 当前值 + 序列步长值 - 增量
                DBCounter.MaxKey = nextvals.add(seqIncrement).subtract(step);
            } else {
                nextvals = DBCounter.CurveKey.add(step);
                DBCounter.CurveKey = nextvals;
            }
            return nextvals.longValue();
        }
    }

     /**
      * @Description 查询数据库，获取序列值
      * @Author wen_jf@suixingpay.com
      * @Date 2019/8/16 10:53
      **/
    private static BigInteger getSequenceVal(String seqName) {
        SequenceService  sequenceService = ApplicationContextUtil.getBean(SequenceService.class);
        BigInteger sequence = sequenceService.currentValue(seqName);
        return sequence;
    }

}
