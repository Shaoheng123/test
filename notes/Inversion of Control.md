<b>Inversion of Control<b>
Transfer control to a framework
control for progam and make calls to custom code

Framework use abstract with additional behaviour

Benefits:
decouple execution of task from implementation to switch
greater modularity
easier to test by isolatingg and testing dependencies, communicate through contracts

How to achieve Inversion of Control
Strategy Design Pattern, Service Location pattern, Factory pattern and Dependency Injection

<b>Dependency Injection<b>

injection is done by assembler
```
public Store(Item item) {
    this.item = item;
}
```

<br><h4>Spring IoC Container:<h4>
managing objects of an application
use dependency injection to achieve inversion of control
represented by BeanFactory and ApplicationContext
Bean Factory: root interface for accessing Spring container for managing beans.

ApplicationContext(sub-interface of BeanFactory) 

Features: resolving message, supporting internationalization, publish event, application-layer specific contexts
<br><h3> Spring bean</h3>

instantiating, configuring object known as beans and managing life cycle
AnnotationConfigApplicationContext
FileSystemXMLApplicationContext
WebApplicationContext

assemble beans: configuration metadata: annotations.
<h4>Spring Bean<h4>
Object that Spring instantiates, assemble and manages
Beans 
service layer objects
DAO
presentation objects
infrastructure objects(HibernateSessionFactories)
JMS Queues

Define Java class: Spring bean
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
}

<br><h4><b>Configuring Beans in Container<b><h4>

manage beans provided by application to ApplicationContext
Spring bean configuration bean definitions
<br> <h5>Java based Configuration<h5>
Spring 3.0
Configuration uses @Bean-annotated within @Configuration
@Bean: method creates Spring bean
@Configuration: annotation indicates Spring bean configurations
Defining Coniguration class
```
@Configuration
public class config {
//Inversion of Control
    @Bean
    public AccountService accountService() {
    return new AccountService(accountRepository)
    }
}

```
<br><h5>Annotation-Based Configuration:<b>
Enable Annotation-based configuration via XML configuration
Annotations on Java classes, methods,constructor 
@Controller
@Service
@Repository
@Autowired
@Qualifier

XML configuratoin
`user-bean-config.xml`

```<?xml version="1.0" encoding="UTF-8"?>
<context:annotation-config/>
<context:component-scan> base-package="com.baeldung.applicationcontext"/>
```
annotation-config tag: enables annotation=based mapping
Component-scan tag: where to look for annotated classes

<br><h3> ApplicationContext</h3>
ApplicationContext containers that implements ApplicationContext interface
AnnotationConfigApplicationContext(Spring 3)
@Configuration
@Component
JSR-330 metadata as input
```
ApplicationContext context = new AnnotationConfigApplicationContext(AccountConfig.class);
AccountService accountService = context.getBean(AccountService.class);
```

AnnotationConfigWebApplicationContext:(web based)

configure Spring's ContextLoaderListener servlet listener or Spring MVC DispatcherSevlet in web.xml file
```
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AccountConfig.class);
        context.setServletContext(container);
    }
}
```
XMLWebApplicationCOntext:
set config location
then setSevletContext
```
public class MyXmlWebApplicationInitializer implements WebApplicationInitializer
{
    public void onStartup(ServletContext container) throws ServletException {
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation("/WEB-INF/spring/applicationContext.xml");
        context.setServletContext(container)
    }
}
```
FileSystemXMLApplicationCOntext:

XML-based Spring configuration file from filesystem or url
load the applicationContext programmtically.
standalone application or test harnesses(automate testing of operation system components)
```
ApplicationContext context = new FileSystemXMLApplicationCOntext(path);
AccountService accountService = context.getBean("accountService",AccountService.class)
```

ClassPathXmlApplicationContext:
load XML configuration from classPath(text harnesses)
application contexts embedded within jars

ApplicationContext context = new ClassPathXMLApplicationCOntext("applicationcontext/account-bean-config.xml");
AccountService accountService = context.getBean("accountService,AccountService.class");

Multiple Application Context
Modular Applications: each module loads its own ApplicationCOntext, isolate configurations of ach module and prevent bean naming conflicts for ease of maintainance

```
ApplicationContext module1Context = new AnnotationConfigApplicationContext(Module1Config.class);
```
<b><h4> Hierarchical Application Context</h4>
parent context define beans that are available to all child context
``
ApplicationContext parentContext = new AnnotationConfigAppkicationContext(ParentConfig.class);
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
context.setParent(parentContext);
context.register(config.class);
context.refresh();
``
load parent Context, link to parentContext using setParnet()
Beans in ParentConfig available to childContext

<br><h4>Isolation(Testing)<h4>
Different ApplicationContext to simulate different parts of application during testing
``
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Module1Config.class})
public class TestClass1 {
    @Autowired
    ApplicationContext context1;
}
``

<br><h4> Message Resolution</h4>

MessageSource interface
ResourceBundleMessageSource most common implementation of MessageSource
uses JDK ResourceBundle implementation
messages.properties on classpath

account.name = TestAccount

 
StaticMessageSource
Manually Instantiate container:

ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")
Scan for @Bean annotation 
Initialize and manage bean define din class, set up dependencies and manage lifecycle
Container read metadata and use it to assemble beans at runtime
