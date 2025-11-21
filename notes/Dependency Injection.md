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

<h2>ComponentScan</h2>

Annotate to make into Spring Beans
Tell Spring where to search for annotated classes. Not all classes must becme beans in every run

<h2>Default</h2>
<h3>ComponentScan</h3>
Use `@ComponentScan` and `@Configuration`
@ComponentScan scans current package and sub-packages

```
package com.baeldung.comoponentscan.springapp
@Configuration
@ComponentScan
public class SpringComponentScanApp {

    private static ApplicationContext applicationContext
    
    @Bean
    public ExampleBean exampleBean() {
        return new ExampleBean();
    }
    public static void main(String[] args) {
    applicationContext = new AnnotationConfigApplicaitonCOntext(SpringComponentScanApp.class);
    for (String beanName:applicationContext.getBeanDefinitionNames(){
        sout(bean.name());
    })
        }
    }
}
```
```
package com.baeldung.componentscan.springapp.animals;
@Component
public class Cat{
}
```
Only location of configuration class matters as component scanning starts from its package

`@SpringbootApplication` consists of `@AutoConfiguration`, `@Configuration`,`@ComponentScan`
@EnableAutoConfiguration creates beans automatically based on pom.xml dependencies

<h2>@ComponentScan (specific package)</h2>
```
@ComponentScan(basePackages = "com.baeldung.componentscan.springapp.animals
")
@Configuration
public class SpringComponentScanApp{
}

```
SpringComponentScanAPp created as its a configuration passed as argument to AnnotationConfigApplicationContext
bean configured inside configuration.
`@ComponentScan(basePackages = {"com.baeldung.componentscan.springapp.animals","com.baeldung.componentscan.springapp.flowers""})`

<h2>@ComponentScan with Exclusions</h2>

`@ComponentScan(excludeFilters = @ComponentScan.Filter(type=FilterType.REGEX,pattern="com\\.baeldung\\.componentscan\\.springapp\\.flowers\\..))`
`@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Rose.class))`

<h2>Default Package</h2>
scans all the jars in classpath, error and won't start

<h1> Why not field Injection</h1>

1. Null Safety
2. Immutability
3. Design
   1. Single Responsibility Violation
   2. Circular Depdencies
   3. Testing

<h2> Null Safety </h2>

No direct way of instantiating EmailService
Can create instance of EmailService without EmailValidator

<h2> Immutability</h2>
Unable to create immutable class
Autowiring when constructors are called, unable to autowire final fields using field injection
Unable to ensure that dependencies will not change
so unexpected side effect might occur.

<h2> Design Problems</h2>
<h3> Single Responsibility Violation</h3>
Add more dependencies than required
<h3> Circular Dependencies</h3>
Circular dependencies might go unnoticed as it is not on the context load
BeanCurrentlyInCreationException won't be thrown

<h2>Testing</h2>
- Cannot mock object
- @InjectMocks
- Instantiate through reflection
- Failure not reported

<h1> Spring inject on static field</h1>

Unable to inject value to static field, will still be null.

<h2> Solution</h2>

Declare static variables with getter/setter

`private static String Static_name;`

<h3> Setter Injection</h3> 

```
@Value("${name}")
public void setStaticName(String name) {
    Static_Name = name;
}
```

<h1> Spring Core Annotation</h1>

<h2> Di-Related Annotations</h2>

<h3>@Autowired</h3>

- constructor
  - all arguments mandatory(if declare 2 constructors)  
- setter
- field

 <h3>@Bean</h3>

Called when return type required
same name as factory method or `@Bean("engine")`

All Methods annotated with @Bean must be in @Configuration classes

<h3>@Qualifier</h3>
with @Autowired 
1. used when there are multiple matching definitions
2. Constructor Injection
     
   ```
   @Autowired
    Biker(@Qualifier("bike) Vehicle vehicle) {}
   ```
  ```
@Autowired
@Qualifier("bike)
void setVehicle( Vehicle vehicle) { 
    this.vehicle = vehicle;
}

```

3. Field
    ```
   
   
   ```
4. @Value
    - Injecting property values into beans
<h3>Constructor Injection</h3>
   ```
   Engine(@Value() int cylinderCount) {
    this.cylinderCount = cylinderCount;
   }
   ```
<h3>Setter Injection</h3>
```
   @Value("8")
   @Autowired
    void setCylinderCount(int cylinderCount){
        this.cylinderCount = cylinderCount;
    }
```

