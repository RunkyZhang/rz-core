package com.rz.core.mongo.test;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.model.Filters;
import com.rz.core.mongo.builder.MongoRepositoryBuilder;
import com.rz.core.mongo.builder.MongoSort;
import com.rz.core.mongo.builder.ShardingMongoRepositoryBuilder;
import com.rz.core.mongo.repository.MongoRepository;
import com.rz.core.mongo.repository.ShardingMongoRepository;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public class Tests {
    //private static MongoRepository<ConfigApplicationPo> mongoRepository;
    private static ShardingMongoRepository<AutoIdTestModel, Date> autoIdTestModelRepository;
    private static ShardingMongoRepository<SpecialIdTestModel, Date> specialIdTestModelRepository;

    public static void main(String[] args) {
        Tests.autoIdTestModelRepository = ShardingMongoRepositoryBuilder.create(
                AutoIdTestModel.class,
                new MonthSharding(
                        "mongodb://localhost:27017",
                        "RunkyTest",
                        "AutoIdTestModel")).build();
        Tests.specialIdTestModelRepository = ShardingMongoRepositoryBuilder.create(
                SpecialIdTestModel.class,
                new MonthSharding(
                        "mongodb://localhost:27017",
                        "RunkyTest",
                        "SpecialIdTestModel")).build();

        Tests.specialIdTestModelRepository.createIndex(new Date(), "age", false);

        Tests tests = new Tests();

        try {
//            tests.test();

            //tests.testMax();
            //tests.testIncrease();
            //tests.testUpdate();
            tests.testInstert();
            //tests.testSelect();
            //tests.testCount();
            //tests.testDelete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test() {
//        ConfigApplicationPo asd = JSON.parseObject(
//                "{\"createdTime\":1500192980072,\"deleted\":false,\"id\":\"asdasd\",\"operationUser\":\"888.666\",\"privateValue1\":\"3333\",\"privateValue2\":\"22222\",\"privateValue3\":\"2222\",\"privateValue4\":\"1111\",\"updatedTime\":1500192980072,\"versions\":[{\"createdTime\":1500192980072,\"default\":true,\"deleted\":false,\"id\":\"0.0\",\"operationUser\":\"888.666\",\"privateValue3\":\"privateValue3\",\"updatedTime\":1500192980072}]}",
//                ConfigApplicationPo.class);
//
//        System.out.println(JSON.toJSONString(buildConfigApplication("asdasd")));
    }

    private void testIncrease() {
//        if (Tests.isSharding) {
//            Date now = new Date();
//            ConfigOperationLogPo configOperationLogPo = Tests.shardingMongoRepository.selectById(now, new ObjectId("596c5d7ae42a285f94f08c8e"));
//            System.out.println(configOperationLogPo.getCreatedTime().getTime());
//            System.out.println(Tests.shardingMongoRepository.increaseById(now, configOperationLogPo.getId(), "createdTime", -10));
//        } else {
//
//        }
    }

    private void testMax() {
//        if (Tests.isSharding) {
//            Date now = new Date();
//            System.out.println(Tests.shardingMongoRepository.max(now, "nodeValue"));
//            System.out.println(Tests.shardingMongoRepository.min(now, "nodeValue"));
//        } else {
//
//        }
    }

    private void testInstert() {
//        AutoIdTestModel autoIdTestModel = this.buildAutoIdTestModel(0);
//        autoIdTestModelRepository.insert(new Date(), autoIdTestModel);
//        System.out.println(autoIdTestModel.get_id());

        SpecialIdTestModel specialIdTestModel = this.buildSpecialIdTestModel(0);
        specialIdTestModel.setKey(String.valueOf(new Date().getTime()));
        specialIdTestModelRepository.insert(new Date(), specialIdTestModel);
        System.out.println(specialIdTestModel.getKey());


//        if (Tests.isSharding) {
//            List<ConfigOperationLogPo> configOperationLogPos = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                ConfigOperationLogPo configApplicationPo = new ConfigOperationLogPo();
//                configApplicationPo = buildConfigOperationLog(i);
//                configOperationLogPos.add(configApplicationPo);
//            }
//
//            Date now = new Date();
//            Tests.shardingMongoRepository.insert(now, configOperationLogPos);
//        } else {
//            List<ConfigApplicationPo> configApplicationPos = new ArrayList<>();
//            for (int i = 0; i < 100; i++) {
//                ConfigApplicationPo configApplicationPo = new ConfigApplicationPo();
//                configApplicationPo = buildConfigApplication("runky.test.app" + i);
//                configApplicationPos.add(configApplicationPo);
//            }
//
//            mongoRepository.insert(configApplicationPos);
//        }
    }

    private void testUpdate() {
//        if (Tests.isSharding) {
//            ConfigOperationLogPo configOperationLogPo = new ConfigOperationLogPo();
//            configOperationLogPo.setComment(String.valueOf(new Date().getTime()));
//            configOperationLogPo.setOperationUser("88888888888");
//            configOperationLogPo.setRunEnvironment(RunEnvironmentEnum.PRD);
//            configOperationLogPo.setNodeType(ConfigNodeTypeEnum.SENSITIVE);
//            configOperationLogPo.setDataOperationType(DataOperationTypeEnum.DELETE);
//            configOperationLogPo.setApplicationVersion("1.1");
//            configOperationLogPo.setApplicationId("sssssss");
//            configOperationLogPo.setNodeValue("vvvvvvv");
//            configOperationLogPo.setNodeKey("kkkkkkk");
//            configOperationLogPo.setDeleted(true);
//
//            Date now = new Date();
//
//            configOperationLogPo = Tests.shardingMongoRepository.selectById(now, new ObjectId("596c5d7ae42a285f94f08c86"));
//            configOperationLogPo.setComment(String.valueOf(new Date().getTime()));
//
//            System.out.println(Tests.shardingMongoRepository.updateById(
//                    now, configOperationLogPo.getId(), configOperationLogPo));
//            System.out.println(Tests.shardingMongoRepository.updateById(
//                    now, new ObjectId("596c5d7ae42a285f94f08c86"), configOperationLogPo));
//            System.out.println(Tests.shardingMongoRepository.update(
//                    now,
//                    Filters.or(Filters.eq("nodeValue", "value15"), Filters.eq("nodeValue", "value16")),
//                    configOperationLogPo));
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("comment", "CommentCommentCommentComment");
//            System.out.println(Tests.shardingMongoRepository.updateById(
//                    now, new ObjectId("596c5d7ae42a285f94f08c8e"), map));
//        }
    }

    private void testSelect() {
//        List<ConfigApplicationPo> configApplicationPos = mongoRepository.selectAll();
//        ConfigApplicationPo configApplicationPo = mongoRepository.selectById("runky.test.app");
//
//        Bson bson = Filters.and(
//                Filters.eq("operationUser", "888.666"),
//                Filters.lt("createdTime", new Date().getTime()));
//
//        configApplicationPos = mongoRepository.select(
//                bson,
//                0,
//                1000,
//                Arrays.asList(
//                        new MongoSort("createdTime", true),
//                        new MongoSort("id", false)));
//
//        configApplicationPo = mongoRepository.selectFirst(
//                bson,
//                Arrays.asList(
//                        new MongoSort("createdTime", true),
//                        new MongoSort("id", false)));
//
//        List<Map> maps = mongoRepository.select(
//                bson,
//                0,
//                1000,
//                Arrays.asList(
//                        new MongoSort("createdTime", true),
//                        new MongoSort("id", false)),
//                "id", "updatedTime");
//
//        Map map = mongoRepository.selectFirst(
//                bson,
//                Arrays.asList(
//                        new MongoSort("createdTime", true),
//                        new MongoSort("id", false)),
//                "id", "updatedTime");
    }

    private void testCount() {
//        Bson bson = Filters.and(
//                Filters.eq("createdTime", 1500015152201L));
//
//
//        System.out.println(mongoRepository.count(bson));
//        System.out.println(mongoRepository.countById("runky.test.app15"));
//        System.out.println(mongoRepository.count());
    }

    private void testDelete() {
//        Bson bson = Filters.and(
//                Filters.eq("createdTime", 1500015152201L));
//
//        System.out.println(mongoRepository.delete(bson));
//        System.out.println(mongoRepository.deleteById("runky.test.app88"));
    }

    private AutoIdTestModel buildAutoIdTestModel(int index) {
        AutoIdTestModel autoIdTestModel = new AutoIdTestModel();

        autoIdTestModel.setAge(index);
        autoIdTestModel.setName("Name" + String.valueOf(1000 - index));
        autoIdTestModel.setVersions(new ArrayList<>());
        Date now = new Date();
        autoIdTestModel.setCreatedTime(now);
        for (int i = 0; i < 5; i++) {
            TestVersion testVersion = new TestVersion();
            testVersion.setCurrent(0 == index % 2);
            testVersion.setNumber(String.valueOf(index) + "." + String.valueOf(i));
            autoIdTestModel.getVersions().add(testVersion);
        }
        autoIdTestModel.setDefaultVersion(autoIdTestModel.getVersions().get(0));

        return autoIdTestModel;
    }

    private SpecialIdTestModel buildSpecialIdTestModel(int index) {
        SpecialIdTestModel specialIdTestModel = new SpecialIdTestModel();

        specialIdTestModel.setAge(index);
        specialIdTestModel.setName("Name" + String.valueOf(1000 - index));
        specialIdTestModel.setVersions(new ArrayList<>());
        Date now = new Date();
        specialIdTestModel.setCreatedTime(now);
        for (int i = 0; i < 5; i++) {
            TestVersion testVersion = new TestVersion();
            testVersion.setCurrent(0 == index % 2);
            testVersion.setNumber(String.valueOf(index) + "." + String.valueOf(i));
            specialIdTestModel.getVersions().add(testVersion);
        }
        specialIdTestModel.setDefaultVersion(specialIdTestModel.getVersions().get(0));

        return specialIdTestModel;
    }
}