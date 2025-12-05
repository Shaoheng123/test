<h1> SPring Batch</h1>

<h2> Intro</h2>

job repository schedules and interacts with jobs
- reads
- process
- write

framework does heavy lifting
- low level persistence work 
- h2 as job repository

<h2> Dependencies</h2>

- Spring Batch Core,
- Spring Object/XML Marshalling (OXM)
- H2 database

<h2> Auto Create Schema</h2>

- use pre-packaged SQL initialization scripts to auto-create schema on start-up
  - h2 database
    - Springboot automatically runs corresponding SQL initialization script to initialize the database
  - Other Supported database
    - configure Spring Boot properties to automatically detect database and run corresponding SQL initialization script
    - Spring-Boot: `application.properties`
      - `spring.batch.jdbc.initialize-schema=always`
    - Spring-Boot: `application.yml`
      - ```
        spring:
         batch:
          jdbc:
           initialize-schema: "always"
        ```
      - Do not annotate BatchConfig with @EnableBatchProcessing
      - Springboot takes control of configuring Spring Batch including creating Batch schema in auto-configured data source
      - switch off auto-creation of Spring Batch schema including embedded H2 database
        - `spring.batch.jdbc.initialize-schema=never`
      - Turn off Automatic database initialization
        - ```
          spring:
           batch:
            jdbc:
             initialize-schema: "never"
          ```
<h2> Spring Batch and Job Config</h2>
<h3>Java Based job configuration</h3>

- ```
  @Profile("spring")
  public class SpringBatchConfig {

    @Value("input/record.csv")
    private Resource inputCsv;

    @Value("file:xml/output.xml")
    private Resource outputXml;

    @Bean
    public ItemReader<Transaction> itemReader()
      throws UnexpectedInputException, ParseException {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<Transaction>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] tokens = { "username", "userid", "transactiondate", "amount" };
        tokenizer.setNames(tokens);
        reader.setResource(inputCsv);
        DefaultLineMapper<Transaction> lineMapper = 
          new DefaultLineMapper<Transaction>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<Transaction, Transaction> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<Transaction> itemWriter(Marshaller marshaller)
      throws MalformedURLException {
        StaxEventItemWriter<Transaction> itemWriter = 
          new StaxEventItemWriter<Transaction>();
        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource(outputXml);
        return itemWriter;
    }

    @Bean
    public Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[] { Transaction.class });
        return marshaller;
    }

    @Bean
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, 
      ItemReader<Transaction> reader, ItemProcessor<Transaction, Transaction> processor, 
      ItemWriter<Transaction> writer) {
        return new StepBuilder("step1", jobRepository)
          .<Transaction, Transaction> chunk(10, transactionManager)
          .reader(reader).processor(processor).writer(writer).build();
    }

    @Bean(name = "firstBatchJob")
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {
        return new JobBuilder("firstBatchJob", jobRepository).preventRestart().start(step1).build();
    }
    
    public DataSource dataSource() {
     EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
     return builder.setType(EmbeddedDatabaseType.H2)
       .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
       .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
       .build();
    }
    
    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }
    
    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource());
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
    
    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
       TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
       jobLauncher.setJobRepository(getJobRepository());
       jobLauncher.afterPropertiesSet();
       return jobLauncher;
    }
  }
  ```

<h3> XML</h3>

- ```
      <bean id="itemReader" class="org.springframework.batch.item.file.FlatFileItemReader">
        <property name="resource" value="input/record.csv" />
        <property name="lineMapper">
            <bean class="org.springframework.batch.item.file.mapping.DefaultLineMapper">
                <property name="lineTokenizer">
                    <bean class="org.springframework.batch.item.file.transform.DelimitedLineTokenizer">
                        <property name="names" value="username,userid,transactiondate,amount" />
                    </bean>
                </property>
                <property name="fieldSetMapper">
                    <bean class="com.baeldung.batch.service.RecordFieldSetMapper" />
                </property>
            </bean>
        </property>
        <property name="linesToSkip" value="1" />
    </bean>
    
    <bean id="itemProcessor" class="com.baeldung.batch.service.CustomItemProcessor" />
    
    <bean id="itemWriter" class="org.springframework.batch.item.xml.StaxEventItemWriter">
        <property name="resource" value="file:xml/output.xml" />
        <property name="marshaller" ref="marshaller" />
        <property name="rootTagName" value="transactionRecord" />
    </bean>
    
    <bean id="marshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
        <property name="classesToBeBound">
            <list>
                <value>com.baeldung.batch.model.Transaction</value>
            </list>
        </property>
    </bean>
    
    <batch:job id="firstBatchJob">
    <batch:step id="step1">
    <batch:tasklet>
    <batch:chunk reader="itemReader" writer="itemWriter"
    processor="itemProcessor" commit-interval="10">
    </batch:chunk>
    </batch:tasklet>
    </batch:step>
    </batch:job>
    
    
    <!-- connect to H2 database -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="org.h2.Driver" />
        <property name="url" value="jdbc:h2:file:~/repository" />
        <property name="username" value="" />
        <property name="password" value="" />
    </bean>
    
    <!-- stored job-meta in database -->
    <bean id="jobRepository" class="org.springframework.batch.core.repository.support.JobRepositoryFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="transactionManager" ref="transactionManager" />
        <property name="databaseType" value="h2" />
    </bean>
    
    <bean id="transactionManager" class="org.springframework.batch.support.transaction.ResourcelessTransactionManager" />
    
    <bean id="jobLauncher" class="org.springframework.batch.core.launch.support.SimpleJobLauncher">
        <property name="jobRepository" ref="jobRepository" />
    </bean>
  ```

<h3> Read data and create Objects with ItemReader</h3>

