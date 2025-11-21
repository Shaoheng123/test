@Component vs @Repository and @Service

Data Access, Presentation, service, business

<h2>Spring Annotations</h2>

- classpath scanning annotations
- register each bean in ApplicationContext
- @Component
- @Service
- @Repository

<h2> Component</h2>
Spring only register beans with @Component
Register in ApplicationContext bc annotated with @Component

<h2>@Repository</h2>
catch persistence specific exception and rethrow as unified uncheck exceptions
`<bean class = "org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>`

<h2>Service</h2> 
business logic
 
<h2>Registering Properties File</h2>

```
@Configuration
@PropertySource("classpath:foo.properties")
@PropertySource("classpath:bar.properties")
```
Declare Multiple property location
```
@Configuration
@PropertySources({
    @PropertySource("classpath:foo.properties")
    @PropertySource("classpath:bar.properties")
})

```
`@PropertySource({classpath:persistence-${envTarget:mysql}).properties`
Using/Injecting Properties:/h2
<h2>Injecting with @Value</h2>
```
@Value("${jdbc.url}"")
private String jdbcUrl
```
Obtaining property using Environment API

@Autowired
private Environent env;
dataSource.setUrl(env.getProperty())

default:
`@Value("$jdbc.url:aDefaultUrl"jdbc.url");`
<h2>Best: Properties with SpringBoot</h2>

<h3> application.properties</h3>

place it in src/main/resources
inject any loaded properties from it
configure runtime using environment property
`java -jar app.jar --spring.config.location=classpath:/another-location.propertoes`
look for configuration files matching config/*/ directory pattern outside of our jar file.

`java -jar app.jar --spring.config.location=config/*/`

`baeldung.customProperty=defaultValue`

<h2>Environment Specific Properties</h2>
- application-environment.properties: src/main/resources
- Define staging profile
- Define application-staging.properties
This file will take precedence over the default property file

<h2> Test specific properties</h2>
Change to use these properties when application is under test

<h2> TestPropertySource annotation</h2>h2>
different ways of annotation for testing environment
`@(TestPropertySource("/foo.properties"))`
`@(TestPropertySource(properties = {"foo=bar"}))`
`@SpringBootTest(properties = {"foo= bar"})`

<h2>Hierarchical Properties</h2>

@ConfigurationProperties
database.url = jdbc:postgresql:/localhost:5432/instance
database.username=foo
database.password=bar

Mapping to database object
@ConfigurationProperties(prefix = "database")
public class Database {}
yaml file
```
database:
    url:jdbc:postgresql:/localhost:5432/instance
    username: foo
    password: bar
secret: foo
```
properties declared lower in file will overwrite higher declartion
<h2> Importing Additional Configuration Files</h2>
Adding files or directories
load from classpath or external directory
startup fail if file not found or optional file
import extensionless files
spring.config.import

```
spring.config.import= classpath:additional-application.properties,
    classpath:additional-application[.yml],
    optional:file:./external.properties,
    classpath:additional-application-properties 
