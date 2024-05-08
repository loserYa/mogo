package com.loser;

import com.loser.core.cache.BaseContext;
import com.loser.core.cache.global.InterceptorCache;
import com.loser.core.cache.global.ReplacerCache;
import com.loser.core.logic.interceptor.CollectionLogiceInterceptor;
import com.loser.core.logic.interceptor.LogicAutoFillInterceptor;
import com.loser.core.logic.replacer.LogicGetByIdReplacer;
import com.loser.core.logic.replacer.LogicListByIdsReplacer;
import com.loser.core.logic.replacer.LogicRemoveByIdReplacer;
import com.loser.core.logic.replacer.LogicRemoveReplacer;
import com.loser.core.logic.replacer.LogicUpdateByIdReplacer;
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
        ReplacerCache.replacers.add(new LogicGetByIdReplacer());
        ReplacerCache.replacers.add(new LogicListByIdsReplacer());
        ReplacerCache.replacers.add(new LogicRemoveByIdReplacer());
        ReplacerCache.replacers.add(new LogicRemoveReplacer());
        ReplacerCache.replacers.add(new LogicUpdateByIdReplacer());
        ReplacerCache.sorted();

        InterceptorCache.interceptors.add(new CollectionLogiceInterceptor());
        InterceptorCache.interceptors.add(new LogicAutoFillInterceptor());
        InterceptorCache.sorted();

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
        Loser byId = mapper1.getById(1715187919793L);
        System.out.println(byId);
    }

}
