package cn.bounter.delayedtask.starter;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 延时队列
 */
public class DelayedQueue {

    @Autowired
    RedissonClient redissonClient;


    /**
     * 添加到延时队列
     * @param data          任务数据
     * @param delayTime     延时时间
     * @param timeUnit      时间单位
     * @param taskClazz     任务类型
     * @param <T>
     */
    public <T> void add(T data, long delayTime, TimeUnit timeUnit, Class taskClazz) {
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(taskClazz.getSimpleName());
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        delayedQueue.offer(data, delayTime, timeUnit);
    }
}
