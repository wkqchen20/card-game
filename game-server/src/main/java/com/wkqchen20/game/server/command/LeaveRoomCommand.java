package com.wkqchen20.game.server.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.core.Command;
import com.wkqchen20.game.core.ExecuteResult;
import com.wkqchen20.game.core.Player;

import static com.wkqchen20.game.common.command.ServerCommand.LEAVE_ROOM;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag({LEAVE_ROOM})
public class LeaveRoomCommand implements Command {

    @Override
    public ExecuteResult execute(Player player, String data) {
        player.leaveRoom();
        return ExecuteResult.success();
    }

}
