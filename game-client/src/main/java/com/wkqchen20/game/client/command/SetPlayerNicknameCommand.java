package com.wkqchen20.game.client.command;

import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import io.netty.channel.Channel;

import static com.wkqchen20.game.common.command.ServerCommand.NEW_PLAYER;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.SET_PLAYER_NICKNAME)
public class SetPlayerNicknameCommand implements Command {

    @Override
    public void execute(Channel channel, String msg) {
        Printer.waitForInput("请设置昵称：");
        String name = Inputs.input();
        // TODO name check
        channel.writeAndFlush(TransferRequestResponseDataProto.newBuilder().setCommand(NEW_PLAYER).setData(name));
    }
}
