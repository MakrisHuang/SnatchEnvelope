package com.makris.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootConfiguration
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.HIGHEST_PRECEDENCE
)
@ComponentScan(
        basePackages = "com.makris.site"
)
public class ApplicationConfiguration {
    private static final Logger logger = LogManager.getLogger(ApplicationConfiguration.class);

    @Autowired
    ResourceLoader resourceLoader;

//    @Bean
//    public MessageSource messageSource(){
//        ReloadableResourceBundleMessageSource messageSource =
//                new ReloadableResourceBundleMessageSource();
//        messageSource.setCacheSeconds(-1);
//        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
//        messageSource.setBasenames(
//                "/WEB-INF/i18n/"
//        );
//        return messageSource;
//    }
//
//    @Bean
//    public LocalValidatorFactoryBean localValidatorFactoryBean()
//    {
//        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(this.messageSource());
//        return validator;
//    }
//
//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor()
//    {
//        MethodValidationPostProcessor processor =
//                new MethodValidationPostProcessor();
//        processor.setValidator(this.localValidatorFactoryBean());
//        return processor;
//    }

    @Bean
    public BasicDataSource basicDataSource(){
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/RequestEnvelop");
        bds.setUsername("root");
        bds.setPassword("password");
        bds.setMaxActive(255);
        bds.setMaxIdle(5);
        bds.setMaxWait(1000);
        return bds;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException{
        SqlSessionFactoryBean sqlSessionFactoryBean =
                new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(basicDataSource());

        // Set MyBatis Configuration file
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/mybatis-config.xml"));

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/sqlMapper/*.xml");
        logger.debug("get resource");
        logger.debug(resources);
        sqlSessionFactoryBean.setMapperLocations(resources);


        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dstm =
                new DataSourceTransactionManager();
        dstm.setDataSource(this.basicDataSource());
        return dstm;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc =
                new MapperScannerConfigurer();
        msc.setBasePackage("com.makris.site.mapper");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        msc.setAnnotationClass(Mapper.class);
        return msc;
    }
}
