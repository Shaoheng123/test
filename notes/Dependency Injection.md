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

Automatically register in ApplicationCOntext scan current and sub packages
<h2>Inject Beans using AutoWired</h2>

Autowire can be used on Field, Setter and Constructor


