<h1> Enable Spring retry</h1>

<h2> enabling</h2>

```
@Configuration
@EnableRetry
public class AppConfig
```

<h2> Usage</h2> 

```
@Service
public interface MyService{

    @Retryable
    void retryService(String sql);
    }
```

- ExhaustedRetryException thrown if max retries and have exception
- Default
  - retry 3 times with delay of 1 second between retries
<h3> Retryable and Recover</h2>
  -
  - ```
    @Service
    public interface MyService {

    @Retryable(retryFor = SQLException.class)
    void retryServiceWithRecovery(String sql) throws SQLException;

    @Recover
    void recover(SQLException e, String sql);
    } 
    ```
    
- Retry attempted when SQLException is thrown
- @Recover defines recovery method when retryable fails with specified exception
- After retrying for 3 times (SQLException) call recover method
- recovery handler
  - first parameter: type Throwable and same return type
  - arguments populated from argument list of failed method in the same order

<h2> Customizing @Retryable behaviour</h2>

- using parameter maxAttempts and backoff
- ```
  @Service
  public interface MyService {

  @Retryable(retryFor = SQLException.class, maxAttempts = 2, backoff = @Backoff(delay = 100))
  void retryServiceWithCustomization(String sql) throws SQLException;
  }
  
  ```
  
- 2 attempts and delay of 100 milliseconds

<h3> Spring properties </h3>

- use properties in @Retryable annotation
- Externalizing values of delay and max attempts
<h3> Define properties in a file called retryConfig.properties
- ```
  retry.maxAttempts=2
  retry.maxDelay=100
  ```
  
<h3>Loading Application</h3>

- ```
  @PropertySource("classpath:retryConfig.properties")
  public class AppConfig{...}  
  ```

<h3> Injecting values of retry.maxAttempt and maxDelay</h3>

- ```
  @Service
  public interface MyService {
  @Retryable(retryFor = "SQLException.class", maxAttemptsExpression = "${retry.maxAttempts}",
        backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
        void retryServiceWithExternalConfiguration(String sql) throws SQLException
  }
  ```
  
  - use maxAttemptsExpression and delayExpression instead of maxAttempts and delay
  <h3> Logging Retry Count </h3>

- ```
  @Override
  public void retryService() {
    logger.info("Retry Number" + RetrySynchronizationManager.getContent().getRetryCount());
  }
  ```

<h2> RetryOperations</h2>
Spring RetryOperations

- ```
    public interface RetryOperation {
        <T> T execute(RetryCallback<T> retryCallback) throws Exception;
    }
  ```
  
RetryCallback
    - which is parameter of execute() is interface that allows insertion of business logic that needs to be upon failure
    - ```
        public interface RetryCallable<T> {
            T doWithRetry(RetryContext context) throws Throwable;
        }    
        ```     
<h2> RetryPolicyBuilder API and factory Method</h2>

- implementations of RetryOperations
- simplify creation of RetryPolicy and BackoffPolicy by introducing factory methods and Builder API
- configuration clearer and more fluent
- RetryPolicy determines when operation should be retried
- SimplyRetryPolicy retry fixed number of times
- use static factory method or builder to configure maximum number of attempts
- if 0, execute once and no retries
- Backoff
  - backoff between retry attempts
- FixedBackoffPolicy
  - pauses fixed amount of time before continuing
<h3> RetryTemplate Configuration</h3>
  - configure RetryTemplate bean in @Configuration class using new Builder API for SimpleRetryPolicy
  - ```
    @Configuration
    public class AppConfig {
        @Bean
        public RetryTemplate retryTemplate = new Retrytemplate();
          FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
          fixedBackOffPolicy.setBackOffPolicy(2000l);
          SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy.builder
            .maxAttempts(2)
            .build();
          retryTemplate.setRetryPolicy(retryPolicy);
          return retryPolicy();
    }
        @Bean
        public RetryTemplate = new RetryTemplate(); 
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackoffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(100L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy)
        SimpleRetryPolicy retryPolicy = SimpleRetryPolicy.builder()
        .maxAttempt().build()  
    ```

<h2> RetryTemplate</h2>

```
retryTemplate.execute(new RetryCallable<Void,  RunTimeException>)

    @Override
    public Void doWithRetry(RetryContext arg0) {
        myService.templateRetryService();
    }
```

```
retryTemplate.execute(arg0 ->{service.templateRetryService})
```
<h2>Listener</h2>

- Listeners provide additional callBacks upon retries. utilize for cross-cutting concerns across different retry attempts
<h3> Adding Callbacks</h3>
  - ```
    public class DefaultListenerSupport extends RetryListenerSupport {
      @Override
      public <T,E extends Throwable> void close(RetryContext context, Retrycallback<T,E> callback, Throwable throwable){
      logger.info("OnClose");
      super.close(context,callback,throwable );
    } 
    @Override
    public <T,E extends Throwable> void onError(RetryContext context, RetryCallback<T,E>, Throwable throwable) {
        logger.info("onError");
        super.onError(context,callback,throwable);
    }
    @Override
    public <T,E extends Throwable> boolean open(RetryContext context,RetryCallback<T,E> callback) {
        logger.info("OnOpen);
        return super.open(context,callBack);
    }
    }
    ```
  - open and close callbacks come before and after entire retry
  - onError apply to individual RetryCallBack calls

<h2> Register Listener</h2>

```
@Configuration
public class AppConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        ...
        retryTemplate.registerListener(new DefaultListenerSupport());
        return retryTemplate;
    }
}
```

Testing
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  classes = AppConfig.class,
  loader = AnnotationConfigContextLoader.class)
public class SpringRetryIntegrationTest {

    @Autowired
    private MyService myService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Test(expected = RuntimeException.class)
    public void givenTemplateRetryService_whenCallWithException_thenRetry() {
        retryTemplate.execute(arg0 -> {
            myService.templateRetryService();
            return null;
        });
    }
}
```