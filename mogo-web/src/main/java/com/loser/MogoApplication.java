package com.loser;

import com.loser.core.cache.BaseContext;
import com.loser.core.config.MogoConfiguration;
import com.loser.core.logic.entity.LogicProperty;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.module.loser.Loser;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootConfiguration
@SpringBootApplication
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
        return MogoConfiguration.instance();

    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(MogoApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        BaseMapper<Long, User> mapper = userService.getMapper();
        BaseMapper<Long, User> userMapper = BaseContext.getMapper(Long.class, User.class);
        BaseMapper<Long, Loser> mapper1 = BaseContext.getMapper(Long.class, Loser.class);
        BaseMapper<Long, Loser> mapper2 = BaseContext.getMapper(Long.class, Loser.class);
//        Loser loser = new Loser();
//        loser.setId(System.currentTimeMillis());
//        loser.setLoginName("loser");
//        loser.setPassWord("loser");
//        loser.setAge(10);
//        loser.setCreateTime(System.currentTimeMillis());
//        loser.setUpdateTime(System.currentTimeMillis());
//        mapper1.save(loser);
        mapper2.getOne(Wrappers.<Loser>lambdaQuery().eq(Loser::getId, 1715187919793L));
//        Loser byId = mapper1.getById(1715187919793L);
//        System.out.println(byId);
        for (long i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            mapper1.removeById(i);
            System.out.println(System.currentTimeMillis() - start);
        }

    }

}
