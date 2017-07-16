package com.rz.core.mongo.test;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.model.Filters;
import com.rz.core.mongo.builder.MongoRepositoryBuilder;
import com.rz.core.mongo.builder.MongoSort;
import com.rz.core.mongo.repository.MongoRepository;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public class Tests {
    private static MongoRepository<ConfigApplicationPo> mongoRepository;

    public static void main(String[] args) {


        mongoRepository = MongoRepositoryBuilder.create(ConfigApplicationPo.class)
                .setDatabaseName("ConfigCenterBusiness")
                .setTableName("ConfigApplication")
                .build();

        Tests tests = new Tests();

        try {
            //tests.testSelect();
            //tests.testCount();
            //tests.testDelete();
            tests.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test() {
        ConfigApplicationPo asd = JSON.parseObject(
                "{\"createdTime\":1500192980072,\"deleted\":false,\"id\":\"asdasd\",\"operationUser\":\"888.666\",\"privateValue1\":\"3333\",\"privateValue2\":\"22222\",\"privateValue3\":\"2222\",\"privateValue4\":\"1111\",\"updatedTime\":1500192980072,\"versions\":[{\"createdTime\":1500192980072,\"default\":true,\"deleted\":false,\"id\":\"0.0\",\"operationUser\":\"888.666\",\"privateValue3\":\"privateValue3\",\"updatedTime\":1500192980072}]}",
                ConfigApplicationPo.class);

        System.out.println(JSON.toJSONString(build("asdasd")));
    }

    private void testInstert() {
        List<ConfigApplicationPo> configApplicationPos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ConfigApplicationPo configApplicationPo = new ConfigApplicationPo();
            configApplicationPo = build("runky.test.app" + i);
            configApplicationPos.add(configApplicationPo);
        }

        //mongoRepository.insert(configApplicationPos);


//        mongoRepository.updateById("runky.test.app", configApplicationPo);

    }

    private void testSelect() {
        List<ConfigApplicationPo> configApplicationPos = mongoRepository.selectAll();
        ConfigApplicationPo configApplicationPo = mongoRepository.selectById("runky.test.app");

        Bson bson = Filters.and(
                Filters.eq("operationUser", "888.666"),
                Filters.lt("createdTime", new Date().getTime()));

        configApplicationPos = mongoRepository.select(
                bson,
                0,
                1000,
                Arrays.asList(
                        new MongoSort("createdTime", true),
                        new MongoSort("id", false)));

        configApplicationPo = mongoRepository.selectFirst(
                bson,
                20,
                20,
                Arrays.asList(
                        new MongoSort("createdTime", true),
                        new MongoSort("id", false)));

        List<Map> maps = mongoRepository.select(
                bson,
                0,
                1000,
                Arrays.asList(
                        new MongoSort("createdTime", true),
                        new MongoSort("id", false)),
                "id", "updatedTime");

        Map map = mongoRepository.selectFirst(
                bson,
                20,
                20,
                Arrays.asList(
                        new MongoSort("createdTime", true),
                        new MongoSort("id", false)),
                "id", "updatedTime");
    }

    private void testCount() {
        Bson bson = Filters.and(
                Filters.eq("createdTime", 1500015152201L));


        System.out.println(mongoRepository.count(bson));
        System.out.println(mongoRepository.countById("runky.test.app15"));
        System.out.println(mongoRepository.count());
    }

    private void testDelete() {
        Bson bson = Filters.and(
                Filters.eq("createdTime", 1500015152201L));


        System.out.println(mongoRepository.delete(bson));
        System.out.println(mongoRepository.deleteById("runky.test.app88"));
    }

    private ConfigApplicationPo build(String id) {
        Set<String> versionIds = new HashSet<>();
        versionIds.add("0.0");

        Date now = new Date();
        List<ConfigApplicationVersionPo> configApplicationVersionPos = new ArrayList<>();
        for (String version : versionIds) {
            ConfigApplicationVersionPo configApplicationVersionPo = new ConfigApplicationVersionPo();
            configApplicationVersionPo.setId(version);
            configApplicationVersionPo.setDefault(false);
            configApplicationVersionPo.setCreatedTime(now);
            configApplicationVersionPo.setOperationUser("888.666");
            configApplicationVersionPo.setUpdatedTime(now);
            configApplicationVersionPos.add(configApplicationVersionPo);
        }
        configApplicationVersionPos.get(0).setDefault(true);
        ConfigApplicationPo configApplicationPo = new ConfigApplicationPo();
        configApplicationPo.setId(id);
        configApplicationPo.setVersions(configApplicationVersionPos);
        configApplicationPo.setCreatedTime(now);
        configApplicationPo.setOperationUser("888.666");
        configApplicationPo.setUpdatedTime(now);

        return configApplicationPo;
    }
}