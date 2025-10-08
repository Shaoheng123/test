<h2>Purpose:</h2> 
Set object dependencies instead of creating dependencies in object itself

<h2>Benefits:</h2> 
-Loose Coupling by communicating using interface
- Easily Test without real dependencies
- Switch implementation easier


<h3>Traditional Dependencies:</h3>

```
public class Store {
    private Item item;
 
    public Store() {
        item = new ItemImpl1();    
    }
}
```

Annotation Based Configuration

```
@Configuration
@ComponentScan("com.baeldung.constructordi")
public class Config

@Bean
public Engine engine() {
    return new Engine(0,0);
}


```
Declare to perform context Scan for additional beans
```
@Component
public class Car {
    @Autowired
    public Car(Engine engine, Transmisison transmission) {
        this.engine = engine;
        this.transmission = transmission;
    }
}

```
Spring package scan Car class and initialize using Autowired annotated constructor

calling @Bean obtain instance of Engine

<h2>Bootstrapping Application Context</h2>

```
ApplicationContext context  = new AnnotationConfigApplicationContext(Config.class)
Car car = context.getBean(Car.class);
```

Instantiate implementation of Item interface within store class\
<h2>Dependency Injection</h2>
- Constructor Injection
- Field Injection
- Setter Injection

<h3>Constructor Injection</h3>

Pass required component into class during instantiation

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
 ```

<h3>Constructor injection is better than Field injection</h3>
Cannot initialize constructor state
Only through reflection APi which breaks encapsulation

Possibility of nullPointer where have Userservice but no UserRepository
Easier to build immutable objects.

Cons:
Too many dependencies.

<b>@Configuration<b> indicates that the class is a source of <b>bean definition<b>
Singleton scope, Spring checked if cached instance of the bean already exists
prototype scope: Spring creates new  bean instance for each method call/


XML: Configuration
```
<bean id="item1" class="org.baeldung.store.ItemImpl1" /> 
<bean id="store" class="org.baeldung.store.Store"> 
    <constructor-arg type="ItemImpl1" index="0" name="item" ref="item1" /> 
</bean>
```

<h3>Setter Based Dependency Injection</h3>

```
@Bean
public Store store() {
    Store store = new Store();
    store.setItem(item1());
    return store;
}

<bean id="store" class="org.baeldung.store.Store">
    <property name="item" ref="item1" />
</bean>
```

<h3>Recommendation:</h3> 

Constructor based injection
- Mandatory dependencies 

Setter based injection 
- Optional dependencies


<h2>Field Based Injection:</h2>

```
@Autowired
    private Item item; 
```
Disadvantages:

Reflection and violate SRP

<h2>Autowiring Dependencies</h2>

Default: explicity name the dependencies
- name: Spring look for a bean with same name as property to be set.
- type: Spring look for bean with same type of the property to be set.
- constructor: autowire based on constructor arguments

Match by name:
Autowiring by name, must use @ComponentScan with application context
```
@Configuration
@ComponentScan(basePackages="com.baeldung.dependency"))
```
search pacakge for classes annotated with @Component annotation

`@Component(value = "autowiredFieldDependency")`
Name must correspond with
```
@Autowired
private ArbitraryDependency autowiredFieldDependency
```
<h3>Setter Injection</h3>
Similar to Resource but annotate the setter

```
public class Store {
    @Autowired
    private Item item;
}
```

Using Qualifier Annotation:
```
public class Store {
    
