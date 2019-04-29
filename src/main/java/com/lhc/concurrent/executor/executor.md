

```

/**
     * Creates a new {@code ThreadPoolExecutor} with the given initial parameters and default thread factory.
     *
     * @param corePoolSize the number of threads to keep in the pool, even if they are idle,   
     *          unless {@code allowCoreThreadTimeOut} is set
     *      核心线程数，即使空闲也会保存在线程池中，除非设置allowCoreThreadTimeOut
     * @param maximumPoolSize the maximum number of threads to allow in the pool
     *        线程中允许的最大线程数
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     *      核心线程之外的空闲线程的回收时间
     * @param unit the time unit for the {@code keepAliveTime} argument
     *      上面时间的单位
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     *      任务队列
     * @param threadFactory the factory to use when the executor creates a new thread
     *      创建线程时使用的工厂,Executors提供了Executors.defaultThreadFactory()
     * @param handler the handler to use when execution is blocked
     *        because the thread bounds and queue capacities are reached
     *      当处理程序由于已达到线程边界和队列容量而阻塞时，handler被使用
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue}
     *         or {@code handler} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             threadFactory, handler);
    }

````

shutdown() 和 shutdownNow()