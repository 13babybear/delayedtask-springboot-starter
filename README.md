# delayedtask-springboot-starter
### 超简单的延时任务框架  
##### 核心组件
- 延时队列 - 基于Redission的延时队列
- 任务调度器JobManager - 负责从延时队列中拉取到达执行时间的任务，然后分发给任务管理器执行
- 任务管理器TaskManager - 负责启动多个线程来异步执行接收到的任务  

使用示例参考另一个项目：https://github.com/13babybear/delayedtask-test
