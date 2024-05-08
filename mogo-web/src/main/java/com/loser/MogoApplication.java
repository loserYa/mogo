package com.loser;

import com.loser.core.cache.BaseContext;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.module.loser.Loser;
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
        BaseMapper<Long, User> mapper = userService.getMapper();
        BaseMapper<Long, User> userMapper = BaseContext.getMapper(Long.class, User.class);
        BaseMapper<Long, Loser> mapper1 = BaseContext.getMapper(Long.class, Loser.class);
        BaseMapper<Long, Loser> mapper2 = BaseContext.getMapper(Long.class, Loser.class);
        Loser loser = new Loser();
        loser.setId(System.currentTimeMillis());
        loser.setLoginName("loser");
        loser.setPassWord("loser");
        loser.setAge(10);
        loser.setCreateTime(System.currentTimeMillis());
        loser.setUpdateTime(System.currentTimeMillis());
        mapper1.save(loser);
    }

}