<h3>Field Injection</h3>
```
@Value("8")
int cylinderCount;

```
<h3>Inject from External source</h3>
```
@Value("${engine.fuelType}")
String fuelType;

```
<h3>@DependsOn</h3>
```
@Bean
@DependsOn("engine")
class Car implements Vehicle {}

```
Initialized depended beans before annotated bean
Only need when dependencies are implicit.

If define bean with @Bean Factory method annotated with @DependsOn
```
@Bean
@DependsOn("fuel)
Engine engine() {
    return new Engine();
}
```

<h3>@Lazy</h3> 
Create bean when requested, not when startup

- @Bean:delay method call 
- @Configuration:  all contained Bean methods will be affected
- @Component: initialize lazily
- @Autowired: load the bean lazily
```
@Bean
@Lazy(false)
```
Override lazy configuration in @Component class

<h3>@LookUp</h3>

return instance of method return type when invoked

<h3>@Primary</h3>
chose this on if unqualified injection points.
```
@Component
@Primary
class Car implements Vehicle

@Component
class Bike implements Vehicle

@Component
class Driver {
    @Autowired
    Vehicle vehicle;
}

@Component
class Biker {
    @Autowired
    @Qualifier("bike")
    Vehicle vehicle
}
```

Default class for driver is a Car, biker will be bike as qualified

<h3> Scope</h3>h3>

Define scope of @Component and @Bean

- singleton
- prototype
- request
- session
- globalSession
- custom

```
@Component 
@Scope("prototype)
class Engine(){}
```
<h3> Import</h3>
Specify @Configuration class without component scanning provide class with @Import 

```
@Import("VehiclePartSupplier")
class VehicleFactoryConfig{}
```

<h3> Import Resource</h3>

```
@Configuration
@ImportResource("classpath:/annotations.xml")
class VehicleFactoryConfig
```

<h3> @PropertySources</h3>

```
@Configuration
@PropertySource("classpath:/annotations.properties")
class VehicleFactoryCOnfig

@Configuration
@PropertySources({
@PropertySource("classpath:/annotations.properties")
@PropertySource("classpath:/vehicle.properties")
})
```
<h3> @Primary</h3>
Used to register more than one bean of the same type

<h3> ApplicationContext</h3>
Start Application COntext

```
AnnotationConfigApplicationContext context = new Annotation(Config.class);
Employee employee = context.getBean(Employee.class);
```
<h3> @Primary with @Component</h3>

```
public interface Manager {
    String getManagerName();
}

@Component
public class DepartmentManager implements Manager {
    @Override
    public String getManagerName() {
        return "Department Manager";
    }
}

@Component
@Primary
public class GeneralManager implements Manager {
    @Override
    public String getManagerName() {
        return "General Manager";
    }
}
```
Only makes sense when enable component scan

```
@Configuration
@ComponentScan(basePackage="org.baeldung.primary")
public class Config {}

@Service
public class ManagerService{
    @Autowired 
    private Manager manager;
    public Manager getManager(){
        return manager;
    }
}
```
<h3>Bean Naming</h3>
Need to ensure each bean name is unique within the context
Spring uses method as the default bean name
Cannot have multiple beans with the same name in the same configuration class
<h4>
`@Bean(name = "mercedesCar")`
</h4>
- Make sure each method in configuration class has a unique name.

<h2>@Order</h2>
sorting order of annotated component or bean
`@Order(Ordered.HIGHEST_PRECEDENCE)`
<h3>Usage(Spring 4.0)</h3>
Ordering injected components to a collection.
Inject Autowired beans of same type based on order value

<h3>How to use</h3>

- Interface Creation 
- Component Creation(@Order(1))
- Testing 
```
@Order(1)
@Order(2)
@Order(Highest_Precedence)
@Order(Lowest_Precedence)
```

Dependency relations and @DependsOn declaration determine singleton startup order

<h2>Abstract class Autowired</h2>

<h3>Setter Injection</h3>

```
public abstract class {
    private LogRepository logRepository;
    
    @Autowired
    public final void LogRepository() {
        this.logRepository = logRepository;
    }
}
```
Need to use final keyword so subclass won't override setter method
May not be stable if subclass overrides setter method

<h3> Constructor Injection</h3>

