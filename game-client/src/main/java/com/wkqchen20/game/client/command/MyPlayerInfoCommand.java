package com.wkqchen20.game.client.command;

import com.alibaba.fastjson.JSON;
import com.wkqchen20.game.client.Context;
import com.wkqchen20.game.client.command.Command;
import com.wkqchen20.game.client.util.Inputs;
import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.TransferRequestResponseData.TransferRequestResponseDataProto;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import com.wkqchen20.game.common.entity.DisplayPlayer;
import io.netty.channel.Channel;

import static com.wkqchen20.game.common.command.ServerCommand.NEW_PLAYER;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.MY_PLAYER_INFO)
public class MyPlayerInfoCommand implements Command {

    @Override
    public void execute(Channel channel, String msg) {
        DisplayPlayer displayPlayer = JSON.parseObject(msg, DisplayPlayer.class);
        Context.setPlayer(displayPlayer);
    }
}
