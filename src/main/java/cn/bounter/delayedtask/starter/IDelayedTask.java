package cn.bounter.delayedtask.starter;

/**
 * 延时任务标准接口，可以基于它实现不同的延时任务
 */
public interface IDelayedTask<T> {

    /**
     * 任务处理方法
     * @param param      任务执行参数
     */
    void execute(T param);
}
