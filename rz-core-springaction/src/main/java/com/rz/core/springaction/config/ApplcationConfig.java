package com.rz.core.springaction.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.rz.core.springaction.service.BeanCondition;
import com.rz.core.springaction.service.GameShopManager;
import com.rz.core.springaction.service.SalesManManager;

@EnableAspectJAutoProxy
@PropertySource("classpath:app.properties")
// ComponentScan for auto bean
@ComponentScan(basePackages = { "com.rz.core.springaction" })
// ImportResource for XML bean
@ImportResource(value = { "app-bean.xml" })
// below for java bean
@Configuration
public class ApplcationConfig {
	@Autowired
	private Environment environment;
	
    @Bean
    public SalesManManager salesManManager() {
        return new SalesManManager();
    }

    @Conditional(value = BeanCondition.class)
    @Bean
    public GameShopManager gameShopManager(SalesManManager salesManManager) {
        return new GameShopManager(salesManManager);
    }
    
    @Bean
    public String appUserName(){
    	return environment.getProperty("app.user.name");
    }
    
    @Bean
    public String appPassword(){
    	return environment.getProperty("app.user.password");
    }
}
