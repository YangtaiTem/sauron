@ConfigurationProperties的使用

####在Springboot中，使用@ConfigurationProperties注解来加载配置文件中的属性，
注入配置Bean特别方便，下面来介绍一下@ConfigurationProperties的使用相关。

主要有两块内容：
    
    1.@ConfigurationProperties的使用
    2.@ConfigurationProperties生效的两种方式
    
###### 1.@ConfigurationProperties的使用

首先是配置文件的内容
    
```text
    
    # redis
    spring.redis.host=localhost
    spring.redis.port=6379
    spring.redis.password=123456
    
```

接下来注入属性的bean必须包含对应属性的set方法

```java

@ConfigurationProperties(prefix = "spring.boot")
public class RedisConfig {

    private String host;
    private String port;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

```

###### 2.@ConfigurationProperties生效的两种方式

第一种方法直接在配置类上加上@Component注解，位置就在这个地方

```java

@Component
@ConfigurationProperties(prefix = "spring.boot")

```

测试可以正常注入。

第二种方法就是在使用这个bean的类上加入@EnableConfigurationProperties(RedisConfig.class)，这样
也可以让上面的属性注入Bean生效。

##### 附：另外一种注入属性Bean的方法

使用@Configuration和@Value，可以起到同样的效果

```java

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
}

```