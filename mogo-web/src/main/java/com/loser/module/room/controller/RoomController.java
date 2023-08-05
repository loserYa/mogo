package com.loser.module.room.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.loser.common.anto.ApiConst;
import com.loser.common.anto.ApiVersion;
import com.loser.common.entity.APiResponsePage;
import com.loser.common.entity.ApiResponse;
import com.loser.module.room.handler.RoomHandler;
import com.loser.module.room.vo.req.RoomPageReq;
import com.loser.module.room.vo.req.RoomSaveReq;
import com.loser.module.room.vo.req.RoomUpdateReq;
import com.loser.module.room.vo.resp.RoomInfoResp;
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
 * 房间 接口
 *
 * @author loser
 * @date 2023-08-05 20:26:45
 */
@RestController
@RequestMapping("/api/room/room")
@ApiVersion(ApiConst.LOSER)
@Api(tags = "房间 接口")
public class RoomController {

    @Autowired
    private RoomHandler roomHandler;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "01-新增房间")
    @PostMapping("/save")
    public ApiResponse<Boolean> save(@RequestBody RoomSaveReq req) {
        return roomHandler.save(req);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "02-修改房间")
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody RoomUpdateReq req) {
        return roomHandler.update(req);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "03-删除房间")
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Boolean> deleteById(@ApiParam(value = "id") @PathVariable String id) {
        return roomHandler.deleteById(id);
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "04-通过ID查询房间")
    @GetMapping("/get/{id}")
    public ApiResponse<RoomInfoResp> getById(@ApiParam(value = "id") @PathVariable String id) {
        return roomHandler.getById(id);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "05-分页查询房间")
    @PostMapping("/page")
    public APiResponsePage<RoomInfoResp, Object> queryList(@RequestBody RoomPageReq req) {
        return roomHandler.queryList(req);
    }

}
