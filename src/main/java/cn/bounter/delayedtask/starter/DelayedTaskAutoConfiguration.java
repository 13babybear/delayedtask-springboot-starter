package cn.bounter.delayedtask.starter;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DelayedTaskAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DelayedTaskAutoConfiguration.class);

    private RedissonClient redissonClient;
    private Set<IDelayedTask> tasks;

    public DelayedTaskAutoConfiguration(RedissonClient redissonClient, ObjectProvider<Set<IDelayedTask>> tasksObjectProvider) {
        this.redissonClient = redissonClient;
        this.tasks = tasksObjectProvider.getIfAvailable();
    }

    @Bean
    public DelayedQueue delayedQueue() {
        return new DelayedQueue();
    }

    @EventListener(ApplicationReadyEvent.class)
    public <T> void initTask() {
        logger.info("开始初始化延时任务...");
        if (CollectionUtils.isEmpty(tasks)) {
            logger.warn("没有配置任何延时任务");
            return;
        }
        new Thread(() -> {
            while (true) {
                tasks.forEach(delayedTask -> {
                    RBlockingQueue<T> distinationQueue = redissonClient.getBlockingQueue(delayedTask.getClass().getSimpleName());
                    T data = distinationQueue.poll();
                    if (data != null) {
                        delayedTask.execute(data);
                    }
                });
            }
        }).start();
        logger.info("初始化延时任务成功，延时任务列表：{}", tasks.stream().map(iDelayedTask -> iDelayedTask.getClass().getSimpleName()).collect(Collectors.toList()));
    }

}
