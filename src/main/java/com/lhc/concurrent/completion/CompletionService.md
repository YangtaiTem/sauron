### CompletionService

1.1 CompletionService 的功能
    
    CompletionService的功能就是以异步的方式，一边产生新的任务，一边处理已完成任务的结果。 
    CompletionService主要解决一个什么问题呢？ 
        Future 接口调用get()方法取得处理的结果，但是这个方法是阻塞性的，如果调用get()
    方法时，任务尚未执行完成,get方法会一直阻塞到此任务执行完成为止。这样的后果就是一旦前面
    的任务耗时太长，后面的任务的get()方法就会排队等待，影响相率。
        使用CompletionService可以按照完成这些任务的时间顺序处理他们的结果。
        
        
1.2 代码

```

package com.lhc.concurrent.completion.nonBlock;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.concurrent.*;

public class MyCallable implements Callable<String>{
    private String userName;
    private long sleepTime;

    public MyCallable(String userName, long sleepTime) {
        this.userName = userName;
        this.sleepTime = sleepTime;
    }

    @Override
    public String call() throws Exception {
        System.out.println(userName);
        Thread.sleep(sleepTime);
        return "return " + sleepTime;
    }

    public static void main(String[] args) throws Exception{
        MyCallable name1 = new MyCallable("name1", 1050);
        MyCallable name2 = new MyCallable("name2", 1040);
        MyCallable name3 = new MyCallable("name3", 1030);
        MyCallable name4 = new MyCallable("name4", 1020);
        MyCallable name5 = new MyCallable("name5", 1010);

        ArrayList<Callable> callables = Lists.newArrayList();
        callables.add(name1);
        callables.add(name2);
        callables.add(name3);
        callables.add(name4);
        callables.add(name5);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 5,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        ExecutorCompletionService completionService = new ExecutorCompletionService(executor);

        for (int i = 0; i < callables.size(); i++) {
            completionService.submit(callables.get(i));
        }

        for (int i = 0; i < 5; i++){
            /**
             * 哪个任务先执行完，那个任务的返回值就先打印，解决了Future 阻塞的特点
             * 但是如果没有任何任务被执行完，则.take().get()方法还是呈阻塞特性。
             */
            System.out.println(completionService.take().get());
        }
    }
}

```

1.3测试结果

> name1  
  name5  
  name2  
  name3  
  name4  
  return 1010  
  return 1020  
  return 1030  
  return 1040  
  return 1050  


2.1 Future<V> submit(Runnable task, V result)方法  
    
    另外一种使用姿势

2.2 代码

```

public class User {
    private String name;
    private String password;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


package com.lhc.concurrent.completion.submit;

import java.util.concurrent.*;

public class MyRunnable implements Runnable{
    private User user;

    public MyRunnable(User user) {
        super();
        this.user = user;
    }

    @Override
    public void run() {
        user.setName("jt");
        user.setPassword("pwd");
        System.out.println("running end");
    }

    public static void main(String[] args) throws Exception{
        User user = new User("name", "pwd");
        MyRunnable myRunnable = new MyRunnable(user);

        ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorCompletionService completionService = new ExecutorCompletionService(executorService);
        Future<User> submit = completionService.submit(myRunnable, user);
        System.out.println(submit.get().getName() + " " + submit.get().getPassword());
    }
}


```

2.3 运行结果

> running end  
  jt pwd