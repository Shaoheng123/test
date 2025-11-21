<h2> ApplicationContext Interface</h2>

IoC container manages objects of an application using dependency injection

IOC container:
- BeanFactory
- ApplicationCOntext

ApplicationContext is sub-interface of BeanFactory

Enterprise-specific functionalities
ApplicationCOntext
- resolve messages
- internationalization
- publish events
- application-layer specific contexts.
Default Spring container

Bean is an object that Spring container onstantiates, assembles and manages

Define beans for service layer objects
- data access objects
- presentation objects
- infrastructure objects such as Hibernate SessionFactories
- JMS Queues

DAOs and business logic to create and load domain objects

```
public class AccountService {

@Autowired
private AccountRepository accountRepository;
}
```
<h2> Configuring Beans in the Container</h2>

Application must provide bean configuration to ApplicationContext container
A Spring ean configuration consists of one or more bean definitions

- Spring supports difference ways of configuring beans
1) Java-Based Configuration
2) Annotation-Based Configuration
3) XML-Based Configuration

<h3> Java-Based Configuration</h3>

- Preferred way of bean configuration
- @Bean annotated methods within @Configuration class
- method creates a Spring bean
- @Configuration indicates it contains Spring Bean configurations

<h3> Create COnfiguration class to define AccountService as Spring bean</h3>
Spring 3

```
@Configuration
public class AccountConfig {
    @Bean
    public AccountService accountService() {
        return new AccountService(accountRepository());
    }
    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository();   
    }
}
```
<h3> Annotation-Based Configuration</h3>

Spring 2.5 enable bean configuration
first enable annotation-based configuration via xml configuration
set of annotation  on Java classes, methods, constructor or fields to configure beans

- Component
- Controller
- Service
- Repository
- Autowired
- Qualifier
    
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">
  
  <context:annotation-config/>
  <context:component-scan base-package="com.baeldung.applicationcontext"/>

</beans>
```
annotation-config tag enables annotation-based mapping
component-scan tag tells Spring where to look for annotation classes
Create UserService class and define as Spring bean using @Component annotation

```
@Component
public class UserService {
}
```

```
ApplicationContext context = new ClassPathXmlApplicationContext("applicationcontext/user-bean-config.xml");
UserService userService = context.getBean(UserService.class);
assertNotNull(userService);
```

<h3> XML-Based Configuration</h3>

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="accountService" class="com.baeldung.applicationcontext.AccountService">
    <constructor-arg name="accountRepository" ref="accountRepository" />
  </bean>

  <bean id="accountRepository" class="com.baeldung.applicationcontext.AccountRepository" />
</beans>
```

<h2> Types of ApplicationContext</h2>

<h3> AnnotationConfigApplicationContext</h3>
Spring 3.0

- Take classes annotated with @Configuration, @Component and JSR-330 metadata as input
```
ApplicationContext application context = new AnnotationConfigApplicationContext(AccountConfig.class);
AccountService accountService = context.getBean(AccountService.class);
```

<h3> AnnotationCOnfigWebApplicationContext</h3>

- web based variant of AnnotationConfigApplicationContext 
Use when configure Spring's ContextLoaderListener servlet listener or Spring MVC DispatcherServlet in web.xml file
- implement WebApplicationInitializer interface

```
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotaitonCOnfigWebApplicationCOntext();
        context.register(AccountConfig.class);
        context.setServletContext(container);
    }
}
```

<h3> XmlWebApplicationContext</h3>

XML based configuration in web application, use XmlWebApplicationContext class
Configuring this container is like AnnotationConfigWebApplicationContext configure in web.xml
implement WebApplicationInitializer interface


```
public class MyXmlWebApplicationInitializer implements WebApplicationInitializer {

  public void onStartup(ServletContext container) throws ServletException {
    XmlWebApplicationContext context = new XmlWebApplicationContext();
    context.setConfigLocation("/WEB-INF/spring/applicationContext.xml");
    context.setServletContext(container);

    // Servlet configuration
  }
}
```

<h3> FileSystemXmlApplicationContext</h3>

- load XML-based Spring configuration file from file system or From URLs
- useful when load ApplicationContext programmatically

<h3>Usage</h3>

- test harnesses
- standalone applications

```
String path  = "C:/account-config-bean.xml";

ApplicationContext context = new FileSystemXmlApplicationContext(path);
AccountService accountService = context.getBean("accountService",AccountService.class);

```

<h3> ClassPathXmlApplicationContext</h3>

- load XML configuration file from classpath
- test harnesses
- application contexts embedded within JARs

```
ApplicationContext context = new ClassPathXmlApplicationContext("applicationcontext/account-bean-config.xml");
AccountService accountService = context.getBean("accountService", AccountService.class);

```

<h2> Multiple ApplicationContext in Spring</h2>

Multiple ApplicationCOntext instances are needed within a single application

<h3> Modular Applications</h3>

Each module might have its own context
- isolate configurations of module
- prevent bean naming conflicts and making it easier to maintain

For each modular application, each module can load its own ApplicationContext
```
ApplicationContext module1Config = new AnnotationConfigApplicationContext(Module1Config.class);
ApplicationContext module2Config = new AnnotationConfigApplicationContext(Module2Config.class);
  
```

<h3> Hierarchical Application Context </h3>

- Parent context can define beans that are available to all child contexts
- Child can have specific 
- shared core configuration in the parent context
- ```
  ApplicationContext parentContent = new AnnotationConfigApplicationCOntext(ParentConfig);
  
  AnnotationConfigApplicationCOnfig childContext = new AnnotationConfigApplicationContext();
  childContext.setParent(parentCOntext);
  childCOntext.register(ChildCOnfig.class);
  childContext.refresh();
  ```
  
- Create and Link using setParent
- Beans defined in ParentConfig will be available in childContent

<h3> Isolation for Testing</h3>

- Different ApplicationContext instance to simulate different parts of application for testing
- Create specific contexts for each test scenario for unit and integration testing

```
@RunWith(SpringJUnit4ClassRunner.class)
@COntextConfiguration(classes = {Module1Config.class})
public class TestClass {
    @Autowired
    ApplicationContext context1;
}

@RunWith(SpringJunit4ClassRunner.class)
@ContextConfiguration(classes = {Module2Config.class})
public class TestClass2{
    @Autowired
    ApplicationContext context2
}

```
<h3> Additional Feature of ApplicationContext, create and use different types of ApplicationContext</h3>

<h4> Message Resolution</h4>

tion by extending MessageSource interface
  - ResourceBundleMessage- support message resolution and internationalizaSource
  - StaticMessageSource
  
StaticMessageSource programmtically add messages to source
- support basic internationalization
- suitable for testing
ResourceBundleMessageSource
- underlying JDK ResourceBundle implementation
- JDK MessageFormat
- using MessageSource to read message
messages.properties on classpath
- account.name = TestAccount
- ```
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("config/message");
    return messageSource;
  }
  ```

Inject MessageSource in AccountSource
- 
    ```
    @Autowired
    private MessageSource messageSource
    ```

- `messageSource.getMessage("account.name", null, Locale.English);`

- ReloadableResourceBundleMessageSource
  - reading files from any Spring resource location
  - support hot reloading of bundle property files

<h3> Event Handling</h3>

- ApplicationListener Interface
- ContextStartedEvent
- ContextStoppedEvent
- ContextClosedEvent
- RequestHandledEvent
- supports custom events for business use case


