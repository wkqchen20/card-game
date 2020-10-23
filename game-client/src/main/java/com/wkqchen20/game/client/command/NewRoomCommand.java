package com.wkqchen20.game.client.command;

import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.command.ServerCommand;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import io.vavr.control.Try;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.NEW_ROOM)
public class NewRoomCommand implements Command {

    @Override
    public void execute(Channel channel, String data) {
        Printer.waitForInput("请输入房间名称，返回上一级按'#'：");
        String input = Inputs.input();
        if ("#".equals(input)) {
            Try.run(() -> {
                String rooms = Context.getRooms();
                Context.setRooms(StringUtil.EMPTY_STRING);
                ClientCommands.getCommand(ClientCommand.OPTION_ROOMS).execute(channel, rooms);
            }).onFailure(ex -> {
                throw new IllegalStateException(ex);
            });
            return;
        }
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(ServerCommand.CREATE_ROOM).setData(input).build());
    }
}
