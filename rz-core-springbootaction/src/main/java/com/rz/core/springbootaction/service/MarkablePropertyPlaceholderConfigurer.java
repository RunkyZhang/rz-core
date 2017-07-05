package com.rz.core.springbootaction.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

/**
 * Created by renjie.zhang on 7/5/2017.
 */
@Component
public class MarkablePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    public  MarkablePropertyPlaceholderConfigurer(){
        this.setLocation(new ClassPathResource("application.properties"));
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            System.out.println(beanName);

            BeanDefinition beanDefinition = beanFactoryToProcess.getBeanDefinition(beanName);
            MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
            List<PropertyValue> propertyValues = mutablePropertyValues.getPropertyValueList();
            for (PropertyValue propertyValue : propertyValues) {

                System.out.println(propertyValue.getName() + ": " + propertyValue.getValue());
                if (propertyValue.getValue() instanceof TypedStringValue) {
                    System.out.println("TypedStringValue");
                } else {
                    System.out.println("Not TypedStringValue");
                }
            }
        }

        // 执行父类（Spring的属性占位符配置类）程序逻辑
        super.processProperties(beanFactoryToProcess, props);
    }
}
