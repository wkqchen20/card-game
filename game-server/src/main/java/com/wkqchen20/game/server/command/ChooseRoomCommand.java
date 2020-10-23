package com.wkqchen20.game.server.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.constant.Errors;
import com.wkqchen20.game.core.*;
import com.wkqchen20.game.server.Context;
import com.wkqchen20.game.server.entity.ClientInfo;
import io.vavr.control.Either;

import java.util.Optional;

import static com.wkqchen20.game.common.command.ServerCommand.CHOOSE_ROOM;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag({CHOOSE_ROOM})
public class ChooseRoomCommand implements Command {

    @Override
    public ExecuteResult execute(Player player, String data) {
        Integer roomId = Integer.valueOf(data);
        ClientInfo clientInfo = Context.clientInfo(player);
        GameType gameType = clientInfo.getGameType();
        Optional<Room> optional = Context.rooms(gameType).stream().filter(r -> r.getId() == roomId).findAny();
        if (!optional.isPresent()) {
            return ExecuteResult.fail(Errors.ROOM_NOT_FOUND);
        }
        Room room = optional.get();
        Either<Errors, Void> either = player.joinRoom(room);
        if (either.isRight()) {
            return ExecuteResult.success();
        }
        return ExecuteResult.fail(either.getLeft());
    }

}
