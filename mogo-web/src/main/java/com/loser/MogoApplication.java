package com.loser;

import com.loser.core.anno.EnableMogo;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.global.cache.MongoTemplateCache;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

@SpringBootConfiguration
@SpringBootApplication
@EnableMogo
public class MogoApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(MogoApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        UserService userService1 = context.getBean(UserService.class);
        Map<String, MongoTemplate> cache = MongoTemplateCache.CACHE;
        MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);
        System.out.println("values = " + cache);
        BaseMapper<Long, User> mapper = userService.getMapper();
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
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setLoginName("");
        user.setPassWord("");
        user.setAge(0);
        user.setCreateTime(0L);
        user.setUpdateTime(0L);
        userService.save(user);

        User byId = userService.getById(1715249422667L);
        byId = userService.getById(user.getId());
        System.out.println(byId);
//        RoomService roomService = context.getBean(RoomService.class);
//        MongoTemplate template = roomService.getTemplate();
//        roomService.getById("111");
//        System.out.println(byId);
    }

}
