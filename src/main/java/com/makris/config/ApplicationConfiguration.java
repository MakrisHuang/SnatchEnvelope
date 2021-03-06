package com.makris.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@SpringBootConfiguration
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.HIGHEST_PRECEDENCE
)
@ComponentScan(
        basePackages = "com.makris.site",
        useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,
        value = {Service.class})}
)
@EnableAsync
@EnableCaching
@PropertySource("classpath:/config/application.properties")
public class ApplicationConfiguration extends AsyncSupportConfigurer implements EnvironmentAware{
    private static final Logger logger = LogManager.getLogger(ApplicationConfiguration.class);
    private static final Logger schedulingLogger = LogManager.getLogger(logger.getName() + ".[scheduling]");

    @Autowired
    ResourceLoader resourceLoader;

    Environment env;

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

    @Profile("dev")
    @Bean
    public BasicDataSource devDataSource(){
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(env.getProperty("SPRING_DRIVER_CLASSNAME"));
        String host = env.getProperty("SPRING_DATABASE_HOST");
        String port = env.getProperty("SPRING_DATABASE_PORT");
        String dbName = env.getProperty("SPRING_DATABASE_NAME");
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        bds.setUrl(jdbcUrl);
        bds.setUsername(env.getProperty("SPRING_DATASOURCE_USERNAME"));
        bds.setPassword(env.getProperty("SPRING_DATASOURCE_PASSWORD"));
        bds.setMaxActive(255);
        bds.setMaxIdle(5);
        bds.setMaxWait(1000);
        return bds;
    }

    @Profile("production")
    @Bean
    public BasicDataSource productionDataSource(){
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(System.getenv("SPRING_DRIVER_CLASSNAME"));
        String host = System.getenv("SPRING_DATABASE_HOST");
        String port = System.getenv("SPRING_DATABASE_PORT");
        String dbName = System.getenv("SPRING_DATABASE_NAME");
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        logger.info("[jdbcUrl]: " + jdbcUrl);
        bds.setUrl(jdbcUrl);
        bds.setUsername(System.getenv("SPRING_DATASOURCE_USERNAME"));
        bds.setPassword(System.getenv("SPRING_DATASOURCE_PASSWORD"));
        bds.setMaxActive(255);
        bds.setMaxIdle(5);
        bds.setMaxWait(1000);
        return bds;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException{

        SqlSessionFactoryBean sqlSessionFactoryBean =
                new SqlSessionFactoryBean();
        if (env.getProperty("spring.profiles.active").equals("dev")) {
            sqlSessionFactoryBean.setDataSource(devDataSource());
        }
        if (env.getProperty("spring.profiles.active").equals("production")) {
            sqlSessionFactoryBean.setDataSource(productionDataSource());
        }

        // Set MyBatis Configuration file
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/mybatis-config.xml"));

        // Find corresponding mapper.xml files
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/sqlMapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);


        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dstm =
                new DataSourceTransactionManager();
        if (env.getProperty("spring.profiles.active").equals("dev")) {
            dstm.setDataSource(devDataSource());
        }
        if (env.getProperty("spring.profiles.active").equals("production")) {
            dstm.setDataSource(productionDataSource());
        }
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

    @Bean
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(300);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate initRedisTemplate(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大空間數
        poolConfig.setMaxIdle(50);
        // 最大連接數
        poolConfig.setMaxTotal(100);
        // 最大等待毫秒數
        poolConfig.setMaxWaitMillis(20000);
        // 創建Jedis連接工廠
        JedisConnectionFactory connectionFactory =
                new JedisConnectionFactory();
        connectionFactory.setHostName("localhost");
        connectionFactory.setPort(6379);
        // 調用後初始化方法，沒有他就會拋出異常
        connectionFactory.afterPropertiesSet();
        // 自定義redis序列化器
        RedisSerializer jdkSerializationRedisSerializer =
                new JdkSerializationRedisSerializer();
        RedisSerializer stringRedisSerializer =
                new StringRedisSerializer();

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);
        return redisTemplate;
    }

    @Bean(name = "redisCacheManager")
    public CacheManager initRedisCacheManager(@Autowired RedisTemplate redisTemplate){
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 設置超時時間為10分鐘，單位為秒
        cacheManager.setDefaultExpiration(600);
        // 設置緩存名稱
        List<String> cacheNames = new ArrayList<>();
        cacheNames.add("redisCacheManager");
        cacheManager.setCacheNames(cacheNames);
        return cacheManager;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
