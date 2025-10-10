<h1> Profiles</h1>
Profile: map beans to different prfile(dev,test,prod)

Annotate bean with profile
Include into a profile
`@Component`
`@Profile("dev")`

Exclude from a profile
```
@Component
@Profile("!dev")
```
Combining multiple profile
```
@Component
@Profile("a&b&c")
```

Declaring in XML
```
<bean profile = "dev">
    <bean id="devDatasourceConfig">
        class="org.baeldung.profiles.DevDatasourceConfig"/>
</bean>
```
<h2>Set Profile:</h2>
<h3>WebApplicationInitializer Interface</h3>
```
@Configuration
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
    servletContext.setInitParameter("spring.profiles.active","dev");
    }
}
```
<h3>ConfigurableEnvironment</h3>

```
@Autowired
private ConfigurableEnvironment env;
env.setActiveProfiles("dev");
```


<h3>Context Parameter in web.xml </h3>
```
<context-param>
    <param-name> contextConfigLocation</param-name>
    <param-value>/WEB-INF/app-config.xml</param-value>
</context-param>
<context-param>
    <param-name>spring.profiles.active</param-name>
    <param-value>dev</param-value>
</context-param>
```
JVM System Params:
`-Dspring.profiles.active=dev`

Environment Variable:
`export spring_profiles_active=dev`
<h3>Maven</h3>
```
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
</profiles>

<h2>Application Properties</h2>
spring.profiles.active = @spring.profiles.active@  
Enable resource filtering

<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering> true</filtering>
        </resource>
    </resources>
</build>
```
<h2>Choosing Maven Profile</h3>
mvn clean package -Pprod
command package application for prod profile
applies the `spring.profiles.active` values for this application when running

<h3> ActiveProfile in Tests</h3>

`@ActiveProfiles("dev")`
Priority:
- Context parameter
- WebApplicationInitializer
- JVM System parameter
- Environment variable
- Maven profile

<h3>Default Profile</h3>
spring.profiles.default
<h2>Active Profile</h2>
@Profile annotation for enable or disabling beans

<h3>Accessing beans using Environment</h3>
```
public class ProfileManager {
    @Autowired
    private Environment environment
    public void getActiveProfile() {
        for(String profileName:environment.getActiveProfiles()) {
        }
    }
}
```
<h3>spring.profiles.active</h3>
```
@Value("${spring.profiles.active:}")
private String activeprofile;
```

<h2>Maintaining data configuration</h2>
```
public interface DatasourceConfig{
    public void setup();
}
```

```
@Component
@Profile("dev")
public class DevDataSourceConfig implements DataSourceConfig {
    @Override
    public void setup(){
    }
}
```
<h3>Testing</h3>
```
@RunWith(SpringJunit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(classes = {AccountConfig}),loader = AnnotationConfigContextLoader.class)
    @Autowired
    DatasourceConfig datasourceConfig;

    @Test
    public void testSpringProfiles(){
        Assertions.assertInstanceOf(dataSourceConfig,DevDataSourceConfig)
    }
```
<h2>Multiple</h2>
`@ActiveProfiles({"tomcat","mysql"})`

<h2>Profiles in SpringBoot</h2>
<h3>Spring Application</h2>
`SpringApplication.setAdditionalProfiles("dev");`
<h3>Maven</h3>
```
<plugins>
    <plugin>
        <groupId> org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
            <profiles>
                <profile>dev</profile>
            <profiles>
        </configuration>
    <plugin>
</plugins>
```
`mvn spring-boot:run`

<h2>Profile-specifc properties</h2>
<h3>Setting up datasource for application-production.properties file</h3>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/db
spring.datasource.username=root
spring.datasource.password=root

<h2>Multi-Document Files</h2>
specify properties in the same application.properties file
Last value is read value
```
my.prop = used-always-in-all-profiles
#---
spring.config.activate.on-profile=dev
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/db
spring.datasource.username=root
spring.datasource.password=root
```
<h3>Profile Groups</h3>

`spring.profiles.group.production=proddb,devdb`

