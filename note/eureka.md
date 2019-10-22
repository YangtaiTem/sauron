### @EnableEurekaServer 

1.1 在项目启动类上使用@EnableEurekaServer，可以将项目作为SpringCloud中的注册中心。那么这个注解做了哪些事呢？

1.2 点进去@EnableEurekaServer 这个注解，可以看到介绍时是这样的：
    Annotation to activate Eureka Server related configuration {@link EurekaServerAutoConfiguration}（激活Eureka服务器相关配置EurekaServerAutoConfiguration的注释）
    可以将@EnableEurekaServer 这个注解看作是一个开关，开启时，会激活相关配置，会作为注册中心。同时，他又引入了EurekaServerMarkerConfiguration类。
    
1.3 点进去EurekaServerMarkerConfiguration这个类，可以看到介绍是这样的：
    Responsible for adding in a marker bean to activate {@link EurekaServerAutoConfiguration}。（负责添加一个标记来激活配置类EurekaServerAutoConfiguration）
    这个类中，向容器注入了一个类EurekaServerMarkerConfiguration.Marker，作用就是激活配置类。源代码是这样的：
    
```

@Configuration
public class EurekaServerMarkerConfiguration {

	@Bean
	public Marker eurekaServerMarkerBean() {
		return new Marker();
	}

	class Marker {
	}
}


```

1.4 下面来看配置类EurekaServerAutoConfiguration。这个类上面有三个比较重要的注释：

```

@Configuration
@Import(EurekaServerInitializerConfiguration.class)
@ConditionalOnBean(EurekaServerMarkerConfiguration.Marker.class)
@EnableConfigurationProperties({ EurekaDashboardProperties.class,
		InstanceRegistryProperties.class })
```

@Configuration注解表示这是一个配置类，通过@Bean注解声明一些注入到Spring IOC容器中的Bean。

@Import(EurekaServerInitializerConfiguration.class)注解表示它导入了EurekaServerInitializerConfiguration这个类。

@ConditionalOnBean(EurekaServerMarkerConfiguration.Marker.class)注解表示只要Spring容器中有EurekaServerMarkerConfiguration.Marker.class类的实例存在，那么就会将这个EurekaServerAutoConfiguration也注入到Spring容器中。

@EnableConfigurationProperties注解 将EurekaDashboardProperties和InstanceRegistryProperties对应的配置属性类注入容器中


查看EurekaServerAutoConfiguration的源代码，可以发现它向Spring容器中注入了一些bean。举几个例子：

```
    
    这个bean中保存了这个实例的一些信息，名称为"Eureka Server"
	@Bean
	public HasFeatures eurekaServerFeature() {
		return HasFeatures.namedFeature("Eureka Server",
				EurekaServerAutoConfiguration.class);
	}

```

```
    
        //matchIfMissing属性表示缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错 
	//这个是仪表盘相关的Bean
	@Bean
	@ConditionalOnProperty(prefix = "eureka.dashboard", name = "enabled", matchIfMissing = true)
	public EurekaController eurekaController() {
		return new EurekaController(this.applicationInfoManager);
	}
    
```

这两个实例来自com.netflix.eureka，将相应的处理逻辑交到com.netflix.eureka中，它们接手了真正的EurekaServer的启动逻
辑，SpringCloud 只是在容器中将这些Bean初始化。

```

        // 
        @Bean
	public EurekaServerContext eurekaServerContext(ServerCodecs serverCodecs,
			PeerAwareInstanceRegistry registry, PeerEurekaNodes peerEurekaNodes) {
		return new DefaultEurekaServerContext(this.eurekaServerConfig, serverCodecs,
				registry, peerEurekaNodes, this.applicationInfoManager);
	}

	@Bean
	public EurekaServerBootstrap eurekaServerBootstrap(PeerAwareInstanceRegistry registry,
			EurekaServerContext serverContext) {
		return new EurekaServerBootstrap(this.applicationInfoManager,
				this.eurekaClientConfig, this.eurekaServerConfig, registry,
				serverContext);
	}

```

向容器中初始化一个Jersey 过滤器。   
tips:使用FilterRegistrationBean向Spring中注册过滤器十分方便。

```

        @Bean
	public FilterRegistrationBean jerseyFilterRegistration(
			javax.ws.rs.core.Application eurekaJerseyApp) {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new ServletContainer(eurekaJerseyApp));
		bean.setOrder(Ordered.LOWEST_PRECEDENCE);
		bean.setUrlPatterns(
				Collections.singletonList(EurekaConstants.DEFAULT_PREFIX + "/*"));

		return bean;
	}

```

