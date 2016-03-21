package com.nano.config.system;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

@Configuration
@MapperScan(basePackages = "com.nano.persistence.system")
@EnableTransactionManagement
public class DataSourceConfig {

	@Value("${driverClassName}")
	private String driverClassName;

	@Value("${username}")
	private String username;

	@Value("${password}")
	private String password;

	@Value("${url}")
	private String url;

	@Value("${initialSize}")
	private int initialSize;

	@Value("${minIdle}")
	private int minIdle;

	@Value("${maxActive}")
	private int maxActive;

	@Value("${dialect:mysql}")
	private String dialect;

	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setUrl(url);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive); // 启用监控统计功能
		// dataSource.setFilters("stat");// for mysql
		// dataSource.setPoolPreparedStatements(false);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource,PlatformTransactionManager platformTransactionManager)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		PageHelper helper = new PageHelper();
		Properties properties = new Properties();
		properties.put("dialect", dialect);
		// 设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用
		properties.put("offsetAsPageNum", "true");
		// 设置为true时，使用RowBounds分页会进行count查询
		properties.put("rowBoundsWithCount", "true");
		// 设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果
		properties.put("pageSizeZero", "true");
		// 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
		// 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
		properties.put("reasonable", "true");
		// properties.put("params", "pageNum=start;pageSize=limit;");
		helper.setProperties(properties);
		sessionFactory.setPlugins(new Interceptor[]{helper});
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		return sessionFactory;
	}
	
	@Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) { 
        return new JdbcTemplate(dataSource); 
    }
}
