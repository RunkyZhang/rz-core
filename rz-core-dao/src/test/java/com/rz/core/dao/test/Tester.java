package com.rz.core.dao.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import com.rz.core.SystemException;
import com.rz.core.dao.DecryptDruidSource;

public class Tester {
    public static void main(String[] args) {
        Tester tester = new Tester();
        try {
            tester.buildDataSource(new HashMap<String, Object>());
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    
    private DataSource buildDataSource(Map<String, Object> ds) {
        DruidDataSource druidDataSource = new DecryptDruidSource();
        try {
            druidDataSource.setDriverClassName((String) ds.get("driverClassName"));
            druidDataSource.setUrl((String) ds.get("url"));
            druidDataSource.setUsername((String) ds.get("username"));
            druidDataSource.setPassword((String) ds.get("password"));

            String initialSize = (String) ds.get("initialSize");
            if (initialSize != null) {
                druidDataSource.setInitialSize(Integer.valueOf(initialSize));
            }

            String maxActive = (String) ds.get("maxActive");
            if (maxActive != null) {
                druidDataSource.setMaxActive(Integer.valueOf(maxActive));
            }

            String minIdle = (String) ds.get("minIdle");
            if (minIdle != null) {
                druidDataSource.setMinIdle(Integer.valueOf(minIdle));
            }

            String maxWait = (String) ds.get("maxWait");
            if (maxWait != null) {
                druidDataSource.setMaxWait(Long.valueOf(maxWait));
            }

            String timeBetweenEvictionRunsMillis = (String) ds.get("timeBetweenEvictionRunsMillis");
            if (timeBetweenEvictionRunsMillis != null) {
                druidDataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(timeBetweenEvictionRunsMillis));
            }

            String minEvictableIdleTimeMillis = (String) ds.get("minEvictableIdleTimeMillis");
            if (minEvictableIdleTimeMillis != null) {
                druidDataSource.setMinEvictableIdleTimeMillis(Long.valueOf(minEvictableIdleTimeMillis));
            }

            druidDataSource.setTestWhileIdle(true);
            druidDataSource.setTestOnBorrow(false);
            druidDataSource.setTestOnReturn(false);

            druidDataSource.setPoolPreparedStatements(true);

            String maxPoolPreparedStatementPerConnectionSize = (String) ds.get("maxPoolPreparedStatementPerConnectionSize");
            if(maxPoolPreparedStatementPerConnectionSize != null){
                druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(maxPoolPreparedStatementPerConnectionSize));
            }

            druidDataSource.setValidationQuery("select 1");
            // 超过时间限制是否回收
            druidDataSource.setRemoveAbandoned(false);
            // 超时时间；单位为秒。180秒=3分钟
            druidDataSource.setRemoveAbandonedTimeout(180);
            // 关闭abanded连接时输出错误日志
            druidDataSource.setLogAbandoned(false);
            
            WallFilter wallFilter = null;
            StatFilter statFilter = null;
            druidDataSource.setProxyFilters(Arrays.asList(wallFilter, statFilter));

            druidDataSource.init();

        } catch (Exception e) {
            throw new SystemException(e);
        }

        return druidDataSource;
    }
}
