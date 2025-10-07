Object that is initialized, managed by Spring IOC container

IOC: define dependencies without creating them.

<h3>Domain Class</h3>
```public class Company {
    private Address address;

    public Company(Address address) {
        this.address = address;
    }

    // getter, setter and other properties
}
```
Provide container with appropriate metadata

@Bean Configuration:
`
@Component
public class Company {}
`
<h2>Configuration class</h2>
`
@Configuration
@ComponentScan(basePackageClasses = Company.class)
public class Config {
    @Bean
    public Address getAddress(){
        return 
    }
}
`
@ComponentScan in config tells container to look for beans in package containing the Company class

All objects are managed by Spring

AnnotationConfigApplicationContext required to build up a container
ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

<h3>Bean Scope</h3>

<h3><b>Scopes:</b></h3>
singleton
prototype
<b>Web:</b>
request
session
application
websocket

Singleton:
all requests will return the same cached object
Any changes reflected in all references to the bean
<b>Default</b> value for scope
`@Bean
@Scope("singleton")
public Person personSingleton() {
return new Person();
}
`
Both objects referencing same bean instance even if one changes state

<h3>Prototype</h3>
Different instance each time it is requested from the container

<h3> Request</h3>

`
@Bean
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequestScope
`
Bean instance for a Http Request
proxyMode: No active request at the moment of instantiation of web request
Spring creates and instantiate targetbean when it is needed in a request

<h3> Session</h3>
Bean instance for a Http Session
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SessionScope
However, once it is changed, that value is retained for subsequent requests as the same instance of the bean is returned for the entire session.

<h3>ApplicationScope:<h3>
bean cycle for lifecycle of a Servlet Context
same instance of bean shared among multiple servlet based in the same ServletContext
Singleton scoped to a single application context

`
@Bean
@Scope(
value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Resource(name = "applicationScopedBean")
`
Websocket

`
@Bean
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
`
WebSocket scoped beans are stored in the WebSocket session attributes. 
The same instance of the bean is then returned whenever that bean is accessed during the entire WebSocket session.

<h2>Spring Bean annotations<h2>

<h3> @ComponentScan</h3>
Automatically scans packages
configures which packages to scan
specify base package or value

`
@Configuration
@ComponentScan(basePackage = VehicleFactoryConfig)
@ComponentScan(basePackageClasses=VehicleConfig.class)
class VehicleFactoryConfig{}
<context:component-scan base-package="com.baeldung" />
`

@Component
Spring automatically detects classes associated with @Component
bean instance same name but lowercase initial

@Repository
@Service
@Configuration
@Controller
meta annotations of @Component

@Repository
database access layer in application
`
@Repository
class VehicleRepository
`
automatic persistance exception translationenabled
native Hibernate exception translated to subclass of DataAccessException

`
@Bean
public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    return new PersistenceExceptionTranslationPostProcessor();
}
`
convert to subclass of DataAccessExceptionbean class=
"org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>

@Service(business logic)
@Controller(which class serve as controller)

@Configuration:
bean definition methods

AOP:

`
@Aspect
@Component
public class Class
@Pointcut("within(@org.springframework.stereotype.Repository *)" )
public void method(){}
@Around("method"")
`
<h3>@Pointcut</h3>
matches all methods in the class annotated with repository
<h3>@Around("method()"):</h3>
intercept the method call before and after execution
modify input parameters or return value
Handle exceptions or add logic such as logging and security or performance monitoring
Caching:

```
@Aspect
@Component
public class CachingAspect {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Around("execution(* com.example.service.MyService.getData(..))")
    public Object cacheAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = generateKey(joinPoint);

        if (cache.containsKey(key)) {
            System.out.println("Returning cached result for: " + key);
            return cache.get(key);
        }

        Object result = joinPoint.proceed(); // Execute the method
        cache.put(key, result);              // Store result in cache
        System.out.println("Caching result for: " + key);

        return result;
    }

    private String generateKey(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().toShortString() + Arrays.toString(joinPoint.getArgs());
    }
}
```











