###     Exchanger 的使用

1.exchange() 方法阻塞的特点

> 此方法被调用后等待其他线程来取得数据，如果没有其他线程取得数据，则一直阻塞等待。

代码

```

package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class ThreadA extends Thread {
    private Exchanger<String> exchanger;

    public ThreadA(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            System.out.println("在线程A中得到线程B的值=" + exchanger.exchange("Chinese A"));
            System.out.println("A end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


```

测试类

```

package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class BlockTest {
    public static void main(String[] args){
        Exchanger<String> exchanger = new Exchanger<>();
        Thread a = new ThreadA(exchanger);
        a.start();
        System.out.println("main end");
    }
}


```

测试结果

> main end

当没有其他线程来调用时，会阻塞当前线程。

2.exchange() 方法传递数据

> 此方法也可以在不同线程之间传递数据

添加代码

```

package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class ThreadB extends Thread{
    private Exchanger<String> exchanger;

    public ThreadB(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            System.out.println("在线程B中得到线程A的值=" + exchanger.exchange("Chinese B"));
            System.out.println("B end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


```

修改测试代码如下：

```

package com.lhc.concurrent.exchanger.exchange;

import java.util.concurrent.Exchanger;

public class BlockTest {
    public static void main(String[] args){
        Exchanger<String> exchanger = new Exchanger<>();
        Thread a = new ThreadA(exchanger);
        Thread b = new ThreadB(exchanger);
        a.start();
        b.start();
        System.out.println("main end");
    }
}


```

测试结果

> main end

> 在线程B中得到线程A的值=Chinese A

> B end

> 在线程A中得到线程B的值=Chinese B

>  A end

3.exchange() 方法设置超时时间

> exchange(V v,long timeout,TimeUnit unit) 方法在指定的时间没有其他线程获取数据，则出现超时异常