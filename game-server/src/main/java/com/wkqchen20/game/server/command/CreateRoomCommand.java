package com.wkqchen20.game.server.command;

import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.command.ServerCommand;
import com.wkqchen20.game.core.*;
import com.wkqchen20.game.landlords.core.CommonLGameType;
import com.wkqchen20.game.landlords.core.LRoom;
import com.wkqchen20.game.server.Context;

import java.util.function.Supplier;

import static io.vavr.API.*;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/9
 */
@CommandTag(ServerCommand.CREATE_ROOM)
public class CreateRoomCommand implements Command {

    @Override
    public ExecuteResult execute(Player player, String data) {
        GameType gameType = Context.clientInfo(player).getGameType();
        Class clazz = gameType.getClass();
        Room room = Match(clazz).of(
                Case($(CommonLGameType.class), LRoom.create(data, (HumanPlayer) player, gameType)),
                // TODO
                Case($(), (Supplier<Room>) () -> {
                    throw new UnsupportedOperationException();
                })
        );
        player.createRoom(room);
        Context.newRoom(gameType, room);
        return ExecuteResult.success(room);
    }

}