    @Autowired
    @Qualifier("item1")
    private Item item;
}
```
Used to indicate which bean to inject when there are multiple canditates of the same type.
Different bean instance returned for each request.

XMLConfiguration:

Injecting through xml
- Type:
``<bean id="store" class="org.baeldung.store.Store" autowire="byType"> </bean>``
- Name: 
``
<bean id="store" class="org.baeldung.store.Store" autowire="byName">
``

Lazy Initialized Beans
`lazy-init="true"`
Initialized when first requested, not startup

Configuration Error discovery may be delayed


Spring Bean Autowiring:

```
@Configuration
@ComponentScan("com.baeldung.autowire.sample")
public class AppConfig {}
```
@SpringBootApplication
- @Configuration
- @EnableAutoConfiguration
- @ComponentScan

Automatically register in ApplicationContext scan current and sub packages
<h2>Inject Beans using AutoWired</h2>

Autowire can be used on Field, Setter and Constructor

<h2>Autowired and Optional Depedencies</h2>
<b>
`NoSuchBeanDefinitionException`
<br>For bean to be constructed
```
public class FooService {
    @Autowired(required = false)
    private FooDAO dataAccessor;
}
```
<h2> Autowire Disambiguation</h2>

- Autowiring by @Qualifier 
  - 2 concrete implementation of a single class
    - `NoUniqueBeanDefinitionException`
      ```
      @Component("fooFormatter")
        public class FooFormatter implements Formatter {
            public String format() {
                return "foo";
            }
      }
      
     ```
  - match with name declared in @Component annotation`@Component("fooFormatter")`
  
<h2>Wiring</h2>
    - @Autowired
    - @Resource
    - @Inject

Resolve dependencies with field and setter injection

<h3>@Resource</h3>
Priority Order
 - Name
 - Type
 - Qualifer
<h2>Exception</h2>
   `NoSuchBeanDefinitionException`
```


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
loader=AnnotationConfigContextLoader.class,
classes=ApplicationContextTestResourceNameType.class)

```

`@Resource(name="namedFile")`
Define the bean in ApplicationContextTestResourceNameType

```
@Configuration
public class ApplicationContextTestResourceNameType {

`@Bean(name="namedFile")`
```
<h3> Qualifier</h3>

Configuration is where all beans are declared

Application Context:
```
@Configuration
public class ApplicationContextTestResourceQualifier {

    @Bean(name="defaultFile")
    public File defaultFile() {
        File defaultFile = new File("defaultFile.txt");
        return defaultFile;
    }

    @Bean(name="namedFile")
    public File namedFile() {
        File namedFile = new File("namedFile.txt");
        return namedFile;
    }
}
```
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
loader=AnnotationConfigContextLoader.class,
classes=ApplicationContextTestResourceQualifier.class)
```
```
@Resource
@Qualifier("defaultFile")
private File dependency1;
```
Inject specific dependencies

<h2>Setter Injection</h2>

By Name:
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
  loader=AnnotationConfigContextLoader.class,
  classes=ApplicationContextTestResourceNameType.class)
@Resource(name="namedFile")
    protected void setDefaultFile(File defaultFile) {
        this.defaultFile = defaultFile;
    }
```

By Type:
```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
loader=AnnotationConfigContextLoader.class,
classes=ApplicationContextTestResourceNameType.class)
@Resource

```
Bean class type match reference variable class type.

```
@Resource
@Qualifier("namedFile")
```
`NoUniqueBeanDefinitionException`

<h2>@Inject</h2>

```
@Component
public class Dependnecy {
    
}
```
```
@Inject
private Dependency dependency;
```
resolve by type first
resolve by how bean is defined in application context

<h3>Qualifier</h3>
```
@Inject
@Qualifier("")
Or NoUniqueBeanDefinitionException
<h3> Name
```

```
private final String label = "fieldDependency"
@Inject
@Named("fieldDependency "")
@Qualifier("defaultFile")

```
<h2>Difference:</h2>

@Inject vs @Resource

@Resource:
Name
Type
Qualifier

@Inject:
Type
Name
Qualifier

Setter:

@Resource: annotate reference variable
@Inject: annotate setter method

@Autowired: similar to @Inject, @Autowired is part of Spring Framework

<h2> Applying Annotation</h2>
Design screnario and polymorphism
<h3> Singletons Through Polymorphism</h3>

Implementations of interface are abstract and throughout application
@Inject or @Autowired
Can swap classes when upgrading or applying patch
match by type

<h3> Application Behaviour Configuration through Polymorphism</h3>
Each behaviour based on different interfaces/abstract class.
<h3> Jakarte EE</h3>
All dependencies injected by Jakarta EE platform:
@Resource or @Inject

<h2>@Qualifier</h2>
 qualifier name to be used is the one declared in @Component annotation
Can also use n the Formatter

@Component
@Qualifier("fooFormatter")
public class FooFormatter implements Fortmatter{}

<h2>@Qualifier vs @Primary</h2>
@Primary: bean that Spring will inject by default

@Qualifier: injected precedence














