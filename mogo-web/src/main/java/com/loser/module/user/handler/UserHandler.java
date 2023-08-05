package com.loser.module.user.handler;

import com.loser.common.entity.APiResponsePage;
import com.loser.common.entity.ApiResponse;
import com.loser.common.utils.ParamsUtil;
import com.loser.core.entity.Page;
import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.module.user.entity.User;
import com.loser.module.user.service.UserService;
import com.loser.module.user.vo.req.UserPageReq;
import com.loser.module.user.vo.req.UserSaveReq;
import com.loser.module.user.vo.req.UserUpdateReq;
import com.loser.module.user.vo.resp.UserInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 测试数据 处理器
 *
 * @author loser
 * @date 2023-02-05  14:01
 */
@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    /**
     * 新增测试数据
     */
    public ApiResponse<Boolean> save(UserSaveReq req) {

        ParamsUtil.checkNotNull(req);
        User save = ParamsUtil.copyProperties(req, User.class);
        return ApiResponse.ok(userService.save(save));

    }

    /**
     * 修改测试数据
     */
    public ApiResponse<Boolean> update(UserUpdateReq req) {

        ParamsUtil.checkNotNull(req);
        User update = ParamsUtil.copyProperties(req, User.class);
        return ApiResponse.ok(userService.updateById(update));

    }

    /**
     * 通过id删除测试数据
     */
    public ApiResponse<Boolean> deleteById(Long id) {

        ParamsUtil.checkNotNull(id);
        return ApiResponse.ok(userService.removeById(id));

    }

    /**
     * 通过id获取测试数据
     */
    public ApiResponse<UserInfoResp> getById(Long id) {

        ParamsUtil.checkNotNull(id);
        User dbData = userService.getById(id);
        return ApiResponse.ok(ParamsUtil.copyProperties(dbData, UserInfoResp.class));

    }

    /**
     * 分页获取测试数据
     */
    public APiResponsePage<UserInfoResp, Object> queryList(UserPageReq req) {

        LambdaQueryWrapper<User> query = Wrappers.<User>lambdaQuery()
                .eq(Objects.nonNull(req.getAge()), User::getAge, req.getAge())
                .eq(Objects.nonNull(req.getLoginName()), User::getLoginName, req.getLoginName())
                .eq(Objects.nonNull(req.getPassWord()), User::getPassWord, req.getPassWord())
                .eq(Objects.nonNull(req.getAge()), User::getAge, req.getAge())
                .between((Objects.nonNull(req.getStartTime()) && Objects.nonNull(req.getEndTime())), User::getCreateTime, req.getStartTime(), req.getEndTime());
        Page<User> page = userService.page(query, req.getPageNo(), req.getPageSize());

        return APiResponsePage.okPage(page, UserInfoResp.class);

    }

}