- ```
    @SuppressWarnings("restriction")
    @XmlRootElement(name = "transactionRecord")
    public class Transaction {
    private String username;
    private int userId;
    private LocalDateTime transactionDate;
    private double amount;

    /* getters and setters for the attributes */

    @Override
    public String toString() {
        return "Transaction [username=" + username + ", userId=" + userId
          + ", transactionDate=" + transactionDate + ", amount=" + amount
          + "]";
        }
    }
  ```
Custom Mapper
  - ```
    public class RecordFieldSetMapper implements FieldSetMapper<Transaction> {
 
      public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyy");
          Transaction transaction = new Transaction();
 
          transaction.setUsername(fieldSet.readString("username"));
          transaction.setUserId(fieldSet.readInt(1));
          transaction.setAmount(fieldSet.readDouble(3));
          String dateString = fieldSet.readString(2);
          transaction.setTransactionDate(LocalDate.parse(dateString, formatter).atStartOfDay());
          return transaction;
      }
      }
    ```

<h2> Processing data with ItemProcessor</h2>
    Passing data from reader to writer

- ```
  public class CustomItemProcessor implements ItemProcessor<Transaction, Transaction> {

    public Transaction process(Transaction item) {
        return item;
        }
    }
  ```

<h3> Writing Objects to the fs with ItemWriter</h3>

- ```
      <bean id="itemWriter" class="org.springframework.batch.item.xml.StaxEventItemWriter">
        <property name="resource" value="file:xml/output.xml" />
        <property name="marshaller" ref="marshaller" />
        <property name="rootTagName" value="transactionRecord" />
    </bean>
  ```

<h3> Configuring the Batch job</h3>

- Connect the dots using batch:job syntax
- commit-interval 
  - number of transactions to be kept in memory before commiting batch to Item-writer
  - hold transactions in memory until point or end of input data is encountered
  - ```
    @Bean
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
    @Qualifier("itemProcessor") ItemProcessor<Transaction, Transaction> processor, ItemWriter<Transaction> writer) {
    return new StepBuilder("step1", jobRepository)
    .<Transaction, Transaction> chunk(10, transactionManager)
    .reader(itemReader(inputCsv))
    .processor(processor)
    .writer(writer)
    .build();
    }
    ```
```
<batch:job id="firstBatchJob">
    <batch:step id="step1">
        <batch:tasklet>
            <batch:chunk reader="itemReader" writer="itemWriter" processor="itemProcessor" commit-interval="10"></batch:chunk>
    </batch:tasklet>
    </batch:step>
</batch:job>

```

<h4> Running Batch job</h4>
We run our Spring application using -Dspring.profiles.active=spring profile.

- ```
  @Profile("spring")
    public class App {
    public static void main(String[] args) {
    // Spring Java config
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(SpringConfig.class);
    context.register(SpringBatchConfig.class);
    context.refresh();
    
            JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
            Job job = (Job) context.getBean("firstBatchJob");
            System.out.println("Starting the batch job");
            try {
                JobExecution execution = jobLauncher.run(job, new JobParameters());
                System.out.println("Job Status : " + execution.getStatus());
                System.out.println("Job completed");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Job failed");
            }
        }
    }
  ```

<h3> Run Batch Job in particular order</h3>

- jobs execute in specific order by creating parent job that orchestrate execution of multiple child jobs
- when output of job is required as input for another or when jobs need to be executed sequentially due to business logic
- ```
  @Bean
    public Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("firstStep", jobRepository)
    .<String, String>chunk(1, transactionManager)
    .reader(new IteratorItemReader<>(Stream.of("Data from Step 1").iterator()))
    .processor(item -> {
    System.out.println("Processing: " + item);
    return item;
    })
    .writer(items -> items.forEach(System.out::println))
    .build();
    }
    
    @Bean
    public Step secondStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("secondStep", jobRepository)
    .<String, String>chunk(1, transactionManager)
    .reader(new IteratorItemReader<>(Stream.of("Data from Step 2").iterator()))
    .processor(item -> {
    System.out.println("Processing: " + item);
    return item;
    })
    .writer(items -> items.forEach(System.out::println))
    .build();
    }
  ```
  
- Parent job that executes defined steps in desired order
- parent job starts with first step and proceed  to second step
- use JobBuilder to define sequence of steps
```
@Bean(name = "parentJob")
public Job parentJob(JobRepository jobRepository,
    @Qualifier("firstStep") Step firstStep,
    @Qualifier("secondStep") Step secondStep) {
    return new JobBuilder("parentJob", jobRepository)
      .start(firstStep)
      .next(secondStep)
      .build();
}
```

- run Multiple Spring Batch jobs in specified order without relying on complex item processing or reading mechanism

<h2> Spring Boot Configuration</h2>
<h3> Maven</h3>

- need database to store Spring Batch job information
- use in-memory database as configured 
 
<h3> Spring Boot Config</h3>

- @Profile
  - use profile to distinguish between Spring and Spring Boot
  - ```
    @SpringBootApplication
    public class SpringBatchApplication {
        public static void main(String[] args) {
            SpringApplication springApp = new SpringApplication(SpringBatchApplication)
        }
    }
    ```
<h3> Spring Batch Job Config</h3>

- ```
  @Configuration
    public class SpringBootBatchConfig {

        @Value("input/record.csv")
        private Resource inputCsv;
    
        @Value("input/recordWithInvalidData.csv")
        private Resource invalidInputCsv;
    
        @Value("file:xml/output.xml")
        private Resource outputXml;
    
        // ...
    }
  ```
  - declare manually JobRepository, JobLauncher and TransactionManager beans
  - JobBuilder and StepBuilder class with name of job/step builder recommended

