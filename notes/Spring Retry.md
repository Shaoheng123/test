<h1> Spring Retry</h1>

<h2>automatically reinvoke failed operation</h2>

- Transient errors like momentary network glitch
<h2>to Enable</h2>
  - ```
    @Configuration
    @EnableRetry
    public class AppConfig{..}
    ```
<h2> Using Spring Retry</h2>

<h3>Retryable without Recovery</h3>
- ```
  @Service
  public interface MyService
  
  @Retryable
  void retryService(String sql);
  ```
  
- Retry enabled for all exceptions
- ExhaustedRetryException thrown after max reached

<h3> @Retryable and  @Recover</h3>

- ```
  @Service
  public interface MyService {
    @Retryable(retryFor = SQLException.class)
    void retryableServiceWithRecovery(String sql) throws SQLException;
  
    @Recover
    void recover(SqlException e, String sql);
  }
  ```
  
- retry attempted when SQL Exception thrown
- @Recover annotation defines separate recovery method when @Retryable fails with specified Exception
- Consequently, if retryServiceWithRecovery throws SQLException after 3 attempts, recover() method will be called
- recovery handler should have first parameter of type Throwable optional and the same return type
- arguments populated from argument list of failed method in the same order

<h3> Customizing @Retryable behaviour</h3>

- ```
  @Service 
  public interface Service{
    @Retryable(retryFor = SQLException.class, maxAttempts =2 , backoff = @Backoff(delay = 100))
    void retryServiceWithCustomization(String sql ) throws SQLException;
  }  
  ````

<h3> Using Spring Properties</h3>

- externalize values of delay and max attempts into a prperties file.
- retryConfig.properties
  - ```
    retry.maxAttempts =2
    retry.maxDelay = 100
    ```
  -  ```
     @PropertySource("classPath:retryConfig.properties")
     public class AppConfig {}
     ```
     <h4> Injecting</h4>
     