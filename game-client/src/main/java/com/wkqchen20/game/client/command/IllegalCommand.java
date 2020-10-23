package com.wkqchen20.game.client.command;

import com.wkqchen20.game.client.util.Printer;
import com.wkqchen20.game.common.command.ClientCommand;
import com.wkqchen20.game.common.command.CommandTag;
import io.netty.channel.Channel;

/**
 * @Author: wkqchen20
 * @Date: 2020/10/14
 */
@CommandTag(ClientCommand.ILLEGAL)
public class IllegalCommand implements Command {

    @Override
    public void execute(Channel channel, String msg) {
        Printer.notice(msg);
    }
}