```
Command Line:
`java -D property.name="value" -jar app.jar`

<h2>Environment Variables</h2>
export name=value
java -jar app.jar

<h2>PropertySourcesPlaceHolderConfigurer</h2>

```
@Bean
public static PropertySourcesPlaceHolderConfigurer properties() {
    PropertySourcesPlaceHolderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
    Resources[] resourcex = new ClassPathResource[ ]{ new ClassPathResource("foo.properties")};
    pspc.setLocation( resources );
    pspc.setIgnoreUnresolvablePlaceholders(true);
return pspc;

}
```
Parent-Child Contexts:

parent core functionality and beans
child: servlet-specific beans

Best way to define properties file and include in context and retrieve from Spring.
- @Value
- environment.getProperty

<h2>@Value:</h2>
injecting values into field in Spring Managed beans
applied at field/constructor/method parameter 

<h2>Set up:</h2>
Properties file
define @PropertySource in configuration class
```
value.from.file =
priority=high
listOfValue =A,B,C
```
Injecting from annotation to the field
```
@Value("string")
private String stringValue; 
```
Injecting from file to the field

```
@Value("$value.from.file}")
private String valueFromFile
```
Default Value:
```
@Value("${unknown.param":default}")
```
Precedence:
1. System
2. Properties File

`@Value("${listOfValues}")`

<h2>SpEL</h2>

`@Value("#{systemProperties{'priority''}})`

null value assigned if system property is null

`@Value{""#{systemProperties['unknown']?:'default'}"}`

field value from other beans
`@Value("#{otherBean.value})`

List of Values:
`@Value("#{'${listOfValues}'.split(',')")`

<h2>@Value with Maps</h2>

Properties file:
`valuesMap = {key1:'1',key2:'2',key3:'3'}`
<h3>Inject value from property file</h3>
`@Value("#{${valuesMap}})`
Get value of specific key in Map:
`@Value("#{${valuesMap}.keyOne}")`

Setting default value:
`@Value("#{${unknownMap:{key1:'1,key2:'2}}}")`
`@Value(""#{valuesMap}['key']"?:5)`

Map entries filtered before injection
`@Value("#{${valuesMap}.?{value>'1''}}")`

`@Value("#{systemProperties}")`

Constructor Injection:

Injecting priority to normal
```
@Component
@PropertySource("classpath:values.properties")
public class PriorityProvider {
    private String priority
    
    @Autowired
    public PriorityProvider{@Value("${priority:normal}" String priority) {
        this.priority = priority;
    }}
}
```
Setter Injection:

SpEL to inject list of values into setValue method
```
@Component
@PropertySource("classpath:values.properties")
public class CollectionProdivder {
    private List<String> values = new ArrayList<>();
    
    @Autowired
    public void setValues(@Value("#'${listOfValues}'.split(',')}" List<String> values))
    this.values.addAll(values);
}
```
<h2>Java 14: @Value with Records</h2>
```
@Component
@PropertySource("classpath:values.properties")
public record PriorityRecord(@Value("${priority:normal}")String priority) {}
```
Inject value directly into record's constructor

<h2> @PostConstruct and @PreDestroy</h2>

Attach actions to bean creation and destruction

<h2> @PostConstruct</h2>

Any access level, cannot be static
```
@Component
public class DbInit {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void postConstruct() {
        User admin = new User("admin", "admin password");
        User normalUser = new User("user", "user password");
        userRepository.save(admin, normalUser);
    }
}

```
 <h3>Usage</h3>
- populating database
- default users
- Initialize Repository before run @PostConstruct

<@h3> PreDestory</h3>

- Run only once when Spring removing bean from application context
- Any access level, cannot be static

<h3>Usage</h3>
@Component
```
public class UserRepository {

    private DbConnection dbConnection;
    @PreDestroy
    public void preDestroy() {
        dbConnection.close();
    }
}

```
<h2>Spring AOP</h2>
Increase Modularity by allowing separation of cross-cutting concerns

<h3>AOP Concepts</h3>

- Business Object
-  Aspect
- Joinpoint
- Pointcut
- Advice

Examples: 
- Logging
- Transaction Managment
- Security
- Exception Handling

<h2>Aspect</h2>

- Modularize convern that cuts across multiple classes.
- Unified logging

<h3>Definition</h3>
```
public class Aspect {
    Private Logger logger = LoggerFactory.getLogger(this.getClass);
    public void afterReturn(Object return) throws Throwable{
        logger.info(returnValue);
    }
}
```

<h2>@JoinPoint</h2>

- Point during execution of program , execution of method or exception handling
- Method execution

<h2> Pointcut</h2>

- Predicate that help match advice to be applied by an Aspect at a particular JoinPoint
- associate Advice with the Pointcut expression and it runs at any JoinPoint matched by Pointcut

<h2> @Advice</h2>

- interceptor
- Difficult types of advice include around, before and after
- interceptors maintain chain of interceptors around the joinpoint

<h2> Wiring Business Object and Aspect </h2>

```
<bean id="sampleAdder" class="org.baeldung.logger.SampleAdder" />
<bean id="doAfterReturningAspect"
class="org.baeldung.logger.AdderAfterReturnAspect" />
<aop:config>
<aop:aspect id="aspects" ref="doAfterReturningAspect">
<aop:pointcut id="pointCutAfterReturning" expression=
"execution(* org.baeldung.logger.SampleAdder+.*(..))"/>
<aop:after-returning method="afterReturn"
returning="returnValue" pointcut-ref="pointCutAfterReturning"/>
</aop:aspect>
</aop:config>

```

<h2>Configuration at a Glance</h2>

<h3>tag: aop:config</h3>
- Define class that represents an aspect
- Define aspect bean that was created

Define Pointcut using pointcut tag
apply an advice on any method within SimpleAdder class that accepts any number of arguments and returns any value type
Define which advice want to apply 
- apply the after-return advice
- defined in our Asoect by executing afterReturn that defined using attribute method
- advice within Aspect takes one parameter of type Object
- Take an action before and/or after target method call, log the return value

<h2> Spring Component Annotation</h2>
<h3> Spring ApplicationContext</h3>
Spring Application COntext:
Spring holds instances of objects that identified to be managed and distributed automatically
These are called beans
Spring uses Inversion of Control to collect beans from application and initialize at appropriate time

<h2> @Component</h2>

@Component is annotation hat allows Spring to detect custom bean automatically
- Spring scans application for classes annotated with @Component
- Instantiate them and inject any specified dependencies into them
- Inject when needed.

<h2> Spring stereotype Annotations</h2>

- @Controller
- @Service
- @Repository

Same function as @Component

composed annotations with @Component as a meta annotation for each of them
Specialized use and meaning of Spring auto-detection or dependency injection.

To test: `applicationContext.getBean(ControllerExample.class)`

<h2> @ComponentScan</h2>

- differentiate beans from other domain objects
- Spring uses @ComponentScan annotation to gather them into ApplicationContext
- SpringbootApplication includes @ComponentScan
- WHen @SpringbootApplication is at the root of the project will scan every component
  - configure @ComponentScan explicity if @SpringBootApplication  is not at the root of the project as long as it is on the classpath

Defined
```
package com.baeldung.component.scannedscope;

@Component
public class ScannedScopeExample {
}
```

Include via @ComponentScan annotation

```
package com.baeldung.component.inscope;

@SpringBootApplication
@ComponentScan({"com.baeldung.component.inscope", "com.baeldung.component.scannedscope"})
public class ComponentApplication {
    //public static void main(String[] args) {...}
}
```
<h2> When we cannot include</h2>

- No access to source code
- Conditionally use one bean implementation depending on environment can use @Bean

<h2> @Component vs @Bean</h2>

- @Bean is annotation that Spring uses to gather beans at runtime but not used at class level
- annotate methods with @Bean so Spring can store method's result in Spring bean
<h3>Differences</h3>

| Component     | Bean                                           |
|---------------|------------------------------------------------|
| Class- level  | method-level                                   |
| Auto-detected | Manual class instantiated                      |
|               | third part classes can be made to Spring beans |

