package com.loser.module.user.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.loser.common.anto.ApiConst;
import com.loser.common.anto.ApiVersion;
import com.loser.common.entity.APiResponsePage;
import com.loser.common.entity.ApiResponse;
import com.loser.module.user.handler.UserHandler;
import com.loser.module.user.vo.req.UserPageReq;
import com.loser.module.user.vo.req.UserSaveReq;
import com.loser.module.user.vo.req.UserUpdateReq;
import com.loser.module.user.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试数据 接口
 *
 * @author loser
 * @date 2023-02-05  14:01
 */
@RestController
@RequestMapping("user")
@ApiVersion(ApiConst.LOSER)
@Api(tags = "用户 接口")
public class UserController {

    @Autowired
    private UserHandler userHandler;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "01-新增测试数据")
    @PostMapping("/save")
    public ApiResponse<Boolean> save(@RequestBody UserSaveReq req) {
        return userHandler.save(req);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "02-修改测试数据")
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody UserUpdateReq req) {
        return userHandler.update(req);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "03-删除测试数据")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Boolean> deleteById(@ApiParam(value = "id") @PathVariable Long id) {
        return userHandler.deleteById(id);
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "04-通过ID查询测试数据")
    @GetMapping("/get/{id}")
    public ApiResponse<UserInfoResp> getById(@ApiParam(value = "id") @PathVariable Long id) {
        return userHandler.getById(id);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "05-分页查询测试数据")
    @PostMapping("/page")
    public APiResponsePage<UserInfoResp, Object> queryList(@RequestBody UserPageReq req) {
        return userHandler.queryList(req);
    }

}
