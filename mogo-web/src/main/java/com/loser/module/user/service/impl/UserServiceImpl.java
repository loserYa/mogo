package com.loser.module.user.service.impl;

import com.loser.core.ds.MongoDs;
import com.loser.core.impl.MogoServiceImpl;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 测试数据 实现类
 *
 * @author loser
 * @date 2023-02-05  13:58
 */
@Service
@MongoDs("loser")
public class UserServiceImpl extends MogoServiceImpl<Long, User> implements UserService {

    @Override
    public User getById(Long id) {
        return super.getById(id);
    }

}
