package kr.co.glog;

import com.zaxxer.hikari.HikariDataSource;
import kr.co.glog.common.DataSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"kr.co.glog.domain"})
public class DataSourceConfig {

    @Primary
    @Bean( name="masterDataSource" )
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean( name="slaveDataSource" )
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource, @Qualifier("slaveDataSource") DataSource slaveDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put( "masterDataSource", masterDataSource);
        dataSourceMap.put( "slaveDataSource", slaveDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;

    }


    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {

        return new LazyConnectionDataSourceProxy(routingDataSource);

    }

    /*
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);

    }


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/*.xml"));

        return sqlSessionFactoryBean.getObject();

    }


    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {

        return new SqlSessionTemplate(sqlSessionFactory);

    }

     */

}
