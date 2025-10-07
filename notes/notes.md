SpringBoot
<br>Annotation:<br>
@SpringBootAnnotation:
<br>@ComponentScan<br>
@EnableAutoConfiguation
@Configuration

application.properties:
<br>server.port<br>

MVC Architecture:
![Alt text](https://www.baeldung.com/wp-content/uploads/2016/08/SpringMVC.png "a title")

Dispatcher Servlet intercept incoming request,
convert payload of request to internal structure of data
send data to model for processing
get processed data from model and send to View for rendering.
Controller send data via Http response

<b>Required libraries:
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.0.2</version>
</dependency>

Controller send data directly via Http Response
<br><b>Define a controller

@Controller
<br>
```
@Controller
@RequestMapping(value = "/test")

    @GetMapping
    public ModelAndView getTestData() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("welcome");
        mv.getModel().put("data", "Welcome home man");

        return mv;
    }
}
```

Any url call to /test will be routed by Dispatcher Servlet to getTestData method in TestController

<b>Rest Controller
Dispatcher Servlet bypass view Resolver and returns in HTTP Response body
```
@RestController
public class RestController {

    @GetMapping(value = "/student/{studentId}")
    public Student getTestData(@PathVariable Integer studentId) {
        Student student = new Student();
        student.setName("Peter");
        student.setId(studentId);

        return student;
    }
}
```
<b> Security</b>

<b>Custom Security</b>
```
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean 
}
```











