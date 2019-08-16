package com.wenjifeng.idgenerator.controller.db;

import com.wenjifeng.idgenerator.enums.SequenceEnum;
import com.wenjifeng.idgenerator.util.db.SequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Description 定时,为了模拟分布式部署，在同一个时间点内，同时请求访问
 * @className ScheduledController
 * @Author wen_jf@suixingpay
 * @Date 2019/8/16 14:15
 * @Version 1.0
 **/
@Slf4j
@Component
public class ScheduledComponent {

    // 总请求量
    private static final int requestTotal = 2000;

    // 最大并发线程数20
    private static final int concurrentThreadNum = 40;

    @Scheduled(cron = "0 27 14 * * ?")
    public void scheduled(){
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
