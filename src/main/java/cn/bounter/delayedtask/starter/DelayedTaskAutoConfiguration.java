package cn.bounter.delayedtask.starter;

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

    private Set<IDelayedTask> tasks;

    public DelayedTaskAutoConfiguration(ObjectProvider<Set<IDelayedTask>> tasksObjectProvider) {
        this.tasks = tasksObjectProvider.getIfAvailable();
    }

    @Bean
    public DelayedQueue delayedQueue() {
        return new DelayedQueue();
    }

    @Bean
    public JobManager jobManager() {
        return new JobManager();
    }

    @Bean
    public TaskManager taskManager() {
        return new TaskManager();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        logger.info("开始初始化延时任务...");
        if (CollectionUtils.isEmpty(tasks)) {
            logger.warn("没有配置任何延时任务");
            return;
        }

        //启动任务调度器
        jobManager().startJob(tasks);

        logger.info("初始化延时任务成功，延时任务列表：{}", tasks.stream().map(iDelayedTask -> iDelayedTask.getClass().getSimpleName()).collect(Collectors.toList()));
    }

}
