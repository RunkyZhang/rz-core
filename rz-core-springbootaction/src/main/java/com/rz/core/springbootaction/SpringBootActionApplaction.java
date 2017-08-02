package com.rz.core.springbootaction;

import com.rz.core.springbootaction.service.LoadConfigApplicationContextInitializer;
import com.rz.core.springbootaction.service.PropertyApplicationListener;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by renjie.zhang on 7/5/2017.
 */

@SpringBootApplication
@ComponentScan("com.rz.core.springbootaction")
// for load ServletContextListener
@ServletComponentScan
public class SpringBootActionApplaction {
    public static void main(String[] args) {
        SpringBootActionApplaction.writeLog("server.port=7777\r\napp.user.name=runkyzhang", "application");

        ResourceBanner resourceBanner = new ResourceBanner(new ClassPathResource("banner.txt"));
        new SpringApplicationBuilder()
                .initializers(new LoadConfigApplicationContextInitializer())
                .listeners(new PropertyApplicationListener())
                .sources(SpringBootActionApplaction.class)
                .banner(resourceBanner)
                .run(args);

        //SpringApplication.run(SpringBootActionApplaction.class, args);
    }


    public static void writeLog(String str, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append(str + "\n");
        File file = new File(SpringBootActionApplaction.class.getClassLoader()
                .getResource("").getPath()
                + type + ".properties");
        System.out.println("file:" + file);
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("创建文件报错");
        }
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(file);
            writer.write(sb.toString().getBytes());
        } catch (IOException e) {
            System.out.println("写入文件报错");
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("关闭流报错");
            }
        }
    }
}
