package com.wenjifeng.idgenerator.util.snowflake;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 名称：IdWorker.java
 * </p>
 * <p>
 * 描述：分布式自增长ID
 * </p>
 *
 * <pre>
 *     Twitter的 Snowflake　JAVA实现方案
 * </pre>
 * <p>
 * 核心代码为其IdWorker这个类实现，其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用： 1||0---0000000000
 * 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 * 在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），接下来的41位为毫秒级时间，
 * 然后5位datacenter标识位，5位机器ID（并不算标识符，实际是为线程标识），
 * 然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
 * 这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），
 * 并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * <p>
 * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
 *
 */
@Slf4j
public class IdWorker {

    // 时间起始标记点，作为基准，一般取系统的最近时间（一旦确定不能变动）
    private static final long TWEPOCH = 1482831049587L;
    // 机器标识位数
    private static final long WORKERIDBITS = 5L;
    // 数据中心标识位数
    private static final long DATACENTERIDBITS = 5L;
    // 机器ID最大值
    private static final long MAXWORKERID = -1L ^ -1L << WORKERIDBITS;
    // 毫秒内自增位
    private static final long SEQUENCEBITS = 12L;
    // 机器ID偏左移12位
    private static final long WORKERIDSHIFT = SEQUENCEBITS;
    // 时间毫秒左移22位
    private static final long TIMESTAMPLEFTSHIFT = SEQUENCEBITS + WORKERIDBITS + DATACENTERIDBITS;

    private static final long SEQUENCEMASK = -1L ^ (-1L << SEQUENCEBITS);
    /* 上次生产id时间戳 */
    private static long lastTimestamp = -1L;
    // 0，并发控制
    private long sequence = 0L;

    private final long workerId;

    public IdWorker(final long workerId) {
        super();
        if (workerId > MAXWORKERID || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", MAXWORKERID));
        }
        this.workerId = workerId;
    }

    /**
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCEMASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        // 如果当前时间戳小于最后一次执行的时间戳，说明时间回调了，给予异常处理
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        lastTimestamp = timestamp;
        long nextId = ((timestamp - TWEPOCH << TIMESTAMPLEFTSHIFT)) | (workerId << WORKERIDSHIFT)
                | (sequence);
        return nextId;
    }

    /**
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(MAXWORKERID);
    }
}