use @Autowired on constructor of subclass
```
@Component
public class BasketballService extends BallService {
    @Autowired
    public BasketballService(RuleRepository ruleRepository) {
        super(ruleRepository);
    }
}
```
<h2>Lombok</h2>


Usage:
- Automate code generation without performance impact on runtime
- single annotation instead of longer code

Generate a constructor for class's field

- @AllArgsConstructor(All fields)
- @RequiredConstructor(All final fields)
- @NoArgsConstructor(Empty constructor)

Need to annotate

- `@Autowired`
- `@Inject`
- `@Resource`

<h3> Annotating Constuctor</h3>

```
@Component
@RequiredArgsConstructor = @__(@Autowired)
```
Delay complication as annotation processor may create it later
<h2>Injecting Prototype Beans into Singleton instance</h2>
Default: singleton
```
@Configuration
public class AppConfig{

@Bean
@Scope(ConfigurableBeanFactory.scopePrototype){
public Prototype()Bean

}
```
```
public class SingletonBean(){
@Autowired
private PrototypeBean() prototypeBean;
}
```

returns the same instance if 
@Component also creates a bean of the class

<h3>Solution:</h3>

- @Lookup
- javax.inject.api
- Scoped Proxy
- Object factory interface

<h3>@Lookup</h3>
```
@Component
public class SingletonLookupBean(){
    @Lookup
    public PrototypeBean getPrototypeBean() {
        return null;
    }
}
```
override getPrototypeBean   and get new instance

<h3>javax.inject.API</h3>

```
public class SingletonProvider {
    @Autowired
    private Provider<PrototypeBean> prototypeBeanProvider;
    
    public PrototypeBean getPrototypeBeanInstance() {
        return prototypeBeanProvider.get();
    }
}

```

<h3>Scoped Proxy</h3>

create a proxy object to wire real object with dependent object
proxy object decide whether to use or create new object

```
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
proxyMode = ScopedProxyMode.TARGET_CLASS
```

```
@Configuration
public class Appconfig {
    @Bean
    public Function<String, PrototypeBean > beanFactory() {
        return name -> prototypeBeanwithParam();
    }
    @Bean
    @Scope(value = "prototype")
    public prototypeBeanwithParam(String name) {
        return new PrototypeBean(name);
    }
}
```
<h2> How to dynamically autowire a bean in Spring</h2>

```
@Service("G8regionService")
public class G8RegionService implements RegionService() {
    @Override
    public boolean isServiceActive() {
        return false; 
    }
    @Override
    public String getIsoCountryCode() {
    
    }
}
```
Dynamically change RegionService interface implementation based on user input

<h3> Usage</h3>

- beanfactory
- interface

<h3>Bean Factory<h3>

- Root interface to access spring container
- Autowire Beanfactory for class

@Service
public class BeanFactoryDynamicAutowireService {
private static final String SERVICE_NAME_SUFFIX = "regionService";
private final BeanFactory beanFactory;
```
    @Autowired
    public BeanFactoryDynamicAutowireService(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public boolean isServerActive(String isoCountryCode, int serverId) {
        RegionService service = beanFactory.getBean(getRegionServiceBeanName(isoCountryCode), 
          RegionService.class);

        return service.isServerActive(serverId);
    }

    private String getRegionServiceBeanName(String isoCountryCode) {
        return isoCountryCode + SERVICE_NAME_SUFFIX;
    }
}
```

<h3>Interface</h3>
Better choice

```
@Service
public class CustomMapFromListDynamicAutowire {
    private final Map<String,Integer> servicesByCountryCode;
    
    @Autowired
    public CustomMapFromListDynamicAutowireService() {
        servicesByCountryCode = regionServices.stream()
            .collect(Collectors.toMap(RegionService::getISOCountryCode,Function.identity()));
    ]
}

public boolean isServerActive(String isoCountryCode, int serverId) {
        RegionService service = servicesByCountryCode.get(isoCountryCode);

        return service.isServerActive(serverId);
    }
```
Map to hold implementation by country code 


<h2> Spring Factory Bean</h2>

- Ordinary beans
- Factory bean

```
public interface FactoryBean {
    T getObject() throws Exception; - object used by Spring container 
    Class<?> getObjectType(); - return type of object FactoryBean produces
    boolean isSingleton(); - denote if object produced by factorybean is singleton
}
```

FactoryBean With XML-based Configuration

```
<beans ...>

    <bean id="tool" class="com.baeldung.factorybean.ToolFactory">
        <property name="factoryId" value="9090"/>
        <property name="toolId" value="1"/>
    </bean>
</beans>
```
<h3> Testing</h3>

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:factorybean-spring-ctx.xml" })
public class FactoryBeanXmlConfigTest {
@Autowired
private Tool tool;

