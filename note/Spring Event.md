### Spring中事件的发布订阅机制

1.1

    事件的发布者发布事件，事件的监听这对对应的事件进行监听，当监听到对应的事件时，就会触发调用相关的方法。因此，在事件处理中，事件是核心，是事件发布者和事件监听者的桥梁。

1.2

    Spring 是基于Observer模式（java.util包中有对应实现），提供了针对Bean的事件发布功能。  
    Spring中相关的主要有四个接口：ApplicationEvent,ApplicationEventPublisher,ApplicationEventPublisherAware,ApplicationListener.

1.2.1 ApplicationEvent

    ApplicationEvent继承自JavaSE中的EventObject，我们在使用的时候可以继承这个接口来定义事件，进行信息的传递，因为ApplicationListener只接受ApplicationEvent或者他的子类。

1.2.2 ApplicationEventPublisher

    事件的发布接口,通过publishEvent(ApplicationEvent event)方法来发布消息。

1.2.3 ApplicationEventPublisherAware

> 这个接口的解释是: Interface to be implemented by any object that wishes to be notified of the ApplicationEventPublisher (typically the ApplicationContext) that it runs in. 
    (这是一个 任何想要实现通知正在运行的ApplicationEventPublisher的对象需要去实现的接口)。
    
    通过这个接口的void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)方法，将ApplicationEventPublisher注入进去，来进行消息通知。
    
1.2.4 ApplicationListener

> 监听器接口，ApplicationListener中的void onApplicationEvent(E event)来接受消息事件，进行消息处理。                                              

```

public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 */
	void onApplicationEvent(E event);

}

```

    通过源代码发现，需要使用ApplicationEvent或者ApplicationEvent的子类。
    
1.3 具体使用

Event

```

public class MyEvent extends ApplicationEvent {
    private String words;

    public MyEvent(Object source,String words) {
        super(source);
        this.words = words;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}

```

Aware

```

public class MyPublishAware implements ApplicationEventPublisherAware {

    public void sayHello() {
        MyEvent myEvent = new MyEvent(this, new String("helloWorld"));
        applicationEventPublisher.publishEvent(myEvent);
    }

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        
    }
}

```

Listenner

```

public class MyListenner implements ApplicationListener {
    @Override
    public void onApplicationEvent(MyEvent e) {
        System.out.println(e.getClass().toString());
        System.out.println(e.getWords());
    }
}

```

3.1 Spring自己定义的几个ApplicationEvent的实现类

    1)ContextRefreshedEvent：当ApplicationContext初始化或者刷新时触发该事件。
    
    2)ContextClosedEvent：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁。
    
    3)ContestStartedEvent：当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件。
    
    4)ContestStopedEvent：当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件。

    5)ApplicationContextEvent 这个是平时经常用到的。 