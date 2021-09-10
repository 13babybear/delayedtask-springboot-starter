package cn.bounter.delayedtask.starter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务管理器
 * 负责启动多个线程来异步执行接收到的任务
 */
public class TaskManager<T> {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void executeTask(IDelayedTask delayedTask, T param) {
        executorService.execute(() -> delayedTask.execute(param));
    }
}