    @Test
    public void testConstructWorkerByXml() {
        assertThat(tool.getId(), equalTo(1));
    }

```

<h3> Accessing Factory Beans</h3>

`    @Resource(name = "&tool")
    private ToolFactory toolFactory;
`
<h3>Programmatically</h3>

```
@Bean
    public Tool tool() throws Exception {
    return toolFactory().getObject();
}
```

<h3> Initialize</h3>

Perform operations after Factory Bean set but before getObject called
`@PostConstruct`

<h3> Abstract Factory Bean</h3>

Implement factory bean  which creates singleton or prototype

encapsulate complex construction logic or configure highly configuration objects easier in Spring.

<h3> Injecting Collections</h3>

- List
- Set
- Map

<h4>List</h4>

```
@Autowired
private List<String> nameList;
```

```
@Configuration
public class CollectionConfig {
    @Bean
    public CollectionsBean getCollection() {
        return new CollectionBean();
    }
    
    @Bean
    public List<String> nameList() {
        return Arrays.asList();
    }
}
```

<h4>Set</h4>

```
public class CollectionsBean(){
    private Set<String> nameSet;
    public CollectionBeans(Set<String> String) {
        this.nameSet = strings;
    }
}
```

<h4>Map</h4>

```
@Autowired
public void setNameMap<>(Map<Integer,String> nameMap){
    this.nameMap = nameMap;
}

```

Configuration:

```
@Bean
public Map<Integer, String> nameMap(){
    Map<Integer, String>  nameMap = new HashMap<>();
    nameMap.put(1, "John");
    nameMap.put(2, "Adam");
    nameMap.put(3, "Harry");
    return nameMap;
}

```
<h3> Injecting Beans</h3>

```
public class Bean {
@Autowired(required = false)
private List<Bean> beanList;
public void printBeanList<>() {
    System.out.println(beanList);
}

}
```

```
@Configuration
public class CollectionConfig {
    @Bean 
    public Bean getElement(){
        
    }
    @Bean 
    public Bean getElement(){
        
    }
}
```
- Spring containers inject individual beans into one List collection
print using printBeanList method


`@Autowired(required = false)`

<h3>Use Order</h3>
`@Bean`
`Order(1)`

Specify the order of injection into a list

<h3> Use @Qualifier to select beans</h3>

```
@Autowired
@Qualifier("CollectionsBean")
private List<BaeldungBean> beanList;
```

```
@Configuration
public class CollectionConfig {
    @Bean
    @Qualifier("CollectionsBean")
}
```
Specify  bean injected into CollectionsBean

<h3> Setting Empty List as default value</h3>

- `@Value("${names.list:}#{T(java.util.Collections).emptyList()}")`

<h2> Inject Map from YAML file </h2>
<h3> Use @PropertySource to load YAML files</h3>

<h3> Inject Map from YAML files</h3>

- `@ConfigurationProperties`
Define values in application.yml
- Map application to Map<String,String>
- inject config details with String keys and object from credentials as value
```
server:
  application:
    name: InjectMapFromYAML
    url: http://injectmapfromyaml.dev
    description: How To Inject a map from a YAML File in Spring Boot
  config:
    ips:
      - 10.10.10.10
      - 10.10.10.11
      - 10.10.10.12
      - 10.10.10.13
    filesystem:
      - /dev/root
      - /dev/md2
      - /dev/md4
  users: 
    root:
      username: root
      password: rootpass
    guest:
      username: guest
      password: guestpass
```
<h3>Inject Config details</h3>
Create bean to  encapsulate binding properties to Map
Map all properties with specified prefix to ServerProperties object
```
@Component
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

    private Map<String, String> application;
    private Map<String, List<String>> config;
    private Map<String, Credential> users;

    // getters and setters

    public static class Credential {
    	
        private String username;
        private String password;
        
        // getters and setters
        
    }
}
```

<h3>@ConfigurationProperties vs @Value</h3>

- Configuration Properties binds multiple properties to particular objects
and provide properties via mapped object
- Centralize and groupto structured to inject later to other beans.


