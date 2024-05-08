package com.loser;

import com.loser.core.cache.BaseContext;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootConfiguration
@SpringBootApplication
public class MogoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MogoApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        BaseMapper<Long, User> mapper1 = BaseContext.getMapper(Long.class, User.class);
        BaseMapper<Long, User> mapper = userService.getMapper();
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setLoginName("");
        user.setPassWord("");
        user.setAge(0);
        user.setCreateTime(0L);
        user.setUpdateTime(0L);
        mapper1.save(user);
        User byId = mapper.getById(1715153384599L);
        System.out.println(byId);
        User byId1 = userService.getById(1715153384599L);
        System.out.println(byId1);
    }

}
