<h1>Virtual Threads(Java 21 ) Springboot 3.2 Spring 6</h1>

<h2>Virtual Threads vs Platform threads</h2>
Virtual Threads does not rely on OS thread
Decoupled from hardware
abstraction layer provided by JVM

<h2>Enabling Virtual Threads</h2> 
`spring.threads.virtual.enabled = true`

<h2>Maven</h2>
pom.xml  

<h2> Configuration and beans</h2>
ApplicationTaskExecutor overrides the default Executor to start a new virtual thread for each task.
ProtocolHandlerVirtualThreadExecutorCustomizer customizes TomcatProtocolHandler
`@ConditionalOnProperty`
to enable or disable virtual threads from the application.yaml file

```
spring:
    thread-executor: virtual
```

```
@EnableAsync
@Configuration
@ConditionalOnProperty{
    value = "spring.thread-executor",
    havingValue = "virtual"
}
public class ThreadConfig {
    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }
}   @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            };
        }
    }

```
<h2>CHecking virtual Thread Running</h2>
```
@RestController
@RequestMapping("/thread")
public class ThreadController {
    @GetMapping("/name")
    public class getThreadName() {
        return Thread.currentThread().toString();
    }
}
```
<h2>Performance</h2>
Good only for CPU- unintensive tasks as task requires minimal blocking