package com.rz.core.practice;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rz.core.CloneMachine;
import com.rz.core.practice.config.PracticeConfig;
import com.rz.core.practice.config.SelfXmlBeanConfig;
import com.rz.core.practice.model.NormalDto;
import com.rz.core.practice.model.TypeDto;
import com.rz.core.practice.util.AppShutdownHandler;

@SpringBootApplication
// @ComponentScan({ "com.hujiang.basic.framework.rest",
// "com.hujiang.basic.framework.plugin.cache",
// "com.hujiang.notifycenter.apppush", "com.hujiang.notifycenter.core" })
@Import({PracticeConfig.class})
// @Import({ SelfXmlBeanConfig.class })
public class PracticeApplication {
    public static void main(String[] args) throws Exception {
        Pattern pattern = Pattern.compile("1[0-9]{10}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("123456");
        System.out.println(matcher.find());


        SpringApplication.run(PracticeApplication.class, new String[]{});

        Map<String, Date> map = new HashMap<>();
        map.put("ddd", new Date());
        String dateJson = JSON.toJSONString(map, SerializerFeature.UseISO8601DateFormat);
        Map date1 = JSON.parseObject(dateJson, Map.class);
        date1 = JSON.parseObject("{\"ddd\":\"2017-04-17T16:58:16.0254752+08:00\"}", Map.class);
//        
//        List<String> ssss = new ArrayList<>();
//        ssss.addAll(null);
//        List<String> allUserIds = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19");
//        int fromIndex = 0;
//        int toIndex = 0;     
//        int sizePerTime = 3;
//        while(fromIndex < allUserIds.size()){
//            toIndex = allUserIds.size()< fromIndex + sizePerTime ? allUserIds.size() : fromIndex + sizePerTime;
//            
//            List<String> userIds = allUserIds.subList(fromIndex, toIndex);
//            for(String userId : userIds){
//                System.out.println(userId);
//            }
//            System.out.println("******************");
//            fromIndex = fromIndex + sizePerTime;
//        }
//        
//        Date date = new Date(2015, 8, 8, 12, 12, 12);
//        Date now = new Date(date.getTime());

        // testCollection();
        // testAppShutdown();
        // testClone();
        // testRunString();

        // Integer tint1 = 25;
        // Integer tint2 = tint1;
        // tint2 = 26;
        // System.out.println("Integer: " + tint1.equals(tint2));
        //
        // Date tdate1 = new Date();
        // Date tdate2 = tdate1;
        // tdate1.setDate(2);
        // System.out.println("Date: " + tdate1.equals(tdate2));

        // A a = new A();
        // B b = new B();
        // a.setB(b);
        // b.setA(a);
        // a.hashCode();
        // b.hashCode();
        // System.out.println(a.equals(b));
        //
        //
        // TypeDto typeDto2 = typeDto1;
        // TypeDto.change(typeDto2);
        //
        // Class<?> clazz = TypeDto.class;
        // Field[] fields = clazz.getDeclaredFields();
        // for (Field field : fields) {
        // System.out.println(field.getType() + " " +
        // field.getType().isPrimitive());
        // }

        // Map<String, String> map = new HashMap<>();
        // System.out.println("map instanceof Map: " + (map instanceof Map));
        // map.put("key1", "value1");
        // map.put("key2", "value2");
        // map.put("key3", "value3");
        // map.put("key4", "value4");
        // map.put("key5", "value5");
        // Map mapValues = (Map)map;
        //
        // for (Object object : mapValues.entrySet()) {
        // Map.Entry asd = (Map.Entry)object;;
        // System.out.println(asd.getKey() + " " + asd.getValue());
        // }
        //
        // List<Integer> list = new ArrayList<>();
        // list.add(111);
        // list.add(222);
        // list.add(333);
        // list.add(444);
        // list.add(555);
        // Class<?>[] clazzes = list.getClass().getInterfaces();
        // for (Class<?> clazz : clazzes) {
        // System.out.println(clazz);
        // if (List.class == clazz) {
        // System.out.println(clazz);
        // }
        // }
        // List listValues = (List)list;
        //
        // for (Object object : listValues) {
        // System.out.println(object);
        // }

        //
        // Field field = TypeDto.class.getDeclaredField("tint");
        // field.setAccessible(true);
        // field.set(typeDto, 1111);
        // System.out.println(field.get(typeDto));
        //
        // int[] values = new int[]{2, 2, 4, 6, 7};
        // int[] newValues =
        // (int[])Array.newInstance(values.getClass().getComponentType(),
        // Array.getLength(values));
        // for(int i = 0; i < Array.getLength(values); i++){
        // System.out.println(Array.get(values, i));
        // Array.set(newValues, i, Array.get(values, i));
        // }
        //
        // System.out.println(newValues.length);

        // System.out.println(DateTimeUtils.addYear(null, 100));

        // ElasticsearchHelper.Test();

        // AnnotationUtils
        // AopUtils just for spring
        // ReflectionUtils

        // String uuid = UUID.randomUUID().toString();
        // System.out.println(uuid);
        //
        // String asd= null;
        // String dsa = "asdasd" + asdApplicationContext;
        // System.out.println(dsa);
        //
        // String[] flagSplit = "ssss,2222,4444,dddd".split(",");
        // System.out.println(flagSplit[2]);

        System.out.println("End PracticeApplication...");
    }

    protected static void testClone() throws Exception {
        TypeDto typeDto = TypeDto.build();
        TypeDto newTypeDto = CloneMachine.clone(typeDto);
        TypeDto.change(newTypeDto);
        System.out.println("newTypeDto: " + newTypeDto);
        System.out.println("typeDto   : " + typeDto);
    }

    protected static void testAppShutdown() {
        AppShutdownHandler.addDefaultHandler();
    }

    protected static void testCollection() {
        Set<Object> set = new HashSet<Object>();
        Object obj = new Object();
        set.add(obj);
        set.add(obj);

        Collections.singletonList(new Object());
        Collections.unmodifiableList(Arrays.asList("1", "2"));

        Map<String, String> map = new HashMap<>();
        map.put("key", "value1");
        map.put("key2", "value2");
        map.put("key", "value3");

        for (Map.Entry<String, String> entity : map.entrySet()) {
            map.put("asd", "dsa");
        }
    }

    protected static void testRunString() {
        boolean flag = false;

        if (flag) {
            // need @Import({ PracticeConfig.class })
            SpringApplication.run(PracticeApplication.class, new String[]{});

        } else {
            Class<?> configClazz = PracticeConfig.class;
            configClazz = SelfXmlBeanConfig.class;

            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(configClazz);
            // can lazy register and refresh config
            // ((AnnotationConfigApplicationContext) applicationContext).register(configClazz);
            // ((AnnotationConfigApplicationContext) applicationContext).refresh();
            System.out.println(StringUtils.join(applicationContext.getBeanDefinitionNames(), ","));

            NormalDto normalDto = (NormalDto) applicationContext.getBean("dataSourceCheck");
            System.out.println(normalDto);

//            AspectWork aspectWork = applicationContext.getBean(AspectWork.class);
//            aspectWork.run(111);
        }
    }

    protected static void testRegex() {
        String asd = "12345{{{0}}}67899adqweqweqweqweqwe";
        asd = asd.replaceAll("\\{\\{\\{(\\d{1,})\\}\\}\\}", "{$1}");
        Pattern pattern = Pattern.compile("\\{\\{\\{(\\d{1,})\\}\\}\\}");
        Matcher matcher = pattern.matcher(asd);
        asd = matcher.replaceAll("{$1}");
    }
}
