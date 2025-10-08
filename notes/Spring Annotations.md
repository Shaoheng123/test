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
public class Database {]
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

@Value:
injecting values into field in Spring Managed beans
applied at field/constructor/method parameter 