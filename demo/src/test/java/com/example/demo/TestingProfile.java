package com.example.demo;
import com.example.demo.config.AccountConfig;
import com.example.demo.config.DatasourceConfig;
import com.example.demo.config.DevDataSourceConfig;
import com.example.demo.config.ProductionDataSourceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@ContextConfiguration(classes = {AccountConfig.class},loader = AnnotationConfigContextLoader.class)
public class TestingProfile {

    @Autowired
    DatasourceConfig dataSourceConfig;

    @Autowired
    private Environment environment;

    @Test
    public void getActiveProfile() {
        Assertions.assertEquals("dev",environment.getActiveProfiles()[0]);
    }

    @Test
    public void testSpringProfile() {
//        Assertions.assertTrue(dataSourceConfig);
        Assertions.assertInstanceOf(ProductionDataSourceConfig.class, dataSourceConfig);

    }
    @Test
    public void testingTomCatAndDataSourceConfig() {


    }
}
