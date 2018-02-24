package com.makris.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.HIGHEST_PRECEDENCE
)
@ComponentScan(
        basePackages = "com.makris.site"
)
public class ApplicationConfiguration {
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
        bds.setUsername("root");
        bds.setPassword("password");
        bds.setMaxActive(255);
        bds.setMaxIdle(5);
        bds.setMaxWait(1000);
        return bds;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        SqlSessionFactoryBean sqlSessionFactoryBean =
                new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(basicDataSource());
        Resource resource = resourceLoader.getResource("classpath:/com/makris/mybatis/mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(resource);
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
        msc.setBasePackage("com.makris.site.sqlMapper");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        msc.setAnnotationClass(Repository.class);
        return msc;
    }
}