1.5 现在，在回过头来看一下配置类EurekaServerAutoConfiguration导入的EurekaServerInitializerConfiguration
这个类。

首先可以发现它使用了@Configuration注解。然后实现了ServletContextAware, SmartLifecycle, Ordered三个接口，实现了这些接口的方法。

```

	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//TODO: is this class even needed now?
					eurekaServerBootstrap.contextInitialized(EurekaServerInitializerConfiguration.this.servletContext);
					log.info("Started Eureka Server");

					publish(new EurekaRegistryAvailableEvent(getEurekaServerConfig()));
					EurekaServerInitializerConfiguration.this.running = true;
					publish(new EurekaServerStartedEvent(getEurekaServerConfig()));
				}
				catch (Exception ex) {
					// Help!
					log.error("Could not initialize Eureka servlet context", ex);
				}
			}
		}).start();
	}

	private EurekaServerConfig getEurekaServerConfig() {
		return this.eurekaServerConfig;
	}

	private void publish(ApplicationEvent event) {
		this.applicationContext.publishEvent(event);
	}

	@Override
	public void stop() {
		this.running = false;
		eurekaServerBootstrap.contextDestroyed(this.servletContext);
	}

```

上面代码主要实现了start() 和 stop()方法。这两个方法的主要作用是初始化servlet相关上下文，在
start的时候调用EurekaServerBootstrap.contextInitialized，在stop的时候调用eurekaServerBootstrap.contextDestroyed，
都是借助eurekaServerBootstrap类来实现，而eurekaServerBootstrap里头部分调用了EurekaServerContext。再来看EurekaServerBootstrap
中contextInitialized()方法的代码：

```

	public void contextInitialized(ServletContext context) {
		try {
			initEurekaEnvironment();
			initEurekaServerContext();

			context.setAttribute(EurekaServerContext.class.getName(), this.serverContext);
		}
		catch (Throwable e) {
			log.error("Cannot bootstrap eureka server :", e);
			throw new RuntimeException("Cannot bootstrap eureka server :", e);
		}
	}
	
```

```
	
    protected void initEurekaEnvironment() throws Exception {
        log.info("Setting the eureka configuration..");

        String dataCenter = ConfigurationManager.getConfigInstance()
                .getString(EUREKA_DATACENTER);
        if (dataCenter == null) {
            log.info(
                    "Eureka data center value eureka.datacenter is not set, defaulting to default");
            ConfigurationManager.getConfigInstance()
                    .setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, DEFAULT);
        }
        else {
            ConfigurationManager.getConfigInstance()
                    .setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, dataCenter);
        }
        String environment = ConfigurationManager.getConfigInstance()
                .getString(EUREKA_ENVIRONMENT);
        if (environment == null) {
            ConfigurationManager.getConfigInstance()
                    .setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, TEST);
            log.info(
                    "Eureka environment value eureka.environment is not set, defaulting to test");
        }
        else {
            ConfigurationManager.getConfigInstance()
                    .setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, environment);
        }
    }

    protected void initEurekaServerContext() throws Exception {
        // For backward compatibility
        JsonXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(),
                XStream.PRIORITY_VERY_HIGH);
        XmlXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(),
                XStream.PRIORITY_VERY_HIGH);

        if (isAws(this.applicationInfoManager.getInfo())) {
            this.awsBinder = new AwsBinderDelegate(this.eurekaServerConfig,
                    this.eurekaClientConfig, this.registry, this.applicationInfoManager);
            this.awsBinder.start();
        }

        EurekaServerContextHolder.initialize(this.serverContext);

        log.info("Initialized server context");

        // Copy registry from neighboring eureka node
        int registryCount = this.registry.syncUp();
        this.registry.openForTraffic(this.applicationInfoManager, registryCount);

        // Register all monitoring statistics.
        EurekaMonitors.registerAllStats();
    }

```

主要是调用了initEurekaEnvironment()和initEurekaServerContext()方法，来初始化环境配置和初始化上下文。可以看到
initEurekaEnvironment()是设置了一些属性值，在initEurekaServerContext()方法中主要调用了	EurekaServerContextHolder.initialize(this.serverContext),this.registry.syncUp(),
this.registry.openForTraffic(this.applicationInfoManager, registryCount),这三个方法。EurekaServerContextHolder.initialize(this.serverContext),this.registry.syncUp()
主要是给非IOC容器引用EurekaServerContext,registry.syncUp()从其他eureka server获取实例信息，然后注册到本server，然后复制到其他server节点上。registry.openForTraffic()标识自身server的
状态为UP，表示可以开始接收请求。

