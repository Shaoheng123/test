<h1> Spring Events </h1>
- create and public events that are synchronous
listener able t participate in publisher's transaction context
- publishing an event 
- handling in a listener
- enable asynchronous processing of events

Application Event
```
public class CustomSpringEvent extends ApplicationEvent {
    private String message;
    public CustomSpringEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
```
<h2>Publisher </h2>

- inject ApplicationEventPublisher and use publishEvent() API
```
@Component
public class CustomSpringEventPublisher{
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    public void publishCustomEvent(final String message) {
        CustomSprintEvent customSpringEvent = new CustomSpringEvent(this,message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    ]
}
```

- Listener
  - ```
    @Component
    public class CustomSpringEventListener implements ApplicationListener<CustomSpringEvent> {
        @Override
        public void onApplicationEvent(CustomSpringEvent event) {
            
        }
    }
    ```
    
parameterized with generic type of custom event, onApplicationEvent() type-safe
- Spring Events are synchronous
- DoStuffAndPublishAnEvent Method blocks until all listeners finish processing an event

<h3> Asynchronous events </h3>

<h4> Using Application Event Multitasker</h4>

- creating ApplicationEventMultiTasker bean with executor
  - SimpleAsyncTaskExecutor
    - ```
      @Configuration
      public class AsynchronousSpringEventsConfig {
        @Bean(name = "applicationEventMulticaster")
        public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
          SimpleApplicationEventMulticaster eventMulticaster =
            new SimpleApplicationEventMulticaster();

          eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
          return eventMulticaster;
          }
      }
      ```
  - Listener deal with event in a different thread
  - event operate asynchronously and not others

  <h3> Using @Async</h3>
  Identify and annotate indivudal listeners that should process events asynchronously

- ```
  @EventListener
  @Async
  public void handleAsyncEvent(CustomSpringEvent event) {
  
  }
  ```
- process event in separate thread
- use value attribute of @Async annotation to indicate that executor other than default should be used
- ```
  @Async("nonDefaultExcecutor")
  void handleAsyncEvent(CustomSpringEvent event) 
  ```
  - Add @EnableAsync to @Configuration or @SpringBootApplication
  - ```
    @Configuration
    @EnableAsync
    public class AppConfig
    ```
    -@EnableAsync allow Spring to run @Async methods in background thread pool
  - customizes used Executor
  - Spring searches for associated thread pool definition
  - unique TaskExecutor bean
  - Executor bean named taskExecutor
  - SimpleAsyncTaskExecutor will be used to invoke event listener asynchronously

<h3> Existing Framework Events</h3>

- Spring itself publishes variety of events out of the box
- ApplicationContext fire various framework including
  - ContextRefreshedEvent
  - ContextStartedEvent
  - RequestHandledEvent
- hook into life cycle of application and context and add in custom logic
- ```
  public class ContextRefreshedOListener implements ApplicationListener<ContextRefreshedEvent> {
  @Override
  public void onApplicationEvent(ContextRefreshedEvent cse){}
  }
  ```

<h3> Annotation Driven Event Listener </h3>

- registered on any public method of a managed bean via @EventListener annotation
- ```
  @Component
  public class AnnotationDrivenEventListener {
    @EventListener
  public void handleContextStart(ContextStartedEvent cse) {}
  }
  ```
  -method signatire declares event type consumed
Listener invoked syncrhonously in application
- EnableAsync support  in application

<h3> Generic</h3>

- dispatch events with genetics information in event type
- Generic Application Event
- ```
  public class GeneticSpringEvent<T> {
    private T what;
  protected boolean success;
  
  public GenericSpringEvent(T what, boolean success) {
  public GenericSpringEvent(T what, boolean success) {
    this.what = what;
    this.success = success;
  }
    }
  }
  ```
  - Diff between GenericSpringEvent and CustomSpringEvent
  - Can publish any arbitrary event and not required to extend from ApplicationEvent anymore

<h3> Listener</h3>

- implement ApplicationListener interface
- ```
  @Component
  public class GVenericSpringEventListener implements ApplicationListener<GenericSpringEvent<String> {
    @Oveeride
  public void onApplicationEvent(@NonNull GenericSpringZEvent<String> event) {
  }
  }
  ```
  - inherit GenericSpringEvent from ApplicationEvent class
  - annotation-driven eventlistener
  - conditional by defining boolean SpEL expression on @EventListener annotation
  - invoked only for successful GenericSpringEvent of String
  - ```
    @COmponent
    public class AnnotationDrivenEventListener {
    @EventListener(condition = "event.success")
    public void handleSuccessful(GenericSpringEvent<String> event) {}
    }
    ```
    <h3> Publisher</h3>
  - publish event that resolves generrics parameter to filter on
  - `class GenericStringSpringEvent extends GenericSpringEvent<String>`
  - alternative way of publishing event
  - if return non-null value from method annotated with @eEventListener as the result
  - Springframework send result as new event
  - publish multiple new events by returning them in a collection as the result of event processing

<h4> Transaction-Bound Events</h4>

TransactionalEventListener
- extends @EventListener
- bind listener of an event to a phase of the execution
  - after_commit
  - after_rollback
  - after_completion
  - before_commit
- invoked when CustomSpringEvent publish after transaction completed
- register transaction callback via TransactionSynchronizationManager
  - handle event at a specified transaction phase   
  - executed in same thread that publishes event
    - multicast not used
    - use Async to make transactionL
    - ```
      @Async
      @TransactionalEventListener
      void handleCustom(CustomSpringEvent event) {
      }
      ```
- handleCustom method will run in separate thread asynchronously when original transaction has completed sucessfully
- Spring binds transactions to the curent thread
  - listener runs in separate thread cannot access the original transactional context
  - should not use @Async @TransactionalEventListener if event handler relies on transaction's context such as lazy-loaded entities
  - shared database state
  - transactional rollback logic