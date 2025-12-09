<h1> Spring Boot</h1>

- convention over configuration
- minimum effort standalone production grade applications
<h2>Set up</h2>
- Spring initializr to generate the base for our project
- relies on Boot parent
- starter web
- starter-data-jpa
- h2 database

<h2>Application Configuration</h2>
- ```
  @SpringBootApplication
  public class Application {
  main(){
    SpringApplication.run(Application.class,args);
  }
  }
  ```
  - @SpringBootApplication includes:  
    - Configuration
    - EnableAutoConfiguration
    - ComponentScan
  - application.properties
  - spring-boot-starter-security
    - secure endpoints by default using httpBasic or formLogic based on content negotiation strategy
    - define own custom Security Configuration
    - Custom Security Configuration
      - ```
        @Configuration
        @EnableWebSecurity
        public class SecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.
                authorizeHttpRequests(expressionInterceptionUrlRegistry -> expressionInterceptUrlRegistry
                    .anyRequest().permitAll()).csrf(AbstractHttpConfigurer::disable);
                return http.build();
        }
      
        }
        ```