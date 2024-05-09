package com.loser.module.room.service.impl;

import com.loser.core.sdk.impl.MogoServiceImpl;
import com.loser.module.room.entity.Room;
import com.loser.module.room.service.RoomService;
import org.springframework.stereotype.Service;

/**
 * 房间 服务类
 *
 * @author loser
 * @date 2023-08-05 20:26:45
 */
@Service
public class RoomServiceImpl extends MogoServiceImpl<String, Room> implements RoomService {

}
