package com.kunlun.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库以及连接池配置
 * Created by kunlun on 2017/3/28.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig implements EnvironmentAware {

    private static Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);


    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }

    @Bean(destroyMethod = "close", initMethod = "init")
    public DataSource writeDataSource() {
        Minerable config = new Miner().getMiner("hcon.base", "datasource");
        String url = config.getString("url");
        String driverClass = config.getString("driver-class");
        String user = config.getString("userName");
        String password = config.getString("password");
        int initSize = config.getInt("initialSize");
        int minIdle = config.getInt("minIdle");
        int maxWait = config.getInt("maxWait");
        int maxActive = config.getInt("maxActive");
        logger.info("注入Druid连接池...");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initSize);
        dataSource.setMaxWait(maxWait);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        return dataSource;
    }
}
