<h1> Spring Events </h1>
- create and public events that are synchronous
listener able t participate in publisher's transaction context

Application Event
```
public class CustomSpringEvent extends ApplicationEvent {
    private String message;
    public CustomSpringEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
```
<h2>Publisher </h2>

- inject ApplicationEventPublisher and use publishEvent() API
```
@Component
public class CustomSpringEventPublisher{
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    public void publishCustomEvent(final String message) {
        CustomSprintEvent customSpringEvent = new CustomSpringEvent(this,message);
        applicationEventPublisher.publishEvent(customSpringEvent);
    ]
}
```

- Listener
  - ```
    @Component
    public class CustomSpringEventListener implements ApplicationListener<CustomSpringEvent> {
        @Override
        public void onApplicationEvent(CustomSpringEvent event) {
            
        }
    }
    ```
    
parameterized with generic type of custom event, onApplicationEvent() type-safe
- Spring Events are synchronous
- DoStuffAndPublishAnEvent Method blocks until all listeners finish processing an event

<h3> Asynchronous events </h3>
