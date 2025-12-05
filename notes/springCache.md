<h1> Spring Cache</h1>

<h2> Using</h2>

caching abstraction using spring-boot-starter-cache
spring-context-support
    - provides more CacheManager
    - EhCache
Spring-context module

<h3>@Enable Caching</h3>

- @EnableCaching
  - register ConcurrentMapCacheManager
  - customize using new CacheManagerCustomizer<T> beans
  - Cache autoConfiguration picks up customizers and applies to cache manager before complete initializers
  - 
    - ```
      @Component
      public class SimpleCacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

      @Override
      public void customize(ConcurrentMapCacheManager cacheManager) {
          cacheManager.setCacheNames(asList("users", "transactions"));
      }
      }
      ```
      - ```
        @Component
        public class SimpleCacheCustomizer  
        implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

        @Override
        public void customize(ConcurrentMapCacheManager cacheManager) {
            cacheManager.setCacheNames(asList("users", "transactions"));
        }
        }
    
        ```
        <h3> Using Caching with Annotations</h3>

      - register cacheManager
      - bind caching behaviour to methods
      - @Cachable
        - demarcate with `@Cacheable`
          - parameterize with name of the cache
            - ```
              @Cacheable("addresses")
              public String getAddress(Customer customer){}
              ```
              will check the cache addresses before invoking method and cache results
              - any cache contains required result, return result and method not invoked
      - @CacheEvict
        - don't want to populate cache with values C
        - Cache grow quite large and fast
        - hold out to stale or unused data
        - ```
          @CacheEvict(value = "addresses", allEntries=true
          public String getAddresses(Customer customer){
          })
          ```
          - clear all entries in cache addresses and prepare for new data
          - CachePut
            - update the content of the cache without interfering with method execution
            - ```
              @CachePut(value = "addresses")
              public String getAddress(Customer customer){}
              ```
            Difference between @Cacheable and @CachePut
          - Cacheable skip running method
          - @CachePut run method and put results in cache
          - @Caching
          - ```
            @Caching(evict = { 
            @CacheEvict("addresses"),
            @CacheEvict(value="directory", key="#customer.name") })
            public String getAddress(Customer customer) {...}
            ```
            Group multiple caching annotations with @Caching for customized caching logic
          - CacheConfig
            - streamline cache configuration into single place at class level
            - ```
              @CacheConfig("addresses")
              public class CustomerDataService {

              @Cacheable
              public String getAddress(Customer customer) {...}
              }
              ```
          - Conditional Caching
            - Condition Parameter
              - parameterised @CachePut with condition parameter that takes a SPel expression and ensures esults are cached
                - ```
                  @CachePut(value="addresses", condition="#customer.name=="'Tom'")
                  public String getAddresses(Customer customer) {}
                  ```
            - Unless Parameter      
              Control caching based on output of method rather than input
            - ```
              @CachePut(value="addresses",unless="#result.length()<64")
              public String getAddress(Customer customer){}
              ```
            - cache addresses unless shorter than 64 characters
            - effective for manage large results
            - customize instead of enforcing generic behaviour
        - Java-Based Caching
          - ```
            @Configuration
            @EnableCaching
            public class CachingConfig {
            @Bean
            public CacheManager cacheManager() {
                SimpleCacheManager cacheManager = new SimpleCacheManager();
                cacheManager.setCaches(Arrays.asList( new ConcurrentMapCache("directory"), new ConcurrentMapCache("addresses")))
                return cacheManager;
                }
            }
            
            @Component
            public class CustomerDataService {
                @Cacheable(value="addresses",key="#customer.name)
                public String getAddress(Customer customer) {
                    return customer.getAddress()
                }
            
            ```
