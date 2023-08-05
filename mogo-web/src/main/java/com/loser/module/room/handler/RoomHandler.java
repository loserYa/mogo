package com.loser.module.room.handler;

import com.loser.common.entity.APiResponsePage;
import com.loser.common.entity.ApiResponse;
import com.loser.common.utils.ParamsUtil;
import com.loser.core.entity.Page;
import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.module.room.entity.Room;
import com.loser.module.room.service.RoomService;
import com.loser.module.room.vo.req.RoomPageReq;
import com.loser.module.room.vo.req.RoomSaveReq;
import com.loser.module.room.vo.req.RoomUpdateReq;
import com.loser.module.room.vo.resp.RoomInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试数据 处理器
 *
 * @author loser
 * @date 2023-02-05  14:01
 */
@Component
public class RoomHandler {

    @Autowired
    private RoomService roomService;

    /**
     * 新增测试数据
     */
    public ApiResponse<Boolean> save(RoomSaveReq req) {

        ParamsUtil.checkNotNull(req);
        Room save = ParamsUtil.copyProperties(req, Room.class);
        return ApiResponse.ok(roomService.save(save));

    }

    /**
     * 修改测试数据
     */
    public ApiResponse<Boolean> update(RoomUpdateReq req) {

        ParamsUtil.checkNotNull(req);
        Room update = ParamsUtil.copyProperties(req, Room.class);
        return ApiResponse.ok(roomService.updateById(update));

    }

    /**
     * 通过id删除测试数据
     */
    public ApiResponse<Boolean> deleteById(String id) {

        ParamsUtil.checkNotNull(id);
        return ApiResponse.ok(roomService.removeById(id));

    }

    /**
     * 通过id获取测试数据
     */
    public ApiResponse<RoomInfoResp> getById(String id) {

        ParamsUtil.checkNotNull(id);
        Room dbData = roomService.getById(id);
        return ApiResponse.ok(ParamsUtil.copyProperties(dbData, RoomInfoResp.class));

    }

    /**
     * 分页获取测试数据
     */
    public APiResponsePage<RoomInfoResp, Object> queryList(RoomPageReq req) {

        LambdaQueryWrapper<Room> query = Wrappers.<Room>lambdaQuery();
        Page<Room> page = roomService.page(query, req.getPageNo(), req.getPageSize());
        return APiResponsePage.okPage(page, RoomInfoResp.class);

    }

}
