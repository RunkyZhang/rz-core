package com.rz.core.dao.model;

import com.zhaogang.framework.common.ZGHelper;
import com.zhaogang.framework.dal.masking.DataMaskingTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
public class ModelScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(ModelScan.class.getName()));
        ClassPathMapperScanner classPathMapperScanner = new ClassPathMapperScanner(beanDefinitionRegistry);

        if (null != resourceLoader) {
            classPathMapperScanner.setResourceLoader(resourceLoader);
        }

        List<Class> modelClasses = new ArrayList<>();
        for (String packageName : annotationAttributes.getStringArray("value")) {
            try {
                List<Class> classes = ZGHelper.getClassesByPackage(Thread.currentThread().getContextClassLoader(), packageName);
                if (null != classes) {
                    modelClasses.addAll(classes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Class modelClass : modelClasses) {
            ModelDefinition modelDefinition = new ModelDefinition(modelClass);
            if (!StringUtils.isBlank(modelDefinition.getTableName())) {
                DataMaskingTypeHandler.registerPoDefinition(modelDefinition);
            }
        }
    }
}
