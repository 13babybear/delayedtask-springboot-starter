package cn.bounter.delayedtask.starter;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * 任务调度器
 * 负责从延时队列中拉取到达执行时间的任务，然后分发给任务管理器执行
 */
public class JobManager {

    private static final Logger logger = LoggerFactory.getLogger(JobManager.class);

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private TaskManager taskManager;

    public <T> void startJob(Set<IDelayedTask> tasks) {
        new Thread(() -> {
            logger.info("任务调度器启动成功");
            while (true) {
                tasks.forEach(delayedTask -> {
                    try {
                        RBlockingQueue<T> distinationQueue = redissonClient.getBlockingQueue(delayedTask.getClass().getSimpleName());
                        T param = distinationQueue.poll();
                        if (param != null) {
                            taskManager.executeTask(delayedTask, param);
                        }
                    } catch (Exception e) {
                        logger.warn(e.getMessage(), e);
                    }
                });
            }
        }).start();
    }
}
