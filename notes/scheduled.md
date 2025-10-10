Annotate method with @Scheduled:

Rules:
- void return type
- no parameter

```
@Configuration
@EnableScheduling
public class SpringConfig {
}
```
`<task:annotation-driven>`

<h2>Schedule Task at Fixed Delay</h2>
```
@Scheduled(fixedDelay = 1000)
public void scheduleFixedDelayTask() {
    
}
```
Duration between last execution and start is fixed
Use: when mandatory that previous execution is completed
<h3>FixedRate </h3>
```
@Schedule(fixedRate=1000)
public void scheduleFixedRateTask() {
}
```

Use: each execution of task is independent
Not invoked until previous one is done.

<h3>@Async</h3>
```
@EnableAsync
public class ScheduledFixedRateExample {
@Async
@Scheduled(fixedRate = 1000)
public void
}
```
Invoke even when previous task not done

<h2>Fixed Rate vs Fixed Delay</h2>
FixedDelay: n millisecond between finish time of execution of task and start time of next
execution of the task
FixedRate: runs every n millisecond
Could lead to Out of Memory exception

<h2>Schedule task with initial delay</h2>

@Schedule(fixedDelay=1000,initialDelay=1000)

<h2>Cron Expressions</h2>
`@Scheduled(cron = "0 15 10 15 * ?")`
cron expression to control schedule of task.
10:15 am on the 15th day of each month
`@Scheduled(cron = "0 15 10 15 *?", zone = "Europe/Paris")`

<h2>Parameterizing Schedule</h2>
Spring Expressions to externalize configuration of the tasks.
Store in properties files
`@Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds})`
`@Scheduled(fixedRateString="${fixedRate.in.milliseconds}")`
`@Scheduled(cron = "${cron.expression})"`
<h2>Configuring Using XML</h2>
```
<!-- Configure the schedule-->
<task:scheduler id ="myScheduler" pool-size="10">
<!-- Configure parameters-->
<task:scheduler ref ="beanA" method="methodA"
`fixed-delay=5000` initial-delay="1000" />
<task:scheduled ref = "beanC" method="methodB" fixed-rate="5000">
<task:scheduled ref ="beanD" method = "methodD"
    cron="*/5 * * * * MON-FRI"/>
>
</task:scheduled-tasks>
```
<h2>Using Spring SchedulingConfigurer</h2>
```
@Configuration
@EnableScheduling
public class DynamicSchedulingConfig implements SchedulingConfigurer {
    @Autowired
    privateTickService tickService;
    @Bean
    public Executor taskExecutor(){
        return Executors.newSingleThreadScheduledExecutor();
    }
    @Override
    public void configureTasks(ScheduledTaskRegistrat taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor));
        taskRegistrar.addTriggerTask(
            new Runnable() {
                @Override
                public void run() {
                    tickService.tick();
                }
            },
            new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext context) {
                Optional<Date> lastCompletionTime = Optional.OfNullable(context.lastCompletionTime());
                Instant nextExecutionTime = lastCompletionTime.orElseGet(Date::new).toInstant()
                .plusMillis(tickService.getDelay());
                return Date.from(nextExecutionTime);
            }
        )
    }
}
```

<h2>Running Task in Parallel</h2>
```
@Bean
public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize();
    threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
    return threadPoolTaskScheduler;
    
}
