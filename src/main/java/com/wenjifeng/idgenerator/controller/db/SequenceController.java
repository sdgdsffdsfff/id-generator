package com.wenjifeng.idgenerator.controller.db;

import com.wenjifeng.idgenerator.enums.SequenceEnum;
import com.wenjifeng.idgenerator.util.db.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Description 基于数据库序列方式生成id的测试controller
 *              基于分布式测试：可以打多个不同的端口号jar包，通过java -jar 方式在本机启动多个应用，模拟分布式
 * @className SequenceController
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 11:09
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/db")
public class SequenceController {

    // 总请求量
    private static final int requestTotal = 2000;

    // 最大并发线程数20
    private static final int concurrentThreadNum = 40;

    @GetMapping("/getSequence")
    public Long getSequence () {
        return SequenceUtil.getSequence(SequenceEnum.USER);
    }

     /**
      * @Description 单机状态下，每秒QPS；与seqIncrement步长有关，如果seqIncrement越大，则QPS越大
      * 但是在实际运用中，最好不要设置太大
      * @Author wen_jf@suixingpay.com
      * @Date 2019/8/16 11:55
      **/
    @GetMapping("/qps")
    public Long qps() {
        Long count = 0L;
        Long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < 1000) {
            SequenceUtil.getSequence(SequenceEnum.USER);
            count++;
        }
        return count;
    }

     /**
      * @Description 多线程测试
      * @Author wen_jf@suixingpay.com
      * @Date 2019/8/16 12:00
      **/
    @GetMapping("/nums")
    public String nums() {
        ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
        Set<Long> s = Collections.synchronizedSet(new HashSet<Long>());
        new Thread(()->{
            int i = 2000;
            while (i-- > 0) {
                Long id = SequenceUtil.getSequence(SequenceEnum.USER);
                System.out.println(id);
                queue.add(id);
                s.add(id);
            }
        }).start();
        new Thread(()->{
            int i = 2000;

            while (i-- > 0) {
                Long id = SequenceUtil.getSequence(SequenceEnum.USER);
                System.out.println(id);
                queue.add(id);
                s.add(id);
            }
        }).start();
        return s.size() + "  " + queue.size();
    }

     /**
      * @Description 多线程并发测试
      * @Author wen_jf@suixingpay.com
      * @Date 2019/8/16 12:14
      **/
    @GetMapping("/concurrent")
    public void concurrent () {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(requestTotal);
        Semaphore semaphore = new Semaphore(concurrentThreadNum);
        long start = System.currentTimeMillis();

        for (int i = 0;i<requestTotal;i++) {
            int num = i;
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    String result = SequenceUtil.getSequence(SequenceEnum.USER).toString();
                    System.out.println(result);
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        log.info("执行耗时：{}",System.currentTimeMillis()-start);
        log.info("执行完毕");
    }
}
