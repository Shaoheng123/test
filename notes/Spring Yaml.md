<h1>Spring Yaml</h1>

<h2>Declaring Spring profile</h2>
Path:`/application/src/main/resources/application.yml`

```
spring:
    config:
        activate:
            on-profile:test
name:test-YAML
environment: testing
enabled:false
servers:
    - www.abc.test.com            
___
spring:
    config:
        activate:
            on-profile:prod
name:prod-YAML
environment: production
enabled:true
servers:
    - www.abc.com
```
profile specific documents won't be loaded unless explicitly defined 

```
@Configuration  --mark as bean definitions
@EnableConfigurationProperties  -- enable @ConfigurationProperties annotated beans in Spring applicaion
@ConfigurationProperties  -- binds and validates external configurations to configuration class
public class YAMLConfig {

    private String name;
    private String environment;
    private boolean enabled;
    private List<String> servers = new ArrayList<>();
}

```
<h2> Accessing Yaml properties</h2>
Create  object of YAMLConfig class and access properties
application properties file: set active environment variable to prod

<h3> Application properties file</h3>
`spring.profiles.active = prod`
<h2> Accessing Properties</h2>
Testing using commandlineRunner

@SpringBootApplication
```
public class MyApplication implements CommandLineRunner { 
    @Autowired
    private YAMLConfig myConfig;
    
    public static void main(String[] args) {
    new SpringApplicationBuilder(DemoApplication.class) {
        .web(WebApplicationType.NONE).run()
        }
        
    @Override
    private void run(String... args) {
        sout(myConfig.getEnvironment)
        sout(myConfig.getEnabled)
    }    

```

<h2>Priority</h2>
1. External Properties
2. Profiles properties outside packaged jar
3. Profiles properties inside packaged jar
4. Application properties outside packaged jar
5. Applicaiton properties inside packaged jar



