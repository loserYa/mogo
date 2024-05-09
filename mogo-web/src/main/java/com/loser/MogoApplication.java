package com.loser;

import com.loser.config.MogoConfiguration;
import com.loser.module.logic.entity.LogicProperty;
import com.loser.module.room.service.RoomService;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootConfiguration
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class MogoApplication {

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration() {

        LogicProperty logicProperty = new LogicProperty();
        logicProperty.setAutoFill(true);
        logicProperty.setOpen(true);
        logicProperty.setLogicDeleteField("logicDel");
        logicProperty.setLogicDeleteValue("1");
        logicProperty.setLogicNotDeleteValue("0");
        MogoConfiguration.instance().logic(logicProperty);

        MongoClient mongoClient = MongoClients.create("mongodb://loser:loser@119.91.155.174:27017/loser?authMechanism=SCRAM-SHA-1");
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, "errorLoser");
        MogoConfiguration.instance().template("master", mongoTemplate);

        MongoClient loserMongoClient = MongoClients.create("mongodb://loser:loser@119.91.155.174:27017/loser?authMechanism=SCRAM-SHA-1");
        MongoTemplate loserMongoTemplate = new MongoTemplate(loserMongoClient, "loser");
        MogoConfiguration.instance().template("loser", loserMongoTemplate);

        return MogoConfiguration.instance();

    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(MogoApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        UserService userService1 = context.getBean(UserService.class);
//        BaseMapper<Long, User> mapper = userService.getMapper();
//        BaseMapper<Long, User> userMapper = BaseContext.getMapper(Long.class, User.class);
//        BaseMapper<Long, Loser> mapper1 = BaseContext.getMapper(Long.class, Loser.class);
//        BaseMapper<Long, Loser> mapper2 = BaseContext.getMapper(Long.class, Loser.class);
////        MongoTemplate bean = context.getBean(MongoTemplate.class);
//        Loser loser = new Loser();
//        loser.setId(System.currentTimeMillis());
//        loser.setLoginName("loser");
//        loser.setPassWord("loser");
//        loser.setAge(10);
//        loser.setCreateTime(System.currentTimeMillis());
//        loser.setUpdateTime(System.currentTimeMillis());
////        mapper1.save(loser);
////        mapper2.getOne(Wrappers.<Loser>lambdaQuery().eq(Loser::getId, 1715187919793L));
//////        Loser byId = mapper1.getById(1715187919793L);
//////        System.out.println(byId);
////        for (long i = 0; i < 100; i++) {
////            long start = System.currentTimeMillis();
////            mapper1.removeById(i);
////            System.out.println(System.currentTimeMillis() - start);
////        }
        User byId = userService.getById(1715249422667L);
        byId = userService.getById(1715249422667L);
        RoomService roomService = context.getBean(RoomService.class);
        roomService.getById("111");
        System.out.println(byId);
    }

}
