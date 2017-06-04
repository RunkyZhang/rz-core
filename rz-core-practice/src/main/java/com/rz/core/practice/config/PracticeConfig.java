package com.rz.core.practice.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.rz.core.practice.dynamic.AspectProxy;
import com.rz.core.practice.dynamic.AspectWork;
import com.rz.core.practice.dynamic.Worker;

@Configuration
@EnableAspectJAutoProxy
public class PracticeConfig {
    private static final String ENV_KEY = "spring.profiles.active";

    @Autowired
    private StandardEnvironment standardEnvironment;

    @Value("${elasticsearch.hostName}")
    private String elasticsearchHostName;

    @Value("${elasticsearch.port:9300}")
    private int elasticsearchPort;

    @Value("${elasticsearch.clusterName}")
    private String elasticsearchClusterName;

    @Bean
    public AspectProxy annotationBroker() {
        return new AspectProxy();
    }

    @Bean
    public Worker woker() {
        return new Worker();
    }

    @Bean
    public AspectWork userService() {
        return new AspectWork();
    }
    
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter4 fastConverter = new FastJsonHttpMessageConverter4();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        System.out.println(fastJsonConfig.getDateFormat());
        fastJsonConfig.setSerializerFeatures(SerializerFeature.UseISO8601DateFormat);
        
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

    @Bean
    public Map<String, String> properties() {
        String activeProfile = this.standardEnvironment.getProperty(ENV_KEY);

        Map<String, String> map = new HashMap<String, String>();
        System.out.println("application-" + activeProfile + ".properties");
        String propertySourcesName = "applicationConfig: [classpath:/application-" + activeProfile + ".properties]";
        for (PropertySource<?> propertySources : this.standardEnvironment.getPropertySources()) {
            if (true == StringUtils.endsWithIgnoreCase(propertySourcesName, propertySources.getName()) && propertySources.getSource() instanceof Properties) {
                Properties properties = (Properties) propertySources.getSource();

                for (Entry<Object, Object> property : properties.entrySet()) {
                    map.put(property.getKey().toString(), property.getValue().toString());
                }
            }
        }

        return map;
    }
}
